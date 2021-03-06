package com.woxi.sgkks_member.home;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

public class VerificationActivity extends AppCompatActivity {
    private Context mContext;
    private String strMobileNumber;
    private String strOtp;

    private LinearLayout llEnterMobileNumber;
    private EditText etMobileNumber;
    private EditText etOtp;
    private TextView tvErrorMessage;
    private TextView tvConfirmMobileNumber,tvVerificationFor,tvEtLable, tvOtpSent;
    private ProgressBar pbVerify;
    private MemberDetailsItem memberDetailsItem;
    private String strActivityType;
    private boolean isFromEdit = false, isOtpSent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.containsKey("memberItems")){
                memberDetailsItem = (MemberDetailsItem) bundle.getSerializable("memberItems");
            }
            if(bundle.containsKey("activityType")){
                strActivityType = bundle.getString("activityType");
                if(strActivityType.equalsIgnoreCase("EditProfile")){
                    isFromEdit = true;
                }
            }
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("SGKKS Moblie Number "+R.string.verification);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
    }

    public void initializeViews(){
        mContext = VerificationActivity.this;
        llEnterMobileNumber = findViewById(R.id.llEnterMobileNumber);
        //llEnterOtp = findViewById(R.id.llEnterOtp);
        tvEtLable = findViewById(R.id.etLable);
        tvEtLable.setText("Please Enter Your Mobile Number");
        pbVerify = findViewById(R.id.pbVerify);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        tvConfirmMobileNumber = findViewById(R.id.tvConfirmMobileNumber);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        tvVerificationFor = findViewById(R.id.verificationFor);
        tvOtpSent = findViewById(R.id.tvOtpSent);
        if(!isOtpSent){
            mobileNumberListener();
        }
        if(isFromEdit){
            String strMobile = memberDetailsItem.getStrMobileNumber();
            etMobileNumber.setText(memberDetailsItem.getStrMobileNumber());
            etMobileNumber.setEnabled(false);
            tvConfirmMobileNumber.setVisibility(View.VISIBLE);
            tvConfirmMobileNumber.setText("An OTP is sent on "+strMobile);
            tvVerificationFor.setText("Update Information");
            etOtp.setVisibility(View.VISIBLE);
            etMobileNumber.setVisibility(View.GONE);
        }
        etOtp = findViewById(R.id.etOtp);
        etOtp.setVisibility(View.GONE);
        tvVerificationFor.setText("Adding New Member");
        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence otp, int start, int before, int count) {
                if(otp.length() == 6){
                    strOtp = otp.toString();
                    tvErrorMessage.setVisibility(View.GONE);
                    if(new AppCommonMethods(mContext).isNetworkAvailable()){
                        verifyOtp();
                    } else {
                        new AppCommonMethods(mContext).showAlert("You are Offline");
                    }
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

    private void verifyOtp() {
        pbVerify.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("mobile_number", strMobileNumber);
            params.put("otp",strOtp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, AppURLs.API_VALIDATE_OTP, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                pbVerify.setVisibility(View.GONE);
                                new AppCommonMethods(mContext).LOG(0,"OTP_VERIFIED",response.toString());
                                if(response.has("message")){
                                        Intent addMemberIntent = new Intent(VerificationActivity.this, AddMeToSgksActivity.class);
                                        if(isFromEdit){
                                            addMemberIntent.putExtra("memberItems",memberDetailsItem);
                                            addMemberIntent.putExtra("activityType", "EditProfile");
                                        } else {
                                            addMemberIntent.putExtra("contactNumber",strMobileNumber);
                                            addMemberIntent.putExtra("activityType", getString(R.string.add_me_sgks));
                                        }
                                        startActivity(addMemberIntent);
                                }
                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pbVerify.setVisibility(View.GONE);
                        new AppCommonMethods(mContext).LOG(0,"OTP_NOT_VERIFIED",error.toString());
                        new AppCommonMethods(mContext).showAlert("You have entered a wrong OTP!");
                        etOtp.setText("");

                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "verify_OTP");
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
                                        isOtpSent = true;
                                        tvEtLable.setText("Enter OTP");
                                        etMobileNumber.setVisibility(View.GONE);
                                        etOtp.setVisibility(View.VISIBLE);
                                        tvOtpSent.setVisibility(View.VISIBLE);
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "send_OTP");
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void mobileNumberListener(){
        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence mobileNumber, int start, int before, int count) {
                if(mobileNumber.length() == 10){
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
    }
}
