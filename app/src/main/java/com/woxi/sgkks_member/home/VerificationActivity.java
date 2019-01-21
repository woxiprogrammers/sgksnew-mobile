package com.woxi.sgkks_member.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
    private Button btnMobileNumber;
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
        btnMobileNumber = findViewById(R.id.btnSendMobileNo);
        etOtp = findViewById(R.id.etOtp);
        btnMobileNumber.setText("SEND OTP");
        if(!isOtpSent){
            mobileNumberListener();
        }
        if(isFromEdit){
            String strMobile = memberDetailsItem.getStrMobileNumber();
            etMobileNumber.setText(memberDetailsItem.getStrMobileNumber());
            etMobileNumber.setEnabled(false);
            tvConfirmMobileNumber.setVisibility(View.VISIBLE);
            tvVerificationFor.setText("Update Information");
            tvEtLable.setVisibility(View.GONE);
            tvConfirmMobileNumber.setText("An OTP will be sent on "+strMobile);
            etOtp.setVisibility(View.GONE);
            etMobileNumber.setVisibility(View.GONE);
        } else {
            // etOtp = findViewById(R.id.etOtp);
            etOtp.setVisibility(View.GONE);
            tvVerificationFor.setText("Adding New Member");
        }
        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence otp, int start, int before, int count) {
                strOtp = otp.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnNextListener();
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
                                    onBackPressed();
                                    finish();
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
                                        btnMobileNumber.setClickable(true);
                                        isOtpSent = true;
                                        tvConfirmMobileNumber.setVisibility(View.GONE);
                                        tvEtLable.setText("Enter OTP");
                                        etMobileNumber.setVisibility(View.GONE);
                                        etOtp.setVisibility(View.VISIBLE);
                                        tvOtpSent.setVisibility(View.VISIBLE);
                                        btnMobileNumber.setText("VERIFY OTP");
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
                        btnMobileNumber.setClickable(true);
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

    public void mobileNumberListener() {
        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence mobileNumber, int start, int before, int count) {
                tvErrorMessage.setVisibility(View.GONE);
                strMobileNumber = mobileNumber.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void btnNextListener () {
        btnMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOtpSent) {
                    btnMobileNumber.setClickable(false);
                    if (strOtp != null && !strOtp.equalsIgnoreCase("null")){
                        if (strOtp.length() == 6) {
                            tvErrorMessage.setVisibility(View.GONE);
                            if(new AppCommonMethods(mContext).isNetworkAvailable()){
                                verifyOtp();
                            } else {
                                new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                            }
                        }else {
                            tvErrorMessage.setVisibility(View.VISIBLE);
                            tvErrorMessage.setText("Please enter a valid OTP");
                            btnMobileNumber.setClickable(false);
                        }
                        btnMobileNumber.setClickable(true);
                    } else {
                        tvErrorMessage.setVisibility(View.VISIBLE);
                        tvErrorMessage.setText("Please enter a valid OTP");
                        btnMobileNumber.setClickable(true);
                    }
                } else {
                    btnMobileNumber.setClickable(false);
                    if (strMobileNumber != null && !strMobileNumber.equalsIgnoreCase("null")) {
                        if (strMobileNumber.length() == 10) {
                            if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                                sendMobileNumberToServer();
                            } else {
                                new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                                btnMobileNumber.setClickable(true);
                            }
                        } else {
                            btnMobileNumber.setClickable(true);
                            tvErrorMessage.setVisibility(View.VISIBLE);
                            tvErrorMessage.setText("Please enter a valid Mobile Number");
                        }
                    } else {
                        btnMobileNumber.setClickable(true);
                        tvErrorMessage.setVisibility(View.VISIBLE);
                        tvErrorMessage.setText("Please enter a valid Mobile Number");
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
