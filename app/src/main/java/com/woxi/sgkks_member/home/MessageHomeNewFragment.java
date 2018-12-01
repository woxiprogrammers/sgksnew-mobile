package com.woxi.sgkks_member.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;

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
    private boolean isApiInProgress = false;
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
        setUpRecyclerView();
        pbMessages = mParentView.findViewById(R.id.pbMessages);
        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            requestMessageList(pageNumber,true);
        }else {
            new AppCommonMethods(mContext).showAlert("You are Offline");
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
                                        mRvMessageList.setHasFixedSize(true);
                                        mRvAdapter = new MessageListAdapter(mArrMessageDetails);
                                        mRvMessageList.setAdapter(mRvAdapter);
                                    } else {
                                        Toast.makeText(mContext,"No Records Found",Toast.LENGTH_SHORT);
                                    }
                                    mRvMessageList.setHasFixedSize(true);
                                    mRvAdapter = new MessageListAdapter(mArrMessageDetails);
                                    mRvMessageList.setAdapter(mRvAdapter);
                                } else {
                                    ArrayList <MessageDetailsItem> arrNextMessages = messageDetailsItem.getArrMessageList();
                                    if(arrNextMessages.size() != 0){
                                        mArrMessageDetails.addAll(arrNextMessages);
                                        mRvMessageList.getAdapter().notifyItemRangeChanged(arrSize -1, mArrMessageDetails.size() - 1);
                                        mRvMessageList.getAdapter().notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(mContext,"All the Records are Listed",Toast.LENGTH_SHORT);
                                    }
                                }
                            }
                            pDialog.dismiss();
                            pbMessages.setVisibility(View.GONE);
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
            } else {
                new AppCommonMethods(mContext).showAlert("You are offline");
            }
        }
    }

    @Override
    public void fragmentBecameVisible() {
    }
}
