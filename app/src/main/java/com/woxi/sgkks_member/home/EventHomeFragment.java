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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.adapters.EventsListAdapter;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.FragmentInterface;
import com.woxi.sgkks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgkks_member.miscellaneous.AccountsActivity;
import com.woxi.sgkks_member.models.EventDataItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppSettings;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LANGUAGE_APPLIED;
import static com.woxi.sgkks_member.interfaces.AppConstants.STATUS_NO_RESULTS_FOUND;
import static com.woxi.sgkks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

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
    private ArrayList<EventDataItem> mArrEventOfflineData;
    private boolean isApiRequested = false;
    private String selectedYear = "2018";
    private Spinner mSpinAccountYear;
    private DatabaseQueryHandler databaseQueryHandler;
    private ArrayList<Integer> arrayYearIntegerList = new ArrayList<>();

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
        for (int i = 2015; i <= 2025; i++){
            arrayYearIntegerList.add(i-2015,i);
        }
        ArrayAdapter<Integer> integerArrayAdapter = new ArrayAdapter<Integer>(mContext, android.R.layout.simple_spinner_item, arrayYearIntegerList);
        integerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinAccountYear.setAdapter(integerArrayAdapter);
        mSpinAccountYear.setSelection(3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRvEventList.setLayoutManager(linearLayoutManager);
        mArrEventData = new ArrayList<>();
        mEventListAdapter = new EventsListAdapter(mArrEventData);
        mRvEventList.setAdapter(mEventListAdapter);
        databaseQueryHandler = new DatabaseQueryHandler(mContext,false);
        eventClickListner();
        //Function for spinner year change listener
        mSpinAccountYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedYear = mSpinAccountYear.getSelectedItem().toString();
                Log.i("@@@", "onItemSelected: "+selectedYear);
                if (!selectedYear.equalsIgnoreCase("null")) {
                    if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                        requestEventDetailsApi(String.valueOf(parent.getSelectedItem()));
                    } else {
                     mArrEventOfflineData = databaseQueryHandler.queryEvents(selectedYear);
                     Log.i(TAG, "onItemSelected: "+mArrEventOfflineData);
                     setupEventData(mArrEventOfflineData);
                    }
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
        boolean isLanguageChanged = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_LANGUAGE_CHANGED,mContext);
        boolean isCityChanged = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_CITY_CHANGED,mContext);
        if(isLanguageChanged || isCityChanged){
            if(new AppCommonMethods(mContext).isNetworkAvailable()){
                requestEventDetailsApi(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            } else {
                mArrEventOfflineData = databaseQueryHandler.queryEvents(selectedYear);
                setupEventData(mArrEventOfflineData);
            }
        }

    }

    @Override
    public void fragmentBecameVisible() {
        if (!isApiRequested) {
            if(new AppCommonMethods(mContext).isNetworkAvailable()){
                requestEventDetailsApi(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            } else {
                mArrEventOfflineData = databaseQueryHandler.queryEvents(selectedYear);
                setupEventData(mArrEventOfflineData);
            }
        }
    }

    private void requestEventDetailsApi(String year) {
        //ToDo Lazy Loading
        JSONObject params=new JSONObject();
        try {
            params.put("page_id",0);
            params.put("sgks_city",AppSettings.getStringPref(PREFS_CURRENT_CITY,mContext));
            params.put("language_id",AppSettings.getStringPref(PREFS_LANGUAGE_APPLIED,mContext));
            params.put("year",year);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_EVENT_LISTING, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseEventDetailsResponse(response.toString());
                            if (resp instanceof ArrayList) {
                                mArrEventData = (ArrayList<EventDataItem>) resp;
                                if(mArrEventData.size() != 0){
                                    mEventListAdapter = new EventsListAdapter(mArrEventData);
                                    mRvEventList.setAdapter(mEventListAdapter);
                                } else {
                                    //Toast.makeText(mContext,"No Records Found",Toast.LENGTH_SHORT).show();
                                    mEventListAdapter = new EventsListAdapter(mArrEventData);
                                    mRvEventList.setAdapter(mEventListAdapter);
                                }
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

    private void setupEventData(ArrayList<EventDataItem> arrList) {
        mEventListAdapter = new EventsListAdapter(arrList);
        mRvEventList.setAdapter(mEventListAdapter);
    }

    private void eventClickListner () {
        if (new AppCommonMethods(mContext).isNetworkAvailable()){
            onEventClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View selectedView) {
                    Intent intent = new Intent(mContext, EventAndClassifiedDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("eventDetails", mArrEventData.get(mRvEventList.getChildAdapterPosition(selectedView)));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };
        } else {
            onEventClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View selectedView) {
                    Intent intent = new Intent(mContext, EventAndClassifiedDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("eventDetails", mArrEventOfflineData.get(mRvEventList.getChildAdapterPosition(selectedView)));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };
        }
    }
}