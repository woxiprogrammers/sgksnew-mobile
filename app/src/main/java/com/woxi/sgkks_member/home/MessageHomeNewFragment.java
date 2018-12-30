package com.woxi.sgkks_member.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.adapters.MessageListAdapter;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.EndlessRvScrollListener;
import com.woxi.sgkks_member.interfaces.FragmentInterface;
import com.woxi.sgkks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageHomeNewFragment extends Fragment implements AppConstants, FragmentInterface {
    private View mParentView;
    private Context mContext;
    private RecyclerView mRvMessageList;
    private RelativeLayout mPbLazyLoad;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter mRvAdapter;
    private ProgressBar pbMessages;
    //private DatabaseQueryHandler databaseQueryHandler;
    public static View.OnClickListener onRvItemClickListener;
    private ArrayList<MessageDetailsItem> mArrMessageDetails;
    private int pageNumber = 0, arrSize = 0;
    private boolean isApiInProgress = false, isApiRequested = false;
    private DatabaseQueryHandler databaseQueryHandler;
   // ArrayList<MessageDetailsItem> arrOfflineMessages;
    public MessageHomeNewFragment() {
        // Required empty public constructor
    }

    public static MessageHomeNewFragment newInstance() {
        return new MessageHomeNewFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_messages_classified_home, container, false);
        //Calling function to initialize required views.
        initializeViews();
        return mParentView;
    }

    private void initializeViews() {
        mContext = getActivity();
        //databaseQueryHandler = new DatabaseQueryHandler(mContext, false);
        mRvMessageList = mParentView.findViewById(R.id.rvNewsAndClassified);
        mPbLazyLoad = mParentView.findViewById(R.id.rlLazyLoad);
        databaseQueryHandler = new DatabaseQueryHandler(mContext,false);
        setUpRecyclerView();
        pbMessages = mParentView.findViewById(R.id.pbMessages);
        boolean isLanguageChanged = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_LANGUAGE_CHANGED,mContext);
        boolean isCityChanged = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_CITY_CHANGED,mContext);
        if(isLanguageChanged || isCityChanged){
            if(new AppCommonMethods(mContext).isNetworkAvailable()){
                pageNumber=0;
                requestMessageList(pageNumber, true);
            } else {
                mArrMessageDetails = databaseQueryHandler.querryMessages();
                showMessages(mArrMessageDetails);
            }
        }
    }

    private void setUpRecyclerView(){
        mRvMessageList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvMessageList.setLayoutManager(linearLayoutManager);
        onRvItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    MessageDetailsItem messageDetailsItem = mArrMessageDetails.get(mRvMessageList.getChildAdapterPosition(v));
                    Intent intentDetails = new Intent(mContext, MessageDetailsActivity.class);
                    intentDetails.putExtra("currentNewsDetail", messageDetailsItem);
                    startActivity(intentDetails);
            }
        };
        recyclerViewScrollListener();
    }

    private void requestMessageList(int page_id, final boolean isFirstTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = simpleDateFormat.format(new Date());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.PREFS_LAST_MESSAGE_DATE,format);
        editor.apply();
        isApiInProgress = true;
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        if(isFirstTime){
            pDialog.setMessage("Loading, Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        } else {
            pbMessages.setVisibility(View.VISIBLE);
        }
        JSONObject params = new JSONObject();
        try {
            params.put("page_id",page_id);
            params.put("language_id",AppCommonMethods.getStringPref(AppConstants.PREFS_LANGUAGE_APPLIED,mContext));
            params.put("sgks_city",AppCommonMethods.getStringPref(AppConstants.PREFS_CURRENT_CITY,mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,AppURLs.API_MESSAGE_LISTING, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            new AppCommonMethods(mContext).LOG(0,"message_response",response.toString());
                            if (!response.getString("page_id").equalsIgnoreCase("")) {
                                pageNumber = Integer.parseInt(response.getString("page_id"));
                            }
                            Object resp= AppParser.parseMessageNewResponse(response.toString());
                            MessageDetailsItem messageDetailsItem = (MessageDetailsItem) resp;
                            if(resp instanceof Boolean){
                                Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();
                            }else if(resp instanceof MessageDetailsItem){
                                if(isFirstTime){
                                    mArrMessageDetails = messageDetailsItem.getArrMessageList();
                                    if(mArrMessageDetails.size() != 0){
                                       showMessages(mArrMessageDetails);
                                    } else {
                                        Toast.makeText(mContext,"No Records Found",Toast.LENGTH_SHORT);
                                    }
                                    mRvMessageList.setHasFixedSize(true);
                                    mRvAdapter = new MessageListAdapter(mArrMessageDetails);
                                    mRvMessageList.setAdapter(mRvAdapter);
                                } else {
                                    ArrayList <MessageDetailsItem> arrNextMessages = messageDetailsItem.getArrMessageList();
                                    if(arrNextMessages.size() != 0){
                                        showMessages(arrNextMessages);
                                    } else {
                                        Toast.makeText(mContext,"All the Records are Listed",Toast.LENGTH_SHORT);
                                    }
                                }
                            }
                            pDialog.dismiss();
                            pbMessages.setVisibility(View.GONE);
                            isApiInProgress = false;
                            isApiRequested = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req, "messageList");

    }

    private void recyclerViewScrollListener() {
        mRvMessageList.addOnScrollListener(new EndlessRvScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requestLazyLoadMembersApi();
            }
        });
    }

    private void requestLazyLoadMembersApi() {
        if (!isApiInProgress) {
            //Cancelling Pending Request
            AppController.getInstance().cancelPendingRequests("memberSearchList");
            if (new AppCommonMethods(mContext).isNetworkAvailable()){
                requestMessageList(pageNumber,false);
            }/* else {
                new AppCommonMethods(mContext).showAlert("You are offline");
            }*/
        }
    }

    @Override
    public void fragmentBecameVisible() {
        if (!isApiRequested){
            if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                pageNumber=0;
                requestMessageList(pageNumber,true);
            }else {
                mArrMessageDetails = databaseQueryHandler.querryMessages();
                showMessages(mArrMessageDetails);
            }
        } 
    }
    public void showMessages(ArrayList<MessageDetailsItem> arrayList){
        mRvMessageList.setHasFixedSize(true);
        mRvAdapter = new MessageListAdapter(arrayList);
        mRvMessageList.setAdapter(mRvAdapter);
    }
}
