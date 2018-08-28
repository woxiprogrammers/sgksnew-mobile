package com.woxi.sgks_member.local_storage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.home.HomeActivity;
import com.woxi.sgks_member.models.CommitteeDetailsItem;
import com.woxi.sgks_member.models.FamilyDetailsItem;
import com.woxi.sgks_member.models.LocalDataSyncItem;
import com.woxi.sgks_member.models.MemberAddressItem;
import com.woxi.sgks_member.models.MemberDetailsItem;
import com.woxi.sgks_member.models.MessageDetailsItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.woxi.sgks_member.interfaces.AppConstants.IS_DATABASE_VERSION_CHANGED;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_LAST_COMMITTEE_ID;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_LAST_FAMILY_ID;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_LAST_MESSAGE_ID;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_LAST_UPDATED_DATE;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_NO_RESULTS_FOUND;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

/**
 * <b>public class DataSyncService extends Service </b>
 * <p>This class/service is used to Sync Data in Background</p>
 * Created by Rohit.
 */
public class DataSyncService extends Service {
    private static final int VOLLEY_SERVICE_REQUEST_SOCKET_TIMEOUT_MS = 3000;
    private static final int VOLLEY_SERVICE_REQUEST_RETRY_COUNT = 3;
    private ArrayList<FamilyDetailsItem> arrFamilyDetailsItems;
    private ArrayList<MemberDetailsItem> arrMemberDetailsItems;
    private ArrayList<MemberAddressItem> arrMemberAddressItems;
    private ArrayList<CommitteeDetailsItem> arrCommitteeDetailsItems;
    private ArrayList<MessageDetailsItem> arrMessageDetailsItems;
    private String TAG = "DataSyncService";
    private DatabaseQueryHandler databaseQueryHandler;
    private String strCurrentServerTime = "";
    private boolean isVersionChanged;

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
        requestLocalDataSyncAPI();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Stop Data Sync Api
//        HomeActivity.stopLocalStorageSyncService(getApplicationContext());
        new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Destroyed DataSyncService");
    }

    /**
     * private void requestLocalDataSyncAPI()
     * This Function is used to request Sync Api.
     * Created by Rohit.
     */
    private void requestLocalDataSyncAPI() {
        JSONObject params = getParamsToPass();
        new AppCommonMethods().LOG(0, TAG, params.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_LOCAL_DATA_SYNC, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
/* is_update_family:false/true     families:	null or [array]     address	:	null or [array]     members	:	null or [array]
is_update_committee	:false/true     committees:	null or [array]     is_update_message:false/true        messages:	null or [array]
current_timestamp:2017-01-25 11:09:11 */
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseLocalDataSyncResponse(response.toString());
                            if (resp instanceof LocalDataSyncItem) {
                                LocalDataSyncItem localDataSyncItem = (LocalDataSyncItem) resp;
                                arrFamilyDetailsItems = localDataSyncItem.getArrFamilyDetailsItems();
                                arrMemberDetailsItems = localDataSyncItem.getArrMemberDetailsItems();
                                arrMemberAddressItems = localDataSyncItem.getArrMemberAddressItems();
                                arrCommitteeDetailsItems = localDataSyncItem.getArrCommitteeDetailsItems();
                                arrMessageDetailsItems = localDataSyncItem.getArrMessageDetailsItems();
                                boolean isFamilySuccessful = false;
                                boolean isCommitteeSuccessful = false;
                                boolean isMessageSuccessful = false;
                                try {
                                    if (response.has("current_timestamp") && !response.getString("current_timestamp").equalsIgnoreCase("")) {
                                        //This Server TimeStamp is used to maintain consistency in time
                                        strCurrentServerTime = response.getString("current_timestamp");
                                        new AppCommonMethods().LOG(0, TAG + "  CurrentServerTime- ", strCurrentServerTime);
                                    }
                                    boolean isMemberSuccessful = false;
                                    boolean isAddressSuccessful = false;
                                    //Check if family updated or not
                                    if (response.has("is_update_family") && response.getString("is_update_family").equalsIgnoreCase("false")) {
                                        //For Insert
                                        //If family(so member & address) available and inserted then isFamilySuccessful=true
                                        //If family(so member & address) not available then isFamilySuccessful=false
                                        if (arrFamilyDetailsItems.size() != 0) {
                                            isFamilySuccessful = databaseQueryHandler.insertOrUpdateAllFamilies(arrFamilyDetailsItems, false);
                                            if (isFamilySuccessful) {
                                                if (arrMemberDetailsItems.size() != 0) {
                                                    isMemberSuccessful = databaseQueryHandler.insertOrUpdateAllMembers(arrMemberDetailsItems);
                                                }
                                                if (arrMemberAddressItems.size() != 0) {
                                                    isAddressSuccessful = databaseQueryHandler.insertOrUpdateAllAddresses(arrMemberAddressItems, false);
                                                }
                                                isFamilySuccessful = isMemberSuccessful && isAddressSuccessful;
                                            }
                                        } else isFamilySuccessful = false;
                                    } else {
                                        //For Update
                                        //Update family or member or address is independent
                                        //So if any of this is true then isFamilySuccessful=true else isFamilySuccessful=false
                                        if (arrFamilyDetailsItems.size() != 0) {
                                            isFamilySuccessful = databaseQueryHandler.insertOrUpdateAllFamilies(arrFamilyDetailsItems, false);
                                        }
                                        if (arrMemberDetailsItems.size() != 0) {
                                            isMemberSuccessful = databaseQueryHandler.insertOrUpdateAllMembers(arrMemberDetailsItems);
                                        }
                                        if (arrMemberAddressItems.size() != 0) {
                                            isAddressSuccessful = databaseQueryHandler.insertOrUpdateAllAddresses(arrMemberAddressItems, true);
                                        }
                                        AppCommonMethods.putStringPref(PREFS_LAST_UPDATED_DATE, strCurrentServerTime, getApplicationContext());
                                        new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Family Update Sync Complete At " + strCurrentServerTime);
                                        isFamilySuccessful = isFamilySuccessful || isMemberSuccessful || isAddressSuccessful;
                                    }
                                    //Check if committee updated or not
                                    //If committee available and inserted then isCommitteeSuccessful=true
                                    //If committee not available then isCommitteeSuccessful=false
                                    if (response.has("is_update_committee") && response.getString("is_update_committee").equalsIgnoreCase("false")) {
                                        //For Insert
                                        isCommitteeSuccessful = arrCommitteeDetailsItems.size() != 0 && databaseQueryHandler.insertOrUpdateAllCommittees(arrCommitteeDetailsItems, false);
                                    } else {
                                        //For Update
                                        if (arrCommitteeDetailsItems.size() != 0) {
                                            isCommitteeSuccessful = databaseQueryHandler.insertOrUpdateAllCommittees(arrCommitteeDetailsItems, true);
                                            AppCommonMethods.putStringPref(PREFS_LAST_UPDATED_DATE, strCurrentServerTime, getApplicationContext());
                                            new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Committee Update Sync Complete At " + strCurrentServerTime);
                                        } else isCommitteeSuccessful = false;
                                    }
                                    //Check if message updated or not
                                    //If message available and inserted then isMessageSuccessful=true
                                    //If message not available then isMessageSuccessful=false
                                    if (response.has("is_update_message") && response.getString("is_update_message").equalsIgnoreCase("false")) {
                                        //For Insert
                                        isMessageSuccessful = arrMessageDetailsItems.size() != 0 && databaseQueryHandler.insertOrUpdateAllMessages(arrMessageDetailsItems, false);
                                    } else {
                                        //For Update
                                        if (arrMessageDetailsItems.size() != 0) {
                                            isMessageSuccessful = databaseQueryHandler.insertOrUpdateAllMessages(arrMessageDetailsItems, true);
                                            AppCommonMethods.putStringPref(PREFS_LAST_UPDATED_DATE, strCurrentServerTime, getApplicationContext());
                                            new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Message Update Sync Complete At " + strCurrentServerTime);
                                        } else isMessageSuccessful = false;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "Transactions: " + "isFamilySuccessful-" + isFamilySuccessful + "  isCommitteeSuccessful-" + isCommitteeSuccessful + "  isMessageSuccessful-" + isMessageSuccessful);
                                //If any of transaction is true(successful) then request api again else sync is complete
                                if (isFamilySuccessful || isCommitteeSuccessful || isMessageSuccessful) {
                                    //Data inserted locally
                                    new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Data Insert Successful");

                                    if (!isVersionChanged) {
                                        AppCommonMethods.putBooleanPref(IS_DATABASE_VERSION_CHANGED, true, getApplicationContext());
                                    }

                                    //Request Sync Api again to fetch next data
                                    requestLocalDataSyncAPI();
                                } else {
                                    //All transactions false so stop service
//                                    HomeActivity.stopLocalStorageSyncService(getApplicationContext());
                                    AppCommonMethods.putStringPref(PREFS_LAST_UPDATED_DATE, strCurrentServerTime, getApplicationContext());
                                    new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Data Sync Complete At " + strCurrentServerTime);
                                }
                            } else {
                                //Response Parsing Failed so stop service
                                new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Service Response Parse Failed");
//                                HomeActivity.stopLocalStorageSyncService(getApplicationContext());
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
//                HomeActivity.stopLocalStorageSyncService(getApplicationContext());
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

    private JSONObject getParamsToPass() {
        JSONObject params = new JSONObject();
        /*last_updated_date   //format "2017-01-19 10:16:10"
        * last_family_id
        * last_committee_id
        * last_message_id*/

        String lastUpdatedDate = AppCommonMethods.getStringPref(PREFS_LAST_UPDATED_DATE, getApplicationContext());
        if (lastUpdatedDate.equalsIgnoreCase("")) {
            lastUpdatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        }
        isVersionChanged = AppCommonMethods.getBooleanPref(IS_DATABASE_VERSION_CHANGED, getApplicationContext());

        String lastFamilyId = AppCommonMethods.getStringPref(PREFS_LAST_FAMILY_ID, getApplicationContext());
        if (lastFamilyId.equalsIgnoreCase("") || !isVersionChanged) {
            lastFamilyId = "0";
        }
        String lastCommitteeId = AppCommonMethods.getStringPref(PREFS_LAST_COMMITTEE_ID, getApplicationContext());
        if (lastCommitteeId.equalsIgnoreCase("") || !isVersionChanged) {
            lastCommitteeId = "0";
        }
        String lastMessageId = AppCommonMethods.getStringPref(PREFS_LAST_MESSAGE_ID, getApplicationContext());
        if (lastMessageId.equalsIgnoreCase("") || !isVersionChanged) {
            lastMessageId = "0";
        }

        try {
            params.put("last_updated_date", lastUpdatedDate);
            params.put("last_family_id", lastFamilyId);
            params.put("last_committee_id", lastCommitteeId);
            params.put("last_message_id", lastMessageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
}
