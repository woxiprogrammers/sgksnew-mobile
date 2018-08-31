package com.woxi.sgks_member.miscellaneous;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.interfaces.AppConstants;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.utils.AppCommonMethods;
import com.woxi.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>public class MiscellaneousViewActivity extends AppCompatActivity implements AppConstants</b>
 * <p>This class is used for "Miscellaneous" pages</p>
 * <p>Multiple uses of the same class</p>
 * Created by Rohit.
 */
public class MiscellaneousViewActivity extends AppCompatActivity implements AppConstants {
    private Context mContext;
    private WebView mWebView;
    private String strWebViewContent = "", TAG = "MiscellaneousViewActivity", bundleExtraActivityName, paramsWebViewURL;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mContext = MiscellaneousViewActivity.this;
        mWebView = (WebView) findViewById(R.id.webView);

        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            if (bundleExtras.containsKey("activityType")) {
                bundleExtraActivityName = bundleExtras.getString("activityType");
                if (bundleExtraActivityName != null) {
                    getSupportActionBar().setTitle(bundleExtraActivityName);

                    //sgkshealthplus  //privacypolicy  //sgkshelp  //sgksqa
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.help))) {
                        paramsWebViewURL = "sgkshelp";
                    }
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.healthPlus))) {
                        paramsWebViewURL = "sgkshealthplus";
                    }
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.privacyPolicy))) {
                        paramsWebViewURL = "privacypolicy";
                    }
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.q_and_a))) {
                        paramsWebViewURL = "sgksqa";
                    }
                }
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

//        mWebView.getSettings().setJavaScriptEnabled(true);

        /*strWebViewContent = "<html><body><h1>Listing</h1><ol><li><b>item1</b></li><li>item2</li><li>item3</li></ol><p><span style=\"color: rgb(0, 0, 0);text-align: justify;float: none;background-color: rgb(255, 255, 255);\"><span class=\"Apple-converted-space\"> <strong style=\"color: rgb(0, 0, 0);text-align: justify;background-color: rgb(255, 255, 255);\">Lorem Ipsum</strong><span style=\"color: rgb(0, 0, 0);text-align: justify;float: none;background-color: rgb(255, 255, 255);\"><span class=\"Apple-converted-space\"> </span></span></span>is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.  is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</span>\n" +
                "</p></body></html>";*/
        requestWebViewContentAPI();
    }

    private void requestWebViewContentAPI() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);

        ///sgksmain/miscellaneous/sgkshealthplus?city=PUNE
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_MISCELLANEOUS_WEBVIEW + paramsWebViewURL + AppURLs.API_CITY + currentCity, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());

                        try {
                            if (response.has("data") && response.getString("data") != null) {
                                strWebViewContent = response.getString("data");
                            }
                            mWebView.loadData(strWebViewContent, "text/html", "UTF-8");
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "miscellaneousView");
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
