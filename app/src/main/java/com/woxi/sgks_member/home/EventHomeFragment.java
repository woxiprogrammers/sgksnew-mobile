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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.adapters.EventsListAdapter;
import com.woxi.sgks_member.interfaces.FragmentInterface;
import com.woxi.sgks_member.models.EventDataItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_NO_RESULTS_FOUND;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

/**
 * <b><b>public class EventHomeFragment extends Fragment implements AdapterView.OnItemClickListener, FragmentInterface</b></b>
 * <p>This class is used for App-home Event-listing</p>
 * Created by Rohit.
 */
public class EventHomeFragment extends Fragment implements FragmentInterface {
    public static View.OnClickListener onEventClickListener;
    private Context mContext;
    private View mParentView;
    private String TAG = "EventHomeFragment";
    private RecyclerView mRvEventList;
    private EventsListAdapter mEventListAdapter;
    private ArrayList<EventDataItem> mArrEventData;
    private boolean isApiRequested = false;
    private String selectedYear = "";
    private ArrayList<String> mArrEventYears;
    private Spinner mSpinAccountYear;

    public EventHomeFragment() {
    }

    public static EventHomeFragment newInstance() {
        return new EventHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.events_and_account_home, container, false);
        //Initialize Views
        initializeViews();
        return mParentView;
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    public void initializeViews() {
        mContext = getActivity();
        mRvEventList =  mParentView.findViewById(R.id.rvAccountImages);
        mSpinAccountYear = mParentView.findViewById(R.id.spinAccountYear);
        ((TextView) mParentView.findViewById(R.id.tvYearTitle)).setText("Select Event Year");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRvEventList.setLayoutManager(linearLayoutManager);
        mArrEventData = new ArrayList<>();
        mEventListAdapter = new EventsListAdapter(mArrEventData);
        mRvEventList.setAdapter(mEventListAdapter);

        onEventClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View selectedView) {
               /* Intent intent = new Intent(mContext, EventAndClassifiedDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("eventDetails", mArrEventData.get(mRvEventList.getChildAdapterPosition(selectedView)));
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        };

        //Function for spinner year change listener
        mSpinAccountYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = mSpinAccountYear.getSelectedItem().toString();
                if (!selectedYear.equalsIgnoreCase("null")) {
                    requestEventDetailsApi();
                } else {
                    ArrayAdapter<String> arrayYearAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, new ArrayList<String>());
                    arrayYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinAccountYear.setAdapter(arrayYearAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void fragmentBecameVisible() {
        if (!isApiRequested) {
            AppController.getInstance().cancelPendingRequests("apiEventYearListTAG");
            requestEventYearListApi();
        }
    }

    private void requestEventYearListApi() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
        ///sgksmain/events/lists?city=PUNE&year=2017
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_EVENT_YEAR_LIST + currentCity, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        String strEventYears = "";
                        if (response.has("event_year") && response.optString("event_year") != null && !response.optString("event_year").equalsIgnoreCase("") && !response.optString("event_year").equalsIgnoreCase("null")) {
                            ((TextView) mParentView.findViewById(R.id.tvNotAvailable)).setVisibility(View.GONE);
                            ((LinearLayout) mParentView.findViewById(R.id.llSelectYear)).setVisibility(View.VISIBLE);

                            strEventYears = response.optString("event_year");
                            strEventYears = strEventYears.replace("[", "");
                            strEventYears = strEventYears.replace("]", "");
                            strEventYears = strEventYears.replace("\"", "");
                            mArrEventYears = new ArrayList<>(Arrays.asList(strEventYears.split(",")));

                            ArrayAdapter<String> arrayYearAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mArrEventYears);
                            arrayYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mSpinAccountYear.setAdapter(arrayYearAdapter);
                        } else {
                            ((TextView) mParentView.findViewById(R.id.tvNotAvailable)).setVisibility(View.VISIBLE);
                            ((TextView) mParentView.findViewById(R.id.tvNotAvailable)).setText("No Event To Show");
                            ((LinearLayout) mParentView.findViewById(R.id.llSelectYear)).setVisibility(View.GONE);
                        }
                        pDialog.dismiss();
                        isApiRequested = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    ((TextView) mParentView.findViewById(R.id.tvNotAvailable)).setVisibility(View.VISIBLE);
                    ((TextView) mParentView.findViewById(R.id.tvNotAvailable)).setText("No Event To Show");
                    ((LinearLayout) mParentView.findViewById(R.id.llSelectYear)).setVisibility(View.GONE);
                    new AppCommonMethods().LOG(0, TAG, "response code " + error.networkResponse.statusCode + " message= " + new String(error.networkResponse.data));
                    try {
                        if (response.statusCode == STATUS_SOMETHING_WENT_WRONG) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else if (response.statusCode == STATUS_NO_RESULTS_FOUND) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "apiEventYearListTAG");
    }

    private void requestEventDetailsApi() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        selectedYear = mSpinAccountYear.getSelectedItem().toString();
        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
        ///sgksmain/events/lists?city=PUNE&year=2017
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_EVENT_DETAILS_LIST + currentCity + "&year=" + selectedYear, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseEventDetailsResponse(response.toString());
                            if (resp instanceof ArrayList) {
                                mArrEventData = (ArrayList<EventDataItem>) resp;
                                mEventListAdapter = new EventsListAdapter(mArrEventData);
                                mRvEventList.setAdapter(mEventListAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                        isApiRequested = true;
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "apiEventDetailsTAG");
    }
}