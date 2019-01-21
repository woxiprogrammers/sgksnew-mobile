package com.woxi.sgkks_member.home;

import android.app.Activity;
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
import com.woxi.sgkks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgkks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgkks_member.models.CityIteam;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    boolean isFromCreateMember = false;
    private DatabaseQueryHandler databaseQueryHandler;
    //private ArrayList<CityIteam> arrOfflineCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle;
        bundle = getIntent().getExtras();{
            if (bundle != null){
                if( bundle.containsKey("isFromCreateMember")){
                    isFromCreateMember = true;
                }
            }
        }
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
        databaseQueryHandler = new DatabaseQueryHandler(mContext,false);
        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 2){
                    strSearchCity = charSequence.toString();
                } else {
                    strSearchCity = "";
                }
                if(new AppCommonMethods(mContext).isNetworkAvailable()){
                    requestCityList();
                } else {
                    if (charSequence.length() >= 2){
                        strSearchCity = charSequence.toString();
                        arrCityList = databaseQueryHandler.queryCities(strSearchCity);
                        setupCityList(arrCityList);
                        pbSearchCity.setVisibility(View.GONE);
                    } else {
                        strSearchCity = charSequence.toString();
                        arrCityList = databaseQueryHandler.queryCities("");
                        setupCityList(arrCityList);
                        pbSearchCity.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setUpRecyclerView();
        if(new AppCommonMethods(mContext).isNetworkAvailable()){
            requestCityList();
        } else {
            arrCityList = databaseQueryHandler.queryCities("");
            setupCityList(arrCityList);
            pbSearchCity.setVisibility(View.GONE);
        }

    }

    private void restartActivity(Context context) {
        try {
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
            params.put("language_id",AppCommonMethods.getStringPref(AppConstants.PREFS_LANGUAGE_APPLIED,mContext));
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
                        if(arrCityList.size() != 0){
                            setupCityList(arrCityList);
                        } else {
                            setupCityList(arrCityList);
                            Toast.makeText(mContext,"No Records Found",Toast.LENGTH_SHORT).show();
                        }
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

    private void setUpRecyclerView(){
        rvCityList = findViewById(R.id.rvCityList);
        linearLayoutManager = new LinearLayoutManager(mContext);
        rvCityList.setLayoutManager(linearLayoutManager);
        onCityClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isFromCreateMember) {
                    CityIteam cityIteam = arrCityList.get(rvCityList.getChildLayoutPosition(v));
                    String strCityName = cityIteam.getStrCityName();
                    String strCityId = String.valueOf(cityIteam.getIntCityId());
                    Intent intent = new Intent();
                    intent.putExtra("cityName", strCityName);
                    intent.putExtra("cityId", strCityId);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    CityIteam cityIteam = arrCityList.get(rvCityList.getChildLayoutPosition(v));
                    String strCityName = cityIteam.getStrCityName();
                    String strCityId = String.valueOf(cityIteam.getIntCityId());
                    String strCityNameGujarati;
                    if (cityIteam.getStrCityNameGujarati() != null && !cityIteam.getStrCityNameGujarati().equalsIgnoreCase("null")){
                        strCityNameGujarati = cityIteam.getStrCityNameGujarati();
                    } else {
                        strCityNameGujarati = cityIteam.getStrCityNameEnglish();
                    }
                    String strCityNameEnglish = cityIteam.getStrCityNameEnglish();
                    AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_CITY_CHANGED, true, mContext);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(AppConstants.PREFS_CURRENT_CITY, strCityId);
                    editor.putString(AppConstants.PREFS_CITY_NAME, strCityName);
                    editor.putString(AppConstants.PREFS_CITY_NAME_GJ,strCityNameGujarati);
                    editor.putString(AppConstants.PREFS_CITY_NAME_EN,strCityNameEnglish);
                    editor.apply();
                    restartActivity(mContext);
                }
            }
        };

    }

    private void setupCityList(ArrayList<CityIteam> arrCityItem){
        rvCityList.setHasFixedSize(true);
        rvAdapter = new CityAdapter(arrCityItem);
        rvCityList.setAdapter(rvAdapter);
    }
}
