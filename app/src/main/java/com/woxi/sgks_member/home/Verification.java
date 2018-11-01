package com.woxi.sgks_member.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

public class Verification extends AppCompatActivity {
    private Context mContext;
    private String strMobileNumber;
    private String strOtp;

    private LinearLayout llEnterMobileNumber;
    private LinearLayout llEnterOtp;
    private EditText etMobileNumber;
    private EditText etOtp;
    private TextView tvErrorMessage;
    private ProgressBar pbVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.verification);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
    }

    public void initializeViews(){
        mContext = Verification.this;
        llEnterMobileNumber = findViewById(R.id.llEnterMobileNumber);
        llEnterOtp = findViewById(R.id.llEnterOtp);
        llEnterOtp.setVisibility(View.GONE);
        pbVerify = findViewById(R.id.pbVerify);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence mobileNumber, int start, int before, int count) {
                if(mobileNumber.length() ==10){
                    tvErrorMessage.setVisibility(View.GONE);
                    strMobileNumber = mobileNumber.toString();
                    if(new AppCommonMethods(mContext).isNetworkAvailable()){
                        sendMobileNumberToServer();

                    } else {
                        new AppCommonMethods(mContext).showAlert("You are Offline");
                    }
                } else {
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText("Please enter a valid Mobile Number");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etOtp = findViewById(R.id.etOtp);
        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence otp, int start, int before, int count) {
                if(otp.length() == 6){
                    tvErrorMessage.setVisibility(View.GONE);
                    strOtp = otp.toString();
                    sendOtpToServer();
                } else {
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText("Please enter a valid 6 digit OTP");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendOtpToServer() {
        verifyOtp();
    }

    private void verifyOtp() {
        if(true){
            Intent addMemberIntent = new Intent(Verification.this, AddMeToSgksActivity.class);
            addMemberIntent.putExtra("activityType", getString(R.string.add_me_sgks));
            startActivity(addMemberIntent);
        } else {
            Toast.makeText(Verification.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
        }

    }

    private void sendMobileNumberToServer() {
        pbVerify.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("mobile_number", strMobileNumber);
        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, AppURLs.API_SEND_OTP, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                pbVerify.setVisibility(View.GONE);
                                new AppCommonMethods(mContext).LOG(0,"GET_OTP",response.toString());
                                if (response.has("message")) {
                                    try {
                                        llEnterMobileNumber.setVisibility(View.GONE);
                                        llEnterOtp.setVisibility(View.VISIBLE);
                                        Toast.makeText(mContext, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbVerify.setVisibility(View.GONE);
                        new AppCommonMethods(mContext).LOG(0,"GET_OTP",error.toString());
                        Toast.makeText(mContext, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "sendOTP");

    }
}
