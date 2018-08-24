package com.woxi.sgks_member.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.adapters.CommitteeListsAdapter;
import com.woxi.interfaces.AppConstants;
import com.woxi.interfaces.FragmentInterface;
import com.woxi.local_storage.DatabaseQueryHandler;
import com.woxi.models.CommitteeDetailsItem;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.CommitteeDetailsActivity;
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
 * <b><b>public class CommitteeHomeFragment extends Fragment implements AppConstants, FragmentInterface</b></b>
 * <p>This class is used for App-home Committee-listing</p>
 * Created by Rohit.
 */
public class CommitteeHomeFragment extends Fragment implements AppConstants, FragmentInterface {
    public static View.OnClickListener onCommitteeClickListener;
    private Context mContext;
    private String TAG = "CommitteeHomeFragment";
    private View mParentView;
    private boolean isApiRequested = false;
    public DatabaseQueryHandler databaseQueryHandler;
    private ArrayList<CommitteeDetailsItem> arrMainCommList;
    private RecyclerView mRvCommitteeHome;

    public CommitteeHomeFragment() {
    }

    public static CommitteeHomeFragment newInstance() {
        return new CommitteeHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_committee_home, container, false);
        //Calling function to initialize required views.
        initializeViews();
        return mParentView;
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = getActivity();
        mRvCommitteeHome = (RecyclerView) mParentView.findViewById(R.id.rvCommitteeList);

        //Call API only if this page is home/landing page.
        /*if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            requestCommitteeListAPI();
        } else {
            new AppCommonMethods(mContext).showAlert(mContext
                    .getString(R.string.noInternet));
        }*/
    }

    private void requestCommitteeListAPI() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_COMMITTEE_LAZY_LOADING_LIST + currentCity, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseCommitteeListResponse(response.toString());
                            if (resp instanceof ArrayList) {
                                if (((ArrayList) resp).size() != 0) {
                                    arrMainCommList = (ArrayList<CommitteeDetailsItem>) resp;
                                    if (!arrMainCommList.isEmpty()) {
                                        setCommitteeAdapter(arrMainCommList, false);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    new AppCommonMethods().LOG(0, TAG, "response code " + error.networkResponse.statusCode + " message= " + new String(error.networkResponse.data));
                    try {
                        if (response.statusCode == STATUS_SOMETHING_WENT_WRONG) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else if (response.statusCode == STATUS_NO_RESULTS_FOUND) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                            //TODO: Handle the UI if no results found
//                            View viewNoRecordScreen = LayoutInflater.from(mContext).inflate(R.layout.no_record_found_screen, null);
                        } else {
                            new AppCommonMethods(mContext).showAlert((getString(R.string.optional_api_error)));
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "committeeListing");
    }

    private void setCommitteeAdapter(final ArrayList<CommitteeDetailsItem> arrMainCommList, final boolean isLocalData) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRvCommitteeHome.setLayoutManager(linearLayoutManager);
        final CommitteeListsAdapter committeeListAdapter = new CommitteeListsAdapter(arrMainCommList);
        mRvCommitteeHome.setAdapter(committeeListAdapter);

        onCommitteeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View selectedView) {
                CommitteeDetailsItem committeeListItem = arrMainCommList.get(mRvCommitteeHome.getChildAdapterPosition(selectedView));
                Intent intentDetails = new Intent(mContext, CommitteeDetailsActivity.class);
                intentDetails.putExtra("committeeID", committeeListItem.getCommitteeID());
                intentDetails.putExtra("committeeName", committeeListItem.getCommitteeName());
                intentDetails.putExtra("isLocalData", isLocalData);
                intentDetails.putExtra("committeeItem", committeeListItem);
                startActivity(intentDetails);
            }
        };
    }

    @Override
    public void fragmentBecameVisible() {
        if (!isApiRequested) {
            boolean isOfflineSupportEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, mContext);
            if (isOfflineSupportEnabled) {
                databaseQueryHandler = new DatabaseQueryHandler(mContext, false);
                String strCurrentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
                arrMainCommList = databaseQueryHandler.queryCommittees("", strCurrentCity);
                if (arrMainCommList == null || arrMainCommList.size() == 0) {
                    getAllCommitteesOnline();
                } else {
                    setCommitteeAdapter(arrMainCommList, true);
                    isApiRequested = true;
                }
            } else {
                getAllCommitteesOnline();
            }
        }
    }

    private void getAllCommitteesOnline() {
        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            requestCommitteeListAPI();
            isApiRequested = true;
        } else {
            new AppCommonMethods(mContext).showAlert(mContext
                    .getString(R.string.noInternet));
        }
    }
}