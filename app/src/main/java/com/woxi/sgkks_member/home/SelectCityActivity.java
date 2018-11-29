package com.woxi.sgkks_member.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.adapters.CityAdapter;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgkks_member.models.CityIteam;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.woxi.sgkks_member.interfaces.AppConstants.CURRENT_PAGE;

public class SelectCityActivity extends AppCompatActivity {
    private ProgressBar pbSearchCity;
    private String strSearchCity="";
    public static View.OnClickListener onCityClickListener;
    private EditText etSearchCity;
    private RecyclerView rvCityList;
    private RecyclerView.Adapter rvAdapter;
    private LinearLayoutManager linearLayoutManager;
    public static ArrayList<CityIteam> arrCityList;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.select_city);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
    }

    private void initializeViews(){
        mContext = SelectCityActivity.this;
        pbSearchCity = findViewById(R.id.pbCityListing);
        etSearchCity = findViewById(R.id.etSearchCity);
        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 2){
                    strSearchCity = charSequence.toString();
                } else {
                    strSearchCity = "";
                }
                if(new AppCommonMethods(mContext).isNetworkAvailable()){
                    requestCityList();
                } else {
                    new AppCommonMethods(mContext).showAlert("You are Offline");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rvCityList = findViewById(R.id.rvCityList);
        linearLayoutManager = new LinearLayoutManager(mContext);
        rvCityList.setLayoutManager(linearLayoutManager);
        if(new AppCommonMethods(mContext).isNetworkAvailable()){
            requestCityList();
        } else {
            new AppCommonMethods(mContext).showAlert("You are offline");
        }
        onCityClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String position = String.valueOf(rvCityList.getChildLayoutPosition(v)+1);
                AppCommonMethods.putStringPref(AppConstants.PREFS_CURRENT_CITY,position,mContext);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(AppConstants.PREFS_CURRENT_CITY,position);
                editor.apply();
                restartActivity(mContext);
            }
        };
    }

    private void restartActivity(Context context) {
        try {
//            String lang = AGAppSettings.getStringPref(PREFS_LANGUAGE_APPLIED, mContext);
//            Context context = LocaleHelper.setLocale(this, lang);

            Intent intentHome = new Intent(mContext, HomeActivity.class);
            Bundle bundleExtras = new Bundle();
            bundleExtras.putBoolean("isFromLanguage", true);
            intentHome.putExtra("bundleHome", bundleExtras);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intentHome);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void requestCityList(){
        pbSearchCity.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("search_city",strSearchCity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_GET_CITY, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    new AppCommonMethods(mContext).LOG(0, "get_city", response.toString());
                    Object resp = AppParser.parseCityResponse(response.toString());
                    arrCityList = (ArrayList<CityIteam>) resp;
                    if(resp instanceof Boolean){
                        Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                    } else if (resp instanceof ArrayList){
                        rvCityList.setHasFixedSize(true);
                        rvAdapter = new CityAdapter(arrCityList);
                        rvCityList.setAdapter(rvAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pbSearchCity.setVisibility(View.GONE);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AppCommonMethods(mContext).LOG(0, "error_city", error.toString());
                pbSearchCity.setVisibility(View.GONE);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "get_city");
    }
}
