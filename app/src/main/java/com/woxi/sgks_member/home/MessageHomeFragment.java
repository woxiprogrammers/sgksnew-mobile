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
import com.woxi.adapters.MessageListAdapter;
import com.woxi.interfaces.AppConstants;
import com.woxi.interfaces.EndlessRvScrollListener;
import com.woxi.interfaces.FragmentInterface;
import com.woxi.local_storage.DatabaseQueryHandler;
import com.woxi.models.MessageAndClassifiedResponseItem;
import com.woxi.models.MessageDetailsItem;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.MessageDetailsActivity;
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
 * <b><b>public class MessageHomeFragment extends Fragment implements AppConstants, FragmentInterface</b></b>
 * <p>This class is used for App-home - Messages listing</p>
 * Created by Rohit.
 */
public class MessageHomeFragment extends Fragment implements AppConstants, FragmentInterface {
    private View mParentView;
    private Context mContext;
    private boolean isMessageApiRequested = false;
    private boolean isMessageApiInProgress = false;
    private RecyclerView mRvMessageList;
    private RelativeLayout mPbLazyLoad;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.Adapter mRvAdapter;
    public static View.OnClickListener onRvItemClickListener;
    private ArrayList<MessageDetailsItem> mArrMessageDetails;
    private String TAG = "MessageHomeFragment";
    private String messageNextPageUrl = "";
    private int intMessageArraySize = 0;
    public DatabaseQueryHandler databaseQueryHandler;

    public MessageHomeFragment() {
        // Required empty public constructor
    }

    public static MessageHomeFragment newInstance() {
        return new MessageHomeFragment();
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
        mRvMessageList = (RecyclerView) mParentView.findViewById(R.id.rvNewsAndClassified);
        mPbLazyLoad = ((RelativeLayout) mParentView.findViewById(R.id.rlLazyLoad));
        mPbLazyLoad.setVisibility(View.GONE);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
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
        //Following method call is for listening to scroll events
        recyclerViewScrollListener();
    }

    @Override
    public void fragmentBecameVisible() {
        if (!isMessageApiRequested) {
            boolean isOfflineSupportEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, mContext);
            if (isOfflineSupportEnabled) {
                databaseQueryHandler = new DatabaseQueryHandler(mContext, false);
                String strCurrentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
                mArrMessageDetails = databaseQueryHandler.queryMessages("", strCurrentCity);
                if (mArrMessageDetails == null || mArrMessageDetails.size() == 0) {
                    getAllNewsMessagesOnline();
                } else {
                    mRvAdapter = new MessageListAdapter(mArrMessageDetails);
                    mRvMessageList.setAdapter(mRvAdapter);
                    mRvMessageList.getAdapter().notifyDataSetChanged();
                    isMessageApiRequested = true;
                }
            } else {
                getAllNewsMessagesOnline();
            }
        }
    }

    private void getAllNewsMessagesOnline() {
        if (new AppCommonMethods(mContext).isNetworkAvailable()) {
            String strMemberLoadUrl = AppURLs.API_MESSAGE_LAZY_LOADING_LIST;
            JSONObject params = getParamsToPass();
            //Cancelling Pending Request
            AppController.getInstance().cancelPendingRequests("newsList");
            requestNewsListAPI(true, params, strMemberLoadUrl, true);
            isMessageApiRequested = true;
        } else {
            new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.noInternet));
        }
    }

    /**
     * <b>private void recyclerViewScrollListener()</b>
     * <p>Following function is necessary to listen to RV scroll events</p>
     * <p>Do not forget to include this function while requesting API</p>
     * Created By Rohit
     */
    private void recyclerViewScrollListener() {
        mRvMessageList.addOnScrollListener(new EndlessRvScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isMessageApiInProgress) {
                    JSONObject params = getParamsToPass();
                    //Cancelling Pending Request
                    AppController.getInstance().cancelPendingRequests("newsList");
                    requestNewsListAPI(false, params, messageNextPageUrl, false);
                }
            }
        });
    }

    private void requestNewsListAPI(boolean isProgressDialog, JSONObject params, String strMemberLoadUrl, final boolean isFirstTime) {
        isMessageApiInProgress = true;
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
                            Object resp = AppParser.parseMessageResponse(response.toString());
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
                                    mArrMessageDetails = jsonResultObject.getArrMessageList();
                                    intMessageArraySize = mArrMessageDetails.size();
                                    mRvAdapter = new MessageListAdapter(mArrMessageDetails);
                                    mRvMessageList.setAdapter(mRvAdapter);
                                    mRvMessageList.getAdapter().notifyDataSetChanged();
                                } else {
                                    //API requested on next load
                                    ArrayList<MessageDetailsItem> mArrNextNewsDetails = jsonResultObject.getArrMessageList();
                                    if (mArrNextNewsDetails != null) {
                                        mArrMessageDetails.addAll(mArrNextNewsDetails);
                                        mRvMessageList.getAdapter().notifyItemRangeChanged(intMessageArraySize - 1, mArrMessageDetails.size() - 1);
                                        mRvMessageList.getAdapter().notifyDataSetChanged();
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
                        isMessageApiInProgress = false;
                        mPbLazyLoad.setVisibility(View.GONE);
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                mPbLazyLoad.setVisibility(View.GONE);
                isMessageApiInProgress = false;

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
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_REQUEST_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "newsList");
    }

    /**
     * <b>private void showNoRecordMessage()</b>
     * <p>This function is used to show no record found message</p>
     * Created By Rohit
     */
    private void showNoRecordMessage() {
        Snackbar.make(mParentView.findViewById(R.id.llNewsAndClassifiedHome), "No more messages to show", Snackbar.LENGTH_LONG).show();
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
