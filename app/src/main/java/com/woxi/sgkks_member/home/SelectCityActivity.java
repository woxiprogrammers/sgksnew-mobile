package com.woxi.sgkks_member.home;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectCityActivity extends AppCompatActivity {
    private ProgressBar pbSearchCity;
    public static View.OnClickListener onCityClickListener;
    private EditText etSearchCity;
    private RecyclerView rvCityList;
    private RecyclerView.Adapter rvAdapter;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.select_city);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeViews(){
        mContext = SelectCityActivity.this;
        pbSearchCity = findViewById(R.id.pbCityListing);
        etSearchCity = findViewById(R.id.etSearchCity);
        rvCityList = findViewById(R.id.rvCityList);
    }

    private void requestCityList(){
        pbSearchCity.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_GET_CITY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                new AppCommonMethods(mContext).LOG(0, "get_city", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AppCommonMethods(mContext).LOG(0, "error_city", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "get_city");
    }
}
