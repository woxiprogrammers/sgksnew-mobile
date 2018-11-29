package com.woxi.sgkks_member.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.woxi.sgkks_member.adapters.MemberListAdapter;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.EndlessRvScrollListener;
import com.woxi.sgkks_member.interfaces.FragmentInterface;
import com.woxi.sgkks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgkks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.models.MemberSearchDataItem;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppSettings;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberHomeNewFragment extends Fragment implements FragmentInterface, AppConstants {
    public static View.OnClickListener onMemberClickListener;
    private Context mContext;
    private RecyclerView mRvMemberList;
    private RecyclerView.Adapter mRvAdapter;
    private View mParentView;
    private EditText mEtMemberSearch;
    private RelativeLayout mPbLazyLoad;
    private String TAG = "MemberHomeFragment";
    public static ArrayList<MemberDetailsItem> mArrMemDetails;
    //    private boolean isApiRequested = false;
    private String strSearchTerm = "", strNextPageUrl = "";
    private int arrSize = 0;
    private LinearLayoutManager linearLayoutManager;
    private boolean isApiInProgress = false;
    private DatabaseQueryHandler databaseQueryHandler;
    private String strNoResults = "";
    private int pageNumber = 0;
    private ProgressBar pbMemberListing;
    private String searchFullName = "";
    public MemberHomeNewFragment() {
        // Required empty public constructor
    }

    public static MemberHomeNewFragment newInstance() {
        return new MemberHomeNewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_members_home, container, false);
        //Calling function to initialize required views.
        initializeViews();
        return mParentView;
    }

    private void initializeViews() {
        mContext = getActivity();
        mRvMemberList = mParentView.findViewById(R.id.rvMemberList);
        mPbLazyLoad = mParentView.findViewById(R.id.rlLazyLoad);
        mEtMemberSearch = mParentView.findViewById(R.id.etSearchMember);
        strSearchTerm = mEtMemberSearch.getText().toString();
        new AppCommonMethods(mContext).hideKeyBoard(mEtMemberSearch);
        mPbLazyLoad.setVisibility(View.GONE);
        databaseQueryHandler = new DatabaseQueryHandler(mContext, false);
        pbMemberListing = mParentView.findViewById(R.id.pbMemberListing);
        mEtMemberSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 3) {
                    searchFullName = charSequence.toString().toLowerCase();
                    if (new AppCommonMethods(mContext).isNetworkAvailable()){
                        requestToGetMembersData(0,true);
                    } else {
                        new AppCommonMethods(mContext).showAlert("You are offline");
                    }
                } else if (charSequence.length()==0){
                    searchFullName = "";
                    if (new AppCommonMethods(mContext).isNetworkAvailable()){
                        requestToGetMembersData(0,true);
                    } else {
                        new AppCommonMethods(mContext).showAlert("You are offline");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        setUpRecyclerView();
        if (new AppCommonMethods(mContext).isNetworkAvailable()){
            requestToGetMembersData(pageNumber,true);
        } else {
            new AppCommonMethods(mContext).showAlert("You are offline");
        }
    }

    private void setUpRecyclerView() {
        mRvMemberList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvMemberList.setLayoutManager(linearLayoutManager);

        onMemberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDetailsItem memberDetailsItem = mArrMemDetails.get(mRvMemberList.getChildAdapterPosition(v));
                Intent intentDetails = new Intent(mContext, MemberDetailsActivity.class);
                intentDetails.putExtra("currentMemberDetail", memberDetailsItem);
                startActivity(intentDetails);
            }
        };
        //Following method call is for listening to scroll events
        recyclerViewScrollListener();
    }

    @Override
    public void fragmentBecameVisible() {
        mParentView.findViewById(R.id.etSearchMember).clearFocus();
    }

    private void requestToGetMembersData(final int pageId, final boolean isFirstTime) {
        isApiInProgress = true;
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        if(isFirstTime){
            pDialog.setMessage("Loading, Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        } else {
            pbMemberListing.setVisibility(View.VISIBLE);
        }
        JSONObject params = new JSONObject();
        try {
            params.put("page_id", pageId);
            params.put("sgks_city",AppCommonMethods.getStringPref(AppConstants.PREFS_CURRENT_CITY,mContext));
            params.put("language_id",AppSettings.getStringPref(PREFS_LANGUAGE_APPLIED, mContext));
            params.put("search_fullname",searchFullName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppURLs.API_MEMBERS_LISTING, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            new AppCommonMethods(mContext).LOG(0,"member_listing_success",response.toString());
                            if (!response.getString("page_id").equalsIgnoreCase("")) {
                                pageNumber = Integer.parseInt(response.getString("page_id"));
                            }
                            Object resp = AppParser.parseMemberList(response.toString());
                      //      mArrMemDetails = (ArrayList<MemberDetailsItem>) resp;
                            MemberDetailsItem memberDetailsItem = (MemberDetailsItem) resp;
                            if (resp instanceof Boolean) {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            } else if (resp instanceof MemberDetailsItem) {
                                //if (mArrMemDetails.size() > 0) {
                                    if(isFirstTime){
                                        mArrMemDetails = memberDetailsItem.getArrMemberList();
                                        mRvMemberList.setHasFixedSize(true);
                                        mRvAdapter = new MemberListAdapter(mArrMemDetails);
                                        mRvMemberList.setAdapter(mRvAdapter);
                                        databaseQueryHandler.insertOrUpdateAllMembers(mArrMemDetails);
                                    } else {
                                        ArrayList<MemberDetailsItem> arrNextMembers = memberDetailsItem.getArrMemberList();
                                        mArrMemDetails.addAll(arrNextMembers);
                                        mRvMemberList.getAdapter().notifyItemRangeChanged(arrSize - 1, mArrMemDetails.size() - 1);
                                        mRvMemberList.getAdapter().notifyDataSetChanged();
                                    }
                                }
                            //}
                            //setUpMemberListAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                        pbMemberListing.setVisibility(View.GONE);
                        isApiInProgress = false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isApiInProgress = false;
                pbMemberListing.setVisibility(View.GONE);
                new AppCommonMethods(mContext).LOG(0,"error_member_listing",error.toString());
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(req, "member_listing");
    }

    private void setUpMemberListAdapter () {
//        mRvMemberList.setHasFixedSize(true);
//        mRvAdapter = new MemberListAdapter(mArrMemDetails);
//        mRvMemberList.setAdapter(mRvAdapter);
        recyclerViewScrollListener();
    }

    private void recyclerViewScrollListener() {
        mRvMemberList.addOnScrollListener(new EndlessRvScrollListener(linearLayoutManager) {
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
                requestToGetMembersData(pageNumber,false);
            } else {
                new AppCommonMethods(mContext).showAlert("You are offline");
            }
        }
    }
}
