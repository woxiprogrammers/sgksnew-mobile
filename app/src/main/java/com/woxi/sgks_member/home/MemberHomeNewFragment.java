package com.woxi.sgks_member.home;

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
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.adapters.MemberListAdapter;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.interfaces.EndlessRvScrollListener;
import com.woxi.sgks_member.interfaces.FragmentInterface;
import com.woxi.sgks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgks_member.models.MemberDetailsItem;
import com.woxi.sgks_member.models.MessageDetailsItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;

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
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvMemberList.setLayoutManager(linearLayoutManager);
        pbMemberListing = mParentView.findViewById(R.id.pbMemberListing);
        mEtMemberSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 2) {
                    String currentSearch = charSequence.toString().toLowerCase();
                    searchMember(currentSearch);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        recyclerViewScrollListener();
        //functionToGetMembersList();
        requestToGetMembersData(0,false);
        onMemberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberDetailsItem memberDetailsItem = mArrMemDetails.get(mRvMemberList.getChildAdapterPosition(view));
                Intent intentDetails = new Intent(mContext, MemberDetailsActivity.class);
                intentDetails.putExtra("currentMemberDetail", memberDetailsItem);
                startActivity(intentDetails);
            }
        };
    }

    private void searchMember(String currentSearch) {
        pbMemberListing.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("page_id", 0);
            params.put("sgks_city",1);
            params.put("language_id",1);
            params.put("search_fullname",currentSearch);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppURLs.API_MEMBERS_LISTING, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pbMemberListing.setVisibility(View.GONE);
                        try {
                            new AppCommonMethods(mContext).LOG(0,"member_listing_success",response.toString());
                            if (!response.getString("page_id").equalsIgnoreCase("")) {
                                pageNumber = Integer.parseInt(response.getString("page_id"));
                            }
                            Object resp = AppParser.parseMemberList(response.toString());
                            mArrMemDetails = (ArrayList<MemberDetailsItem>) resp;
                            if (resp instanceof Boolean) {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            } else if (resp instanceof ArrayList) {
                                if (mArrMemDetails.size() > 0) {
                                    mRvMemberList.setHasFixedSize(true);
                                    mRvAdapter = new MemberListAdapter(mArrMemDetails);
                                    mRvMemberList.setAdapter(mRvAdapter);
                                    databaseQueryHandler.insertOrUpdateAllMembers(mArrMemDetails);

                                } else {
                                    Toast.makeText(mContext, "No Record Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            setUpMemberListAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbMemberListing.setVisibility(View.GONE);
                new AppCommonMethods(mContext).LOG(0,"error_member_listing",error.toString());
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(req, "member_search");
    }

    private void functionToGetMembersList() {
        boolean isOfflineSupportEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, mContext);
        if (isOfflineSupportEnabled) {
            String strCurrentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
            mArrMemDetails = databaseQueryHandler.queryMembers(strSearchTerm);
            //If there are no results from local database go online else show local results.
            if (mArrMemDetails == null || mArrMemDetails.size() == 0) {
                mRvAdapter = new MemberListAdapter(new ArrayList<MemberDetailsItem>());
                mRvMemberList.setAdapter(mRvAdapter);
                mRvMemberList.getAdapter().notifyDataSetChanged();
                requestToGetMembersData(pageNumber, true);
//                searchMemberOnline();
            } else {
                new AppCommonMethods().LOG(0, TAG + " Local Search", mArrMemDetails.toString());
                mRvAdapter = new MemberListAdapter(mArrMemDetails);
                mRvMemberList.setAdapter(mRvAdapter);
            }
        } else {
            requestToGetMembersData(pageNumber, true);
        }
    }

    @Override
    public void fragmentBecameVisible() {
        mParentView.findViewById(R.id.etSearchMember).clearFocus();
    }

    private void requestToGetMembersData(final int pageId, boolean isFirstTime) {
        pbMemberListing.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("page_id", 0);
            params.put("sgks_city",1);
            params.put("language_id",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppURLs.API_MEMBERS_LISTING, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pbMemberListing.setVisibility(View.GONE);
                        try {
                            new AppCommonMethods(mContext).LOG(0,"member_listing_success",response.toString());
                            if (!response.getString("page_id").equalsIgnoreCase("")) {
                                pageNumber = Integer.parseInt(response.getString("page_id"));
                            }
                            Object resp = AppParser.parseMemberList(response.toString());
                            mArrMemDetails = (ArrayList<MemberDetailsItem>) resp;
                            if (resp instanceof Boolean) {
                                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                            } else if (resp instanceof ArrayList) {
                                if (mArrMemDetails.size() > 0) {
                                    mRvMemberList.setHasFixedSize(true);
                                    mRvAdapter = new MemberListAdapter(mArrMemDetails);
                                    mRvMemberList.setAdapter(mRvAdapter);
                                    databaseQueryHandler.insertOrUpdateAllMembers(mArrMemDetails);

                                } else {
                                    Toast.makeText(mContext, "No Record Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            setUpMemberListAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbMemberListing.setVisibility(View.GONE);
                new AppCommonMethods(mContext).LOG(0,"error_member_listing",error.toString());
                VolleyLog.e("Error: ", error.getMessage());
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(req, "member_listing");
    }

    private void setUpMemberListAdapter () {
        mRvMemberList.setHasFixedSize(true);
        mRvAdapter = new MemberListAdapter(mArrMemDetails);
        mRvMemberList.setAdapter(mRvAdapter);
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
            requestToGetMembersData(pageNumber, false);
        }
    }
}
