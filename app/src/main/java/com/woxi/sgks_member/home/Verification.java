package com.woxi.sgks_member.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.woxi.sgks_member.R;
import com.woxi.sgks_member.miscellaneous.AddMeToSgksActivity;

public class Verification extends AppCompatActivity {
    private String strMobileNumber;
    private String strOtp;

    private LinearLayout llEnterMobileNumber;
    private LinearLayout llEnterOtp;
    private EditText etMobileNumber;
    private EditText etOtp;
    private TextView tvErrorMessage;

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
        llEnterMobileNumber = findViewById(R.id.llEnterMobileNumber);
        llEnterOtp = findViewById(R.id.llEnterOtp);
        llEnterOtp.setVisibility(View.GONE);
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
                    sendMobileNumberToServer();
                    llEnterMobileNumber.setVisibility(View.GONE);
                    llEnterOtp.setVisibility(View.VISIBLE);
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
    }
}
