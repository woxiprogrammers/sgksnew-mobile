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
import com.woxi.sgkks_member.adapters.ClassifiedListAdapter;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.EndlessRvScrollListener;
import com.woxi.sgkks_member.interfaces.FragmentInterface;
import com.woxi.sgkks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgkks_member.models.ClassifiedDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppSettings;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Created By Sharvari
 */
public class ClassifiedHomeNewFragment extends Fragment implements FragmentInterface {
    private View mParentView;
    private Context mContext;
    private boolean isApiRequested = false;
    private RecyclerView mRvClassifiedList;
    private RelativeLayout mPbLazyLoad;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter mRvAdapter;
    public static View.OnClickListener onRvItemClickListener;
    public static ArrayList<ClassifiedDetailsItem> mArrClassifiedDetails;
    //public static ArrayList<ClassifiedDetailsItem> mArrClassifiedOfflineData;
    DatabaseQueryHandler databaseQueryHandler;
    private String TAG = "ClassifiedHomeFragment";
    private int pageNumber = 0, arrSize =0;
    private boolean isApiInProgress = false;
    private ProgressBar pbMessages;
    public ClassifiedHomeNewFragment() {
        // Required empty public constructor
    }

    public static ClassifiedHomeNewFragment newInstance() {
        return new ClassifiedHomeNewFragment();
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
        mRvClassifiedList =  mParentView.findViewById(R.id.rvNewsAndClassified);
        mPbLazyLoad =  mParentView.findViewById(R.id.rlLazyLoad);
        mPbLazyLoad.setVisibility(View.GONE);
        pbMessages = mParentView.findViewById(R.id.pbMessages);
        setUpRecyclerView();
        boolean isLanguageChanged = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_LANGUAGE_CHANGED,mContext);
        boolean isCityChanged = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_CITY_CHANGED,mContext);
        databaseQueryHandler = new DatabaseQueryHandler(mContext,false);
        if(isLanguageChanged || isCityChanged){
            if(new AppCommonMethods(mContext).isNetworkAvailable()){
                pageNumber=0;
                requestToGetClassifiedList(pageNumber, true);
            } else {
                mArrClassifiedDetails = databaseQueryHandler.queryClassified();
                setupClassifiedData(mArrClassifiedDetails);
            }
        }
        if (!new AppCommonMethods(mContext).isNetworkAvailable()){
            mArrClassifiedDetails = databaseQueryHandler.queryClassified();
            setupClassifiedData(mArrClassifiedDetails);
        }
    }

    private void setUpRecyclerView() {
        if (new AppCommonMethods(mContext).isNetworkAvailable()){
            mRvClassifiedList.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(mContext);
            mRvClassifiedList.setLayoutManager(linearLayoutManager);
            mRvClassifiedList.setAdapter(mRvAdapter);
            onRvItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassifiedDetailsItem classifiedDetailsItem = mArrClassifiedDetails.get(mRvClassifiedList.getChildAdapterPosition(v));
                    Intent intentDetails = new Intent(mContext, EventAndClassifiedDetailActivity.class);
                    intentDetails.putExtra("currentClassifiedDetail", classifiedDetailsItem);
                    startActivity(intentDetails);
                }
            };
        } else {
            mRvClassifiedList.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(mContext);
            mRvClassifiedList.setLayoutManager(linearLayoutManager);
            mRvClassifiedList.setAdapter(mRvAdapter);
            onRvItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClassifiedDetailsItem classifiedDetailsItem = mArrClassifiedDetails.get(mRvClassifiedList.getChildAdapterPosition(v));
                    Intent intentDetails = new Intent(mContext, EventAndClassifiedDetailActivity.class);
                    intentDetails.putExtra("currentClassifiedDetail", classifiedDetailsItem);
                    startActivity(intentDetails);
                }
            };
        }

        recyclerViewScrollListener();
    }

    private void requestToGetClassifiedList(final int page_id, final boolean isFirstTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = simpleDateFormat.format(new Date());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.PREFS_LAST_CLASSIFIED_DATE,format);
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
        //ToDO PageID
        JSONObject params = new JSONObject();
        try {
            params.put("page_id",page_id);
            params.put("language_id", AppSettings.getStringPref(AppConstants.PREFS_LANGUAGE_APPLIED,mContext));
            params.put("sgks_city", AppSettings.getStringPref(AppConstants.PREFS_CURRENT_CITY,mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,AppURLs.API_CLASSIFIED_LISTING, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            new AppCommonMethods(mContext).LOG(0,"classified_response",response.toString());
                            if (!response.getString("page_id").equalsIgnoreCase("")) {
                                pageNumber = Integer.parseInt(response.getString("page_id"));
                            }
                            Object resp= AppParser.parseClassifiedResponse(response.toString());
                            ClassifiedDetailsItem classifiedDetailsItem = (ClassifiedDetailsItem) resp;
                            if(resp instanceof Boolean){
                              //  Toast.makeText(mContext,"Failed",Toast.LENGTH_SHORT).show();
                            }else if(resp instanceof ClassifiedDetailsItem){
                                if(isFirstTime){
                                    mArrClassifiedDetails = classifiedDetailsItem.getArrClassifiedList();
                                    if(mArrClassifiedDetails != null){
                                        mRvClassifiedList.setHasFixedSize(true);
                                        mRvAdapter = new ClassifiedListAdapter(mArrClassifiedDetails);
                                        mRvClassifiedList.setAdapter(mRvAdapter);
                                    } else {
                                      //  Toast.makeText(mContext,"No Records Found",Toast.LENGTH_SHORT);
                                    }
                                } else {
                                    ArrayList<ClassifiedDetailsItem> arrNextClassified = classifiedDetailsItem.getArrClassifiedList();
                                    if(arrNextClassified.size() != 0){
                                        mArrClassifiedDetails.addAll(arrNextClassified);
                                        mRvClassifiedList.getAdapter().notifyItemRangeChanged(arrSize - 1, mArrClassifiedDetails.size() - 1);
                                        mRvClassifiedList.getAdapter().notifyDataSetChanged();
                                    } else {
                                     //   Toast.makeText(mContext,"That's all",Toast.LENGTH_SHORT);
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
        mRvClassifiedList.addOnScrollListener(new EndlessRvScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requestLazyLoadMembersApi();
            }
        });
    }

    private void requestLazyLoadMembersApi() {
        if (!isApiInProgress) {
            //Cancelling Pending Request
            AppController.getInstance().cancelPendingRequests(TAG);
            if (new AppCommonMethods(mContext).isNetworkAvailable()){
                requestToGetClassifiedList(pageNumber,false);
            } else {
                //new AppCommonMethods(mContext).showAlert("You are offline");
            }
        }
    }

    @Override
    public void fragmentBecameVisible() {
        if(!isApiRequested){
            if(new AppCommonMethods(mContext).isNetworkAvailable()){
                pageNumber=0;
                requestToGetClassifiedList(pageNumber, true);
            } else {
                mArrClassifiedDetails = databaseQueryHandler.queryClassified();
                setupClassifiedData(mArrClassifiedDetails);
            }
        }
    }

    public void setupClassifiedData (ArrayList<ClassifiedDetailsItem> arrayList ){
        mRvClassifiedList.setHasFixedSize(true);
        mRvAdapter = new ClassifiedListAdapter(arrayList);
        mRvClassifiedList.setAdapter(mRvAdapter);
    }
}
