package com.woxi.sgkks_member.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.utils.AppSettings;

/**
 * <b>public class MemberDetailsActivity extends AppCompatActivity</b>
 * <p>This class is used to show Member Details</p>
 * Created by Rohit.
 */
public class MemberDetailsActivity extends AppCompatActivity {
    private Context mContext;
    private MemberDetailsItem memberDetailsItem;
    private String strMobileNUmber;
    String strFirstName ="";
    String strMiddleName="";
    String strLastName="";
    String strAddress;
    TextView tvAddress;
    TextView tvEmail;
    TextView tvMemCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        getSupportActionBar().setTitle(R.string.member_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Getting extras/string from previous with the key "order".
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("currentMemberDetail")) {
                memberDetailsItem = (MemberDetailsItem) bundle.getSerializable("currentMemberDetail");
            }
        }
        //Calling function to initialize required views.
        initializeViews();
    }

    private void initializeViews() {
        mContext = MemberDetailsActivity.this;
        tvAddress = findViewById(R.id.tvMemAddress);
        tvEmail = findViewById(R.id.tvMemEmail);
        tvMemCity = findViewById(R.id.tvMemCity);
        strMobileNUmber = memberDetailsItem.getStrMobileNumber();
        
        if (memberDetailsItem.getStrFirstName() != null) {
            strFirstName = memberDetailsItem.getStrFirstName();
        }
        if (memberDetailsItem.getStrMiddleName() != null) {
            strMiddleName = memberDetailsItem.getStrMiddleName();
        }
        if (memberDetailsItem.getStrLastName() != null) {
            strLastName = memberDetailsItem.getStrLastName();
        }
        final String strFullName = strFirstName + " " + strMiddleName + " " + strLastName;
        ((TextView) findViewById(R.id.tvMemName)).setText(strFullName);

        if (memberDetailsItem.getStrAddress() != null) {
            if (memberDetailsItem.getStrAddress() != null) {
                strAddress = memberDetailsItem.getStrAddress();
                tvAddress.setText(strAddress);
            }
        } else {
            tvAddress.setText("-");
        }


        if (memberDetailsItem.getStrMobileNumber() != null && !memberDetailsItem.getStrMobileNumber().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemContact)).setText(memberDetailsItem.getStrMobileNumber());
        } else {
            ((TextView) findViewById(R.id.tvMemContact)).setText("-");
        }
        if (memberDetailsItem.getStrEmail() != null && !memberDetailsItem.getStrEmail().equalsIgnoreCase("null")) {
            tvEmail.setText(memberDetailsItem.getStrEmail());
        } else {
            tvEmail.setVisibility(View.GONE);
        }
        if (memberDetailsItem.getStrBloodGroup() != null && !memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemBloodGroup)).setText(memberDetailsItem.getStrBloodGroup());
        } else {
            ((TextView) findViewById(R.id.tvMemBloodGroup)).setText("Do Not Know");
        }
        if(memberDetailsItem.getStrCity() != null && !memberDetailsItem.getStrCity().equalsIgnoreCase("null")){
            tvMemCity.setText(memberDetailsItem.getStrCity());
        } else {
            tvMemCity.setText("-");
        }
        /*if (memberDetailsItem.getStrId() != null && !memberDetailsItem.getStrId().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemMemberId)).setText("SGKS-" + memberDetailsItem.getMemSgksMainCity() + "-" + memberDetailsItem.getMemID());
        } else {
            ((TextView) findViewById(R.id.tvMemMemberId)).setText("-");
        }*/

        FloatingActionButton mFloatingLocation =  findViewById(R.id.memFloatingLocation);
        if (memberDetailsItem.getStrLatitude() != null && !memberDetailsItem.getStrLatitude().equalsIgnoreCase("null")) {
            final String strLatitude = memberDetailsItem.getStrLatitude();
            final String strLongitude = memberDetailsItem.getStrLongitude();
            mFloatingLocation.setVisibility(View.VISIBLE);
            mFloatingLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!strLatitude.equalsIgnoreCase("") && !strLongitude.equalsIgnoreCase("")) {
                        String uriBegin = "geo:" + strLongitude + "," + strLatitude;
                        String query = strLongitude + "," + strLatitude + "(" + strFullName + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uriLocation = Uri.parse(uriString);
                        final Intent intent = new Intent(Intent.ACTION_VIEW, uriLocation);
                        mContext.startActivity(intent);
                    }
                }
            });
        } else {
            mFloatingLocation.setVisibility(View.GONE);
        }

        FloatingActionButton mFloatingEdit = findViewById(R.id.memFloatingEdit);
        if (AppSettings.getStringPref(AppConstants.PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")){
            mFloatingEdit.setVisibility(View.GONE);
            mFloatingEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent verificationIntent = new Intent(mContext,VerificationActivity.class);
                    verificationIntent.putExtra("memberItems",memberDetailsItem);
                    verificationIntent.putExtra("activityType","EditProfile");
                    startActivity(verificationIntent);
                }
            });
        } else {
            mFloatingEdit.setVisibility(View.GONE);
        }


        //Loading member image from url.
        final ImageView mIvMemImage = ((ImageView) findViewById(R.id.ivMemDetImage));
        String strUrl = memberDetailsItem.getStrMemberImageUrl();
        mIvMemImage.setImageDrawable(null);
        if (strUrl != null && !strUrl.equalsIgnoreCase("")) {
            Glide.with(mContext)
                    .load(strUrl)
                    .asBitmap()
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_profile)
                    .into(new BitmapImageViewTarget(mIvMemImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mIvMemImage.setImageDrawable(circularBitmapDrawable);
                            mIvMemImage.setBackground(null);
                        }
                    });
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
}
