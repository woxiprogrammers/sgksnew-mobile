package com.woxi.sgks_member;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.home.HomeActivity;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.models.MasterDataItem;
import com.woxi.sgks_member.models.SGKSAreaItem;
import com.woxi.sgks_member.models.SGKSCategory;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashAndCityActivity extends AppCompatActivity implements AppConstants {
    private static final String APP_FIRST_RUN = "appFirstRun";
    private Context mContext;
    private String TAG = "SplashAndCityActivity";
    private boolean isMasterApiInProgress;
    public static ArrayList<SGKSAreaItem> sgksAreaItems;
    public static ArrayList<SGKSCategory> sgksCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = SplashAndCityActivity.this;
        getNextScreen();
    }

    private void getNextScreen() {
        Handler sleepHandler = new Handler();
        sleepHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentHome = new Intent(mContext, HomeActivity.class);
                startActivity(intentHome);
                finish();
                requestMasterList();
//                boolean notFirstRun = AppCommonMethods.getBooleanPref(APP_FIRST_RUN, mContext);
                /*if (!notFirstRun) {
                    getCitySelectionScreen();
                    //Local Storage Sync is enabled by-default.
                    AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_LOCAL_STORAGE_SYNC_ENABLED, true, mContext);
                    AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, true, mContext);
                } else {
                    //Checking Network Status
                    if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                        requestMasterList();
                    } else {
                        boolean isOfflineSupportEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, mContext);
                        if (isOfflineSupportEnabled) {
                            DatabaseQueryHandler databaseQueryHandler = new DatabaseQueryHandler(mContext, false);
                            //Passing "" to get all members
                            String strCurrentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
                            ArrayList<MemberDetailsItem> mArrMemDetails = databaseQueryHandler.queryMembers("", strCurrentCity);
                            if (mArrMemDetails == null || mArrMemDetails.size() == 0) {
                                getMasterListOnline();
                            } else {
                                Toast.makeText(mContext, getString(R.string.toast_you_are_offline), Toast.LENGTH_SHORT).show();
                                Intent intentHome = new Intent(mContext, HomeActivity.class);
                                startActivity(intentHome);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        } else {
                            new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.noInternet));
                        }
                    }
                }*/
            }
        }, SPLASH_SCREEN_TIME);
    }

    String stringArrayList;
    private void requestMasterList() {
        isMasterApiInProgress = true;
        String url="http://www.mocky.io/v2/5ba0dec53500004f005bbad3";

        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
               AppURLs.API_MASTER_LIST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseNewMasterList(response.toString());
                            if (resp instanceof MasterDataItem) {
                                MasterDataItem masterDataItem= (MasterDataItem) resp;
                                sgksAreaItems=masterDataItem.getSgksAreaItems();
                                sgksCategory=masterDataItem.getSgksCategoryArrayList();
                                /*MasterDataItem masterDataItem = (MasterDataItem) resp;


                                for (int i = 0; i < masterDataItem.getSgksAreaItems().size(); i++) {
                                    stringArrayList=masterDataItem.getSgksAreaItems().get(i).getAreaName();
                                }
                                Log.i("@@string",stringArrayList);
                                AppCommonMethods.putStringPref(AppConstants.PREFS_SGKS_AREA_LIST, stringArrayList, mContext);
                                Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();*/

                            } else {
                                Toast.makeText(mContext, "Fail", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /*try {
                            Object resp = AppParser.parseMasterListResponse(response.toString());
                            if (resp instanceof MasterDataItem) {
                                new AppCommonMethods().LOG(0, TAG, resp.toString());
                                //Saving values in preferences for later use
                                String strSuggestionCategory = ((MasterDataItem) resp).getStrSuggestionCategory();
                                AppCommonMethods.putStringPref(AppConstants.PREFS_SUGGESTION_CATEGORY, strSuggestionCategory, mContext);
                                String strSgksArea = ((MasterDataItem) resp).getStrSgksArea();
                                AppCommonMethods.putStringPref(AppConstants.PREFS_SGKS_AREA_LIST, strSgksArea, mContext);
                                String strMessageIds = ((MasterDataItem) resp).getStrMessageIds();
                                if (!strMessageIds.equalsIgnoreCase("null")) {
                                    AppCommonMethods.putStringPref(AppConstants.PREFS_LATEST_MESSAGE_ID, strMessageIds, mContext);
                                } else {
                                    AppCommonMethods.putStringPref(AppConstants.PREFS_LATEST_MESSAGE_ID, "", mContext);
                                }
                                String strBuzzIds = ((MasterDataItem) resp).getStrSgksBuzz_Id();
                                AppCommonMethods.putStringPref(AppConstants.PREFS_LATEST_BUZZ_ID, strBuzzIds, mContext);
                                String strBuzzImageUrl = ((MasterDataItem) resp).getStrSgksBuzz_ImgUrl();
                                AppCommonMethods.putStringPref(AppConstants.PREFS_BUZZ_IMAGE_URL, strBuzzImageUrl, mContext);
                                int intTotalMemberCount = ((MasterDataItem) resp).getIntTotalMemberCount();
                                AppCommonMethods.putIntPref(AppConstants.PREFS_TOTAL_MEMBER_COUNT, intTotalMemberCount, mContext);
                                String strClassifiedIds = ((MasterDataItem) resp).getStrClassifiedIds();
                                if (!strClassifiedIds.equalsIgnoreCase("null")) {
                                    AppCommonMethods.putStringPref(AppConstants.PREFS_LATEST_CLASSIFIED_ID, strClassifiedIds, mContext);
                                } else {
                                    AppCommonMethods.putStringPref(AppConstants.PREFS_LATEST_CLASSIFIED_ID, "", mContext);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        isMasterApiInProgress = false;
                        Intent intentHome = new Intent(mContext, HomeActivity.class);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentHome);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isMasterApiInProgress = false;
                new AppCommonMethods().LOG(0, TAG, "Error: " + error.getMessage());
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "getMasterList");
    }
}
