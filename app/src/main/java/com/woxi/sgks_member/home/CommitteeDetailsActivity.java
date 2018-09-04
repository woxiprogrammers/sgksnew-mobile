package com.woxi.sgks_member.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.adapters.CommMemListAdapter;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.models.CommMemberDetailsItem;
import com.woxi.sgks_member.models.CommitteeDetailsItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

/**
 * <b>public class CommitteeDetailsActivity extends AppCompatActivity implements AppConstants </b>
 * <p>This class is used to show Committee Details</p>
 * Created by Rohit.
 */
public class CommitteeDetailsActivity extends AppCompatActivity implements AppConstants {
    private Context mContext;
    private ArrayList<CommMemberDetailsItem> arrCommMemberDetails;
    private ExpandableListView mElvCommMembers;
    private int intLastPosition = -1;
    private String TAG = "CommitteeDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.committee_details_title));
        }
        mContext = CommitteeDetailsActivity.this;

        //Calling function to initialize required views.
        initializeViews();

        //Getting extras/string from previous with the key "committeeDetail".
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("committeeID")) {
                String strCommId = bundle.getString("committeeID");
                /*String strCommName = bundle.getString("committeeName");
                if (strCommName != null) {
                    getSupportActionBar().setTitle(strCommName);
                }*/
                boolean isLocalData = bundle.getBoolean("isLocalData");
                if (isLocalData) {
                    CommitteeDetailsItem committeeListItem = (CommitteeDetailsItem) bundle.getSerializable("committeeItem");
                    //Load details
                    if (committeeListItem != null) {
                        loadCommitteeDetails(committeeListItem);
                    }
                } else {
                    //Call API only if this page is home/landing page.
                    if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                        requestCommitteeDetailsAPI(strCommId);
                    } else {
                        new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.noInternet));
                    }
                }
            }
        }
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mElvCommMembers =findViewById(R.id.elvCommitteeMembers);
        mElvCommMembers.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (intLastPosition != -1
                        && groupPosition != intLastPosition) {
                    mElvCommMembers.collapseGroup(intLastPosition);
                }
                intLastPosition = groupPosition;
            }
        });
    }

    private void requestCommitteeDetailsAPI(String strCommId) {
        String url="http://www.mocky.io/v2/5b88e95630000047033381bc";
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, /*AppURLs.API_COMMITTEE_DETAILS + currentCity + AppURLs.API_COMMITTEE_ID + strCommId*/url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseCommitteeListResponse(response.toString());
                            if (resp instanceof ArrayList) {
                                if (((ArrayList) resp).size() != 0) {
                                    ArrayList<CommitteeDetailsItem> arrMainCommList = (ArrayList<CommitteeDetailsItem>) resp;
                                    if (!arrMainCommList.isEmpty()) {
                                        CommitteeDetailsItem committeeListItem = arrMainCommList.get(0);
                                        //Load details
                                        if (committeeListItem != null) {
                                            loadCommitteeDetails(committeeListItem);
                                        }
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
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_REQUEST_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "committeeDetails");
    }

    private void loadCommitteeDetails(CommitteeDetailsItem committeeListItem) {
        String strCommAllMembers = committeeListItem.getCommAllMembers();
        if (strCommAllMembers != null && !strCommAllMembers.equalsIgnoreCase("")) {
            try {
                arrCommMemberDetails = getAllCommMembers(strCommAllMembers);
                new AppCommonMethods(mContext).LOG(0, TAG + TAG, arrCommMemberDetails.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            arrCommMemberDetails = new ArrayList<>();
        }

        ((TextView) findViewById(R.id.tvCommitteeName)).setText(committeeListItem.getCommitteeName() + "");

        if (committeeListItem.getCommitteeDescription().equalsIgnoreCase("")) {
            findViewById(R.id.tvCommitteeDescription).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.tvCommitteeDescription)).setText(committeeListItem.getCommitteeDescription() + "");
        }
        CommMemListAdapter mCommMembersAdapter = new CommMemListAdapter(mContext, arrCommMemberDetails);
        mElvCommMembers.setAdapter(mCommMembersAdapter);
    }

    private ArrayList<CommMemberDetailsItem> getAllCommMembers(String strCommAllMembers) throws JSONException {
        JSONArray jsonMemberArray = new JSONArray(strCommAllMembers);
        ArrayList<CommMemberDetailsItem> arrCommMemberDetails = new ArrayList<>();
        CommMemberDetailsItem commMemberDetailItem;
        for (int arrIndexMem = 0; arrIndexMem < jsonMemberArray.length(); arrIndexMem++) {
            commMemberDetailItem = new CommMemberDetailsItem();
            JSONObject jsonObject = jsonMemberArray.optJSONObject(arrIndexMem);

            if (jsonObject.has("fullname") && jsonObject.getString("fullname") != null) {
                commMemberDetailItem.setCommitteeMemName(jsonObject.getString("fullname"));
            }
            if (jsonObject.has("designation") && jsonObject.getString("designation") != null) {
                commMemberDetailItem.setCommitteeMemDesignation(jsonObject.getString("designation"));
            }
            if (jsonObject.has("member_image") && jsonObject.getString("member_image") != null) {
                commMemberDetailItem.setCommitteeMemImageUrl(jsonObject.getString("member_image"));
            }
            if (jsonObject.has("area") && jsonObject.getString("area") != null) {
                commMemberDetailItem.setCommitteeMemAddress(jsonObject.getString("area"));
            }
            if (jsonObject.has("cont_number") && jsonObject.getString("cont_number") != null) {
                commMemberDetailItem.setCommitteeMemContact(jsonObject.getString("cont_number"));
            }
            if (jsonObject.has("email_id") && jsonObject.getString("email_id") != null) {
                commMemberDetailItem.setCommitteeMemEmail(jsonObject.getString("email_id"));
            }
            arrCommMemberDetails.add(commMemberDetailItem);
        }
        return arrCommMemberDetails;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
