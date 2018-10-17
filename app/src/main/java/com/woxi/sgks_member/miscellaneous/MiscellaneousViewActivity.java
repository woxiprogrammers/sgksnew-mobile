package com.woxi.sgks_member.miscellaneous;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

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
        mWebView = findViewById(R.id.webView);
        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            if (bundleExtras.containsKey("activityType")) {
                bundleExtraActivityName = bundleExtras.getString("activityType");
                if (bundleExtraActivityName != null) {
                    getSupportActionBar().setTitle(bundleExtraActivityName);

                    //sgkshealthplus//privacypolicy  //sgkshelp  //sgksqa // Contact Us
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.help))) {
                        paramsWebViewURL = "help";
                    }
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.healthPlus))) {
                        paramsWebViewURL = "health-plus";
                    }
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.privacyPolicy))) {
                        paramsWebViewURL = "privacy-policy";
                    }
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.q_and_a))) {
                        paramsWebViewURL = "q-a";
                    }
                    if (bundleExtraActivityName.equalsIgnoreCase(getString(R.string.contactUs))) {
                        paramsWebViewURL = "contact-us";
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


        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppURLs.API_MISCELLANEOUS_WEBVIEW + paramsWebViewURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mWebView.loadData(response, "text/html", "UTF-8");
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json; charset=UTF-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "miscellaneousView");
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
