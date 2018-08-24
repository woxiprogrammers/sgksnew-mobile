package com.woxi.sgks_member.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.adapters.MemberListAdapter;
import com.woxi.interfaces.AppConstants;
import com.woxi.interfaces.EndlessRvScrollListener;
import com.woxi.interfaces.FragmentInterface;
import com.woxi.local_storage.DatabaseQueryHandler;
import com.woxi.models.MemberDetailsItem;
import com.woxi.models.MemberSearchDataItem;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.MemberDetailsActivity;
import com.woxi.sgks_member.R;
import com.woxi.utils.AppCommonMethods;
import com.woxi.utils.AppParser;
import com.woxi.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <b><b>public class MemberHomeFragment extends Fragment implements View.OnClickListener, AppConstants, FragmentInterface</b></b>
 * <p>This class is used for App-home Member-listing</p>
 * Created by Rohit.
 */
public class MemberHomeFragment extends Fragment implements AppConstants, FragmentInterface {
    public static View.OnClickListener onMemberClickListener;
    private Context mContext;
    private RecyclerView mRvMemberList;
    private RecyclerView.Adapter mRvAdapter;
    private View mParentView;
    private EditText mEtMemberSearch;
    private RelativeLayout mPbLazyLoad;
    private String TAG = "MemberHomeFragment";
    private ArrayList<MemberDetailsItem> mArrMemDetails;
    //    private boolean isApiRequested = false;
    private String strSearchTerm = "", strNextPageUrl = "";
    private int arrSize = 0;
    private LinearLayoutManager linearLayoutManager;
    private boolean isApiInProgress = false;
    private DatabaseQueryHandler databaseQueryHandler;
    private String strNoResults = "";

    public MemberHomeFragment() {
    }

    public static MemberHomeFragment newInstance() {
        return new MemberHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_members_home, container, false);
        //Calling function to initialize required views.
        initializeViews();
        mParentView.findViewById(R.id.ivMemberHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) mParentView.findViewById(R.id.etSearchMember)).setText("");
                strNoResults = "";
                functionToGetMembersList(false);
            }
        });
        mParentView.findViewById(R.id.ivSearchMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call Function to Load All Members
                functionToGetMembersList(true);
            }
        });
        return mParentView;
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = getActivity();
        mRvMemberList = (RecyclerView) mParentView.findViewById(R.id.rvMemberList);
        mPbLazyLoad = ((RelativeLayout) mParentView.findViewById(R.id.rlLazyLoad));
        mEtMemberSearch = (EditText) mParentView.findViewById(R.id.etSearchMember);
        new AppCommonMethods(mContext).hideKeyBoard(mEtMemberSearch);
        mPbLazyLoad.setVisibility(View.GONE);
        databaseQueryHandler = new DatabaseQueryHandler(mContext, false);

        setUpRecyclerView();
        functionToGetMembersList(false);

        //Setup Member Auto Search
        mEtMemberSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 2) {
                    String currentSearch = charSequence.toString().toLowerCase();
                    if (strNoResults.equalsIgnoreCase("") || !strNoResults.matches("^" + currentSearch + "*")) {
                        strNoResults = "";
                        functionToGetMembersList(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * private void functionToGetMembersList(boolean isFromAutoSearch)
     *
     * @param isFromAutoSearch boolean to decide whether to get all member or search results only
     *                         Created By Rohit.
     */
    private void functionToGetMembersList(boolean isFromAutoSearch) {
        boolean isOfflineSupportEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, mContext);
        if (isOfflineSupportEnabled) {
            if (isFromAutoSearch) {
                strSearchTerm = mEtMemberSearch.getText().toString().trim();
            } else {
                strSearchTerm = "";
            }
            if (!(strSearchTerm.matches(REGEX_MEMBER_SEARCH_ALLOWED) || strSearchTerm.equalsIgnoreCase(""))) {
                Toast.makeText(mContext, mContext.getString(R.string.toast_allowed_characters_member_search), Toast.LENGTH_SHORT).show();
                return;
            }
            String strCurrentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
            mArrMemDetails = databaseQueryHandler.queryMembers(strSearchTerm, strCurrentCity);
            //If there are no results from local database go online else show local results.
            if (mArrMemDetails == null || mArrMemDetails.size() == 0) {
                mRvAdapter = new MemberListAdapter(new ArrayList<MemberDetailsItem>());
                mRvMemberList.setAdapter(mRvAdapter);
                mRvMemberList.getAdapter().notifyDataSetChanged();
                searchMemberOnline();
            } else {
                new AppCommonMethods().LOG(0, TAG + " Local Search", mArrMemDetails.toString());
                mRvAdapter = new MemberListAdapter(mArrMemDetails);
                mRvMemberList.setAdapter(mRvAdapter);
                mRvMemberList.getAdapter().notifyDataSetChanged();
            }
        } else {
            searchMemberOnline();
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

    /**
     * <b>private void recyclerViewScrollListener()</b>
     * <p>Following function is necessary to listen to RV scroll events</p>
     * <p>Do not forget to include this function while requesting API</p>
     * Created By Rohit
     */
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
            JSONObject params = getParamsToPass();
            //Cancelling Pending Request
            AppController.getInstance().cancelPendingRequests("memberSearchList");
            requestSearchMembersAPI(false, params, strNextPageUrl, false);
        }
    }

    /**
     * <b>private JSONObject getParamsToPass()</b>
     * <p>This function is used to get all parameters to pass to server.</p>
     *
     * @return "params" - Json object
     * Created By Rohit
     */
    private JSONObject getParamsToPass() {
        JSONObject params = new JSONObject();
        try {
            String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
            params.put("sgks_city", currentCity);
            if (!strSearchTerm.equalsIgnoreCase("")) {
                params.put("mastertext", strSearchTerm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    private void searchMemberOnline() {
        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            //        sgks_city => 'required'
            //        sgks_city => 'String',
            //        mastertext => ‘String’,
            //        suggestion_message => ‘String’
            //        education => array()
            String strMemberLoadUrl = AppURLs.API_MEMBER_LAZY_LOADING_SEARCH;
            JSONObject params = getParamsToPass();
            //Cancelling Pending Request
            AppController.getInstance().cancelPendingRequests("memberSearchList");
            requestSearchMembersAPI(true, params, strMemberLoadUrl, true);
            //Following method call is for listening to scroll events
            recyclerViewScrollListener();
        } else {
            new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.noInternet));
        }
    }

    /**
     * <b>private void requestSearchMembersAPI(boolean isProgressDialog, JSONObject params, String strMemberLoadUrl, final boolean isFirstTime)</b>
     * <p>This function is used to request API from server</p>
     *
     * @param isProgressDialog "true" if first time else "false"
     * @param params           parameters to pass
     * @param strMemberLoadUrl url for api calling
     * @param isFirstTime      "true" if first time else "false"
     *                         Created By Rohit
     */
    private void requestSearchMembersAPI(boolean isProgressDialog, JSONObject params, String strMemberLoadUrl, final boolean isFirstTime) {
        isApiInProgress = true;
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        if (isProgressDialog) {
            pDialog.setMessage("Loading, Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        } else {
            mPbLazyLoad.setVisibility(View.VISIBLE);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, strMemberLoadUrl, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        strNoResults = "";
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        //{"pagination":null,"data":null,"stats":null,"message":"No records found."}
                        try {
                            Object resp = AppParser.parseMemberSearchResponse(response.toString());

                            if (response.has("pagination") && response.optJSONObject("pagination") != null) {
                                JSONObject jsonPaginationObject = response.optJSONObject("pagination");
                                if (jsonPaginationObject.has("next_page_url") && jsonPaginationObject.optString("next_page_url") != null) {
                                    strNextPageUrl = jsonPaginationObject.optString("next_page_url");
                                    new AppCommonMethods().LOG(0, TAG, strNextPageUrl);
                                } else {
                                    showNoRecordMessage();
                                }
                            } else {
                                showNoRecordMessage();
                            }

                            if (resp instanceof MemberSearchDataItem) {
                                MemberSearchDataItem jsonResultObject = (MemberSearchDataItem) resp;
                                if (isFirstTime) {
                                    mArrMemDetails = jsonResultObject.getArrMemberList();
                                    arrSize = mArrMemDetails.size();
                                    mRvAdapter = new MemberListAdapter(mArrMemDetails);
                                    mRvMemberList.setAdapter(mRvAdapter);
                                    mRvMemberList.getAdapter().notifyDataSetChanged();
                                } else {
                                    //API requested on next load
                                    ArrayList<MemberDetailsItem> mNextArrMemDetails = jsonResultObject.getArrMemberList();
                                    if (mNextArrMemDetails != null) {
                                        mArrMemDetails.addAll(mNextArrMemDetails);
                                        mRvMemberList.getAdapter().notifyItemRangeChanged(arrSize - 1, mArrMemDetails.size() - 1);
                                        mRvMemberList.getAdapter().notifyDataSetChanged();
                                    } else {
                                        showNoRecordMessage();
                                    }
//                                    Toast.makeText(mContext, "Api Requested", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                showNoRecordMessage();
                            }
                        } catch (
                                JSONException e
                                ) {
                            e.printStackTrace();
                        }
                        isApiInProgress = false;
                        mPbLazyLoad.setVisibility(View.GONE);
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                mPbLazyLoad.setVisibility(View.GONE);
                isApiInProgress = false;
//                isSearchTermChanged = false;
                strNoResults = strSearchTerm.toLowerCase();

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    new AppCommonMethods().LOG(0, TAG, "response code " + error.networkResponse.statusCode + " message= " + new String(error.networkResponse.data));
                    try {
                        if (response.statusCode == STATUS_SOMETHING_WENT_WRONG) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                        }
                        if (response.statusCode == STATUS_NO_RESULTS_FOUND) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else {
                            new AppCommonMethods(mContext).showAlert("" + (getString(R.string.optional_api_error)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json; charset=UTF-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "memberSearchList");
    }

    /**
     * <b>private void showNoRecordMessage()</b>
     * <p>This function is used to show no record found message</p>
     * Created By Rohit
     */
    private void showNoRecordMessage() {
        Snackbar.make(mParentView.findViewById(R.id.rlMemberHome), "No more members to show", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void fragmentBecameVisible() {
        mParentView.findViewById(R.id.etSearchMember).clearFocus();
        /*if (!isApiRequested) {
            if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                String strMemberLoadUrl = AppURLs.API_MEMBER_LAZY_LOADING_SEARCH;

                JSONObject params = getParamsToPass();
                //Cancelling Pending Request
                AppController.getInstance().cancelPendingRequests("memberSearchList");
                requestSearchMembersAPI(true, params, strMemberLoadUrl, true);
                isApiRequested = true;
            } else {
                new AppCommonMethods(mContext).showAlert(mContext
                        .getString(R.string.noInternet));
            }
        }*/
    }
}