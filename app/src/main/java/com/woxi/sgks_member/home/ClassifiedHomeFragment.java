package com.woxi.sgks_member.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.interfaces.FragmentInterface;
import com.woxi.sgks_member.models.ClassifiedDetailsItem;
import com.woxi.sgks_member.models.MessageAndClassifiedResponseItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>public class ClassifiedHomeFragment extends Fragment implements AppConstants, FragmentInterface</b>
 * <p>This class is used as a Classified Home </p>
 * Created by Rohit.
 */

public class ClassifiedHomeFragment extends Fragment implements AppConstants, FragmentInterface {
    private View mParentView;
    private Context mContext;
    private boolean isClassifiedApiRequested = false;
    private boolean isClassifiedApiInProgress = false;
    private RecyclerView mRvClassifiedList;
    private RelativeLayout mPbLazyLoad;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter mRvAdapter;
    public static View.OnClickListener onRvItemClickListener;
    private ArrayList<ClassifiedDetailsItem> mArrClassifiedDetails;
    private String TAG = "ClassifiedHomeFragment";
    private String messageNextPageUrl = "";
    private int intClassifiedArraySize = 0;

    public ClassifiedHomeFragment() {
        // Required empty public constructor
    }

    public static ClassifiedHomeFragment newInstance() {
        return new ClassifiedHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_messages_classified_home, container, false);
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
        mRvClassifiedList = (RecyclerView) mParentView.findViewById(R.id.rvNewsAndClassified);
        mPbLazyLoad = ((RelativeLayout) mParentView.findViewById(R.id.rlLazyLoad));
        mPbLazyLoad.setVisibility(View.GONE);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRvClassifiedList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(mContext);
        mRvClassifiedList.setLayoutManager(linearLayoutManager);

        onRvItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ClassifiedDetailsItem classifiedDetailsItem = mArrClassifiedDetails.get(mRvClassifiedList.getChildAdapterPosition(v));
                Intent intentDetails = new Intent(mContext, EventAndClassifiedDetailActivity.class);
                intentDetails.putExtra("currentClassifiedDetail", classifiedDetailsItem);
                startActivity(intentDetails);*/
            }
        };
        //Following method call is for listening to scroll events
        recyclerViewScrollListener();
    }

    @Override
    public void fragmentBecameVisible() {
        if (!isClassifiedApiRequested) {
            if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                String strMemberLoadUrl = AppURLs.API_Classified_LAZY_LOADING_LIST;
                JSONObject params = getParamsToPass();
                //Cancelling Pending Request
                AppController.getInstance().cancelPendingRequests("apiClassifiedListTAg");
                requestClassifiedListAPI(true, params, strMemberLoadUrl, true);
                isClassifiedApiRequested = true;
            } else {
                new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.noInternet));
            }
        }
    }

    /**
     * <b>private void recyclerViewScrollListener()</b>
     * <p>Following function is necessary to listen to RV scroll events</p>
     * <p>Do not forget to include this function while requesting API</p>
     * Created By Rohit
     */
    private void recyclerViewScrollListener() {
        mRvClassifiedList.addOnScrollListener(new EndlessRvScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isClassifiedApiInProgress) {
                    JSONObject params = getParamsToPass();
                    //Cancelling Pending Request
                    AppController.getInstance().cancelPendingRequests("apiClassifiedListTAg");
                    requestClassifiedListAPI(false, params, messageNextPageUrl, false);
                }
            }
        });
    }

    private void requestClassifiedListAPI(boolean isProgressDialog, JSONObject params, String strMemberLoadUrl, final boolean isFirstTime) {
        isClassifiedApiInProgress = true;
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
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        //{"pagination":null,"data":null,"stats":null,"message":"No records found."}
                        try {
                            Object resp = AppParser.parseClassifiedResponse(response.toString());
                            new AppCommonMethods().LOG(0, TAG, resp.toString());

                            if (response.has("pagination") && response.optJSONObject("pagination") != null) {
                                JSONObject jsonPaginationObject = response.optJSONObject("pagination");
                                if (jsonPaginationObject.has("next_page_url") && jsonPaginationObject.optString("next_page_url") != null) {
                                    messageNextPageUrl = jsonPaginationObject.optString("next_page_url");
                                    new AppCommonMethods().LOG(0, TAG, messageNextPageUrl);
                                } else {
                                    showNoRecordMessage();
                                }
                            } else {
                                showNoRecordMessage();
                            }

                            if (resp instanceof MessageAndClassifiedResponseItem) {
                                MessageAndClassifiedResponseItem jsonResultObject = (MessageAndClassifiedResponseItem) resp;
                                if (isFirstTime) {
                                    mArrClassifiedDetails = jsonResultObject.getArrClassifiedList();
                                    intClassifiedArraySize = mArrClassifiedDetails.size();
                                    mRvAdapter = new ClassifiedListAdapter(mArrClassifiedDetails);
                                    mRvClassifiedList.setAdapter(mRvAdapter);
                                    mRvClassifiedList.getAdapter().notifyDataSetChanged();
                                } else {
                                    //API requested on next load
                                    ArrayList<ClassifiedDetailsItem> mArrNextClassifiedDetails = jsonResultObject.getArrClassifiedList();
                                    if (mArrNextClassifiedDetails != null) {
                                        mArrClassifiedDetails.addAll(mArrNextClassifiedDetails);
                                        mRvClassifiedList.getAdapter().notifyItemRangeChanged(intClassifiedArraySize - 1, mArrClassifiedDetails.size() - 1);
                                        mRvClassifiedList.getAdapter().notifyDataSetChanged();
                                    } else {
                                        showNoRecordMessage();
                                    }
                                }
                            } else {
                                showNoRecordMessage();
                            }
                        } catch (
                                JSONException e
                                ) {
                            e.printStackTrace();
                        }
                        isClassifiedApiInProgress = false;
                        mPbLazyLoad.setVisibility(View.GONE);
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                mPbLazyLoad.setVisibility(View.GONE);
                isClassifiedApiInProgress = false;

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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "apiClassifiedListTAg");
    }

    /**
     * <b>private void showNoRecordMessage()</b>
     * <p>This function is used to show no record found message</p>
     * Created By Rohit
     */
    private void showNoRecordMessage() {
        Snackbar.make(mParentView.findViewById(R.id.llNewsAndClassifiedHome), "No more items to show", Snackbar.LENGTH_LONG).show();
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
        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
        try {
            params.put("sgks_city", currentCity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}

