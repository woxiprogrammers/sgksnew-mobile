package com.woxi.sgkks_member.local_storage;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.home.HomeActivity;
import com.woxi.sgkks_member.home.MemberHomeNewFragment;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.models.AccountDetailsItem;
import com.woxi.sgkks_member.models.CityIteam;
import com.woxi.sgkks_member.models.ClassifiedDetailsItem;
import com.woxi.sgkks_member.models.CommitteeDetailsItem;
import com.woxi.sgkks_member.models.EventDataItem;
import com.woxi.sgkks_member.models.FamilyDetailsItem;
import com.woxi.sgkks_member.models.LocalDataSyncItem;
import com.woxi.sgkks_member.models.MemberAddressItem;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.models.MessageDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.woxi.sgkks_member.interfaces.AppConstants.IS_DATABASE_VERSION_CHANGED;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_COMMITTEE_ID;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_FAMILY_ID;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_MESSAGE_ID;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LAST_UPDATED_DATE;
import static com.woxi.sgkks_member.interfaces.AppConstants.STATUS_NO_RESULTS_FOUND;
import static com.woxi.sgkks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

/**
 * <b>public class DataSyncService extends Service </b>
 * <p>This class/service is used to Sync Data in Background</p>
 * Created by Rohit.
 */
public class DataSyncService extends Service {
    private static final int VOLLEY_SERVICE_REQUEST_SOCKET_TIMEOUT_MS = 3000;
    private static final int VOLLEY_SERVICE_REQUEST_RETRY_COUNT = 3;
    private ArrayList<MemberDetailsItem> arrMemberDetailsItems;
    private ArrayList<MemberDetailsItem> arrMemberDetailsGujaratiItems;
    private ArrayList<CityIteam> arrCityEnglishItems;
    private ArrayList<CityIteam> arrCityGujaratiItems;
    private ArrayList<EventDataItem> arrEventEnglish;
    private ArrayList<EventDataItem> arrEventGujarati;
    private ArrayList<MessageDetailsItem> arrMessageDetailsItems;
    private ArrayList<MessageDetailsItem> arrMessageGujaratiDetailsItems;
    private ArrayList<ClassifiedDetailsItem> arrClassifiedEnglish;
    private ArrayList<ClassifiedDetailsItem> arrClassifiedGujarati;
    private ArrayList<AccountDetailsItem> arrAccountEnglish;
    private ArrayList<AccountDetailsItem> arrAccountGujarati;
    private ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItems;
    private ArrayList<CommitteeDetailsItem> arrCommitteeGujarati;
    private String TAG = "DataSyncService";
    private DatabaseQueryHandler databaseQueryHandler;
    private String strCurrentServerTime = "";
    private boolean isVersionChanged;
    boolean isCommitteeSuccessful = false, isGujaratiCommitteeSuccessful = false;
    boolean isMessageSuccessful = false, isGujaratiMessagesSuccessful = false;
    boolean isMemberSuccessful = false, isGujaratiMemberSuccessful = false;
    boolean isEventSuccessful = false, isGujaratiEventSuccessful = false;
    boolean isAccountSuccessful = false, isGujaratiAccountSuccessful = false;
    boolean isClassifiedSuccessful = false, isGujaratiClassifiedSuccessful = false;
    boolean isCitySuccessful = false, isGujaratiCitySuccessful = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Created DataSyncService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Started DataSyncService");
        try {
            databaseQueryHandler = new DatabaseQueryHandler(getApplicationContext(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Request Data Sync Api
        if (new AppCommonMethods(getApplicationContext()).isNetworkAvailable()){
            requestLocalDataSyncAPI();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Stop Data Sync Api
        HomeActivity.stopLocalStorageSyncService(getApplicationContext());
        new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Destroyed DataSyncService");
    }

    /**
     * private void requestLocalDataSyncAPI()
     * This Function is used to request Sync Api.
     * Created by Rohit.
     * Modified by Sharvari
     */
    private void requestLocalDataSyncAPI() {
        JSONObject params = new JSONObject();
        try {
            params.put("current_timestamp", AppCommonMethods.getStringPref(PREFS_LAST_UPDATED_DATE, getApplicationContext()));
            Log.i("@@", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // String url = "http://www.mocky.io/v2/5c27661630000011000bf70b";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_SKS_OFFLINE, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseLocalDataSyncResponse(response.toString());
                            if (resp instanceof LocalDataSyncItem) {
                                LocalDataSyncItem localDataSyncItem = (LocalDataSyncItem) resp;
                                arrMemberDetailsItems= localDataSyncItem.getArrMemberDetailsItems();
                                arrMemberDetailsGujaratiItems = localDataSyncItem.getArrMemberDetailsGujaratiItems();
                                arrCityEnglishItems = localDataSyncItem.getArrCityItems();
                                arrCityGujaratiItems = localDataSyncItem.getArrCityItemsGujarati();
                                arrEventEnglish = localDataSyncItem.getArrEventDataItem();
                                arrEventGujarati = localDataSyncItem.getArrEventDataGujaratiItem();
                                arrMessageDetailsItems = localDataSyncItem.getArrMessageDetailsItems();
                                arrMessageGujaratiDetailsItems = localDataSyncItem.getArrMessageDetailsGujaratiItems();
                                arrClassifiedEnglish = localDataSyncItem.getArrClassifiedItems();
                                arrClassifiedGujarati = localDataSyncItem.getArrClassifiedGujaratiItems();
                                arrAccountEnglish = localDataSyncItem.getArrAccountItem();
                                arrAccountGujarati = localDataSyncItem.getArrAccountGujaratiItem();
                                arrCommitteeDetailsItems = localDataSyncItem.getArrCommitteeDetailsItems();
                                arrCommitteeGujarati = localDataSyncItem.getArrCommitteeDetailsGujaratiItems();

                                try {
                                    if (response.has("current_timestamp") && !response.getString("current_timestamp").equalsIgnoreCase("")) {
                                        //This Server TimeStamp is used to maintain consistency in time
                                        strCurrentServerTime = response.getString("current_timestamp");
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(AppConstants.PREFS_LAST_UPDATED_DATE, strCurrentServerTime);
                                        editor.apply();
                                        new AppCommonMethods().LOG(0, TAG + "  CurrentServerTime- ", strCurrentServerTime);
                                    }
                                    if (arrMemberDetailsItems != null) {
                                        isMemberSuccessful = databaseQueryHandler.insertOrUpdateAllMembersEnglish(arrMemberDetailsItems);
                                    }
                                    if (arrMemberDetailsGujaratiItems != null) {
                                        isGujaratiMemberSuccessful = databaseQueryHandler.insertOrUpdateAllMembersGujarati(arrMemberDetailsGujaratiItems);
                                    }
                                    if(arrCityEnglishItems != null){
                                        isCitySuccessful = databaseQueryHandler.insertOrUpdateCitiesEnglish(arrCityEnglishItems);
                                    }
                                    if (arrCityGujaratiItems != null){
                                        isGujaratiCitySuccessful = databaseQueryHandler.insertOrUpdateCitiesGujarati(arrCityGujaratiItems);
                                    }
                                    if(arrEventEnglish != null){
                                        isEventSuccessful = databaseQueryHandler.insertOrUpdateEventsEnglish(arrEventEnglish);
                                    }
                                    if(arrEventGujarati != null){
                                        isGujaratiCitySuccessful = databaseQueryHandler.insertOrUpdateEventGujarati(arrEventGujarati);
                                    }
                                    if (arrMessageDetailsItems != null){
                                         isMessageSuccessful = databaseQueryHandler.insertOrUpdateMessageEnglish(arrMessageDetailsItems);
                                    }
                                    if (arrMessageGujaratiDetailsItems != null){
                                        isGujaratiMessagesSuccessful = databaseQueryHandler.insertOrUpdateMessageGujarati(arrMessageGujaratiDetailsItems);
                                    }
                                    if (arrClassifiedEnglish != null) {
                                        isClassifiedSuccessful = databaseQueryHandler.inserOrUpdateClassifiedEnglish(arrClassifiedEnglish);
                                    }
                                    if (arrClassifiedGujarati != null) {
                                        isGujaratiClassifiedSuccessful = databaseQueryHandler.insertOrUpdateClassifiedGujarati(arrClassifiedGujarati);
                                    }
                                    if (arrAccountEnglish != null) {
                                        isAccountSuccessful = databaseQueryHandler.insertOrUpdateAccountsEnglish(arrAccountEnglish);
                                    }
                                    if (arrAccountGujarati != null){
                                        isGujaratiAccountSuccessful = databaseQueryHandler.insertOrUpdateAccountGujarati(arrAccountGujarati);
                                    }
                                    if (arrCommitteeDetailsItems != null) {
                                        isCommitteeSuccessful = databaseQueryHandler.insertOrUpdateCommitteeDetailsEnglish(arrCommitteeDetailsItems);
                                    }
                                    if (arrCommitteeGujarati != null) {
                                        isGujaratiCommitteeSuccessful = databaseQueryHandler.insertOrUpdateCommitteeDetailsGuajrati(arrCommitteeGujarati);
                                    }
                                    databaseQueryHandler.insertCount(arrCityEnglishItems,arrMessageDetailsItems,arrClassifiedEnglish);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //If any of transaction is true(successful) then request api again else sync is complete
                                if ((isEventSuccessful || isGujaratiEventSuccessful) &&
                                        (isCommitteeSuccessful || isGujaratiCommitteeSuccessful) &&
                                        (isMemberSuccessful || isGujaratiMemberSuccessful) &&
                                        (isMessageSuccessful || isGujaratiMessagesSuccessful) &&
                                        (isClassifiedSuccessful || isGujaratiClassifiedSuccessful) &&
                                        (isAccountSuccessful || isGujaratiAccountSuccessful) &&
                                        (isCitySuccessful || isGujaratiCitySuccessful)) {
                                    HomeActivity.stopLocalStorageSyncService(getApplicationContext());
                                    AppCommonMethods.putStringPref(PREFS_LAST_UPDATED_DATE, strCurrentServerTime, getApplicationContext());
                                    new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Data Sync Complete At " + strCurrentServerTime);
                                }
                            } else {
                                //Response Parsing Failed so stop service
                                new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Service Response Parse Failed");
                                HomeActivity.stopLocalStorageSyncService(getApplicationContext());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                //Stop Service if api failed
                HomeActivity.stopLocalStorageSyncService(getApplicationContext());
                if (response != null) {
                    new AppCommonMethods().LOG(0, TAG, "response code " + error.networkResponse.statusCode + " message= " + new String(error.networkResponse.data));
                    try {
                        if (response.statusCode == STATUS_SOMETHING_WENT_WRONG) {
                            new AppCommonMethods(getBaseContext()).LOG(0, TAG, "" + (new JSONObject(new String(response.data))).getString("message"));
                        }
                        if (response.statusCode == STATUS_NO_RESULTS_FOUND) {
                            new AppCommonMethods(getBaseContext()).LOG(0, TAG, "" + (new JSONObject(new String(response.data))).getString("message"));
                        } else {
                            new AppCommonMethods(getBaseContext()).LOG(0, TAG, getString(R.string.optional_api_error));
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_SERVICE_REQUEST_SOCKET_TIMEOUT_MS, VOLLEY_SERVICE_REQUEST_RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, getString(R.string.tag_local_storage_sync));
    }
}
