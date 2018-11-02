package com.woxi.sgks_member.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.models.MemberDetailsItem;

/**
 * <b>public class MemberDetailsActivity extends AppCompatActivity</b>
 * <p>This class is used to show Member Details</p>
 * Created by Rohit.
 */
public class MemberDetailsActivity extends AppCompatActivity {
    private Context mContext;
    private MemberDetailsItem memberDetailsItem;
    private String strMobileNUmber;

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

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = MemberDetailsActivity.this;
        strMobileNUmber = memberDetailsItem.getMemMobile();
        String strFName = "", strMName = "", strSurname = "";
        if (memberDetailsItem.getMemFirstName() != null) {
            strFName = memberDetailsItem.getMemFirstName();
        }
        if (memberDetailsItem.getMemFirstName() != null) {
            strMName = memberDetailsItem.getMemMidName();
        }
        if (memberDetailsItem.getMemFirstName() != null) {
            strSurname = memberDetailsItem.getMemSurname();
        }
        final String strFullName = strFName + " " + strMName + " " + strSurname;
        ((TextView) findViewById(R.id.tvMemName)).setText(strFullName);


        String strAddLine = "", strArea = "", strCity = "", strPincode = "", strState = "", strCountry = "";
        if (memberDetailsItem.getMemAddress() != null) {
            if (memberDetailsItem.getMemAddress().getAddAddressLine() != null) {
                strAddLine = memberDetailsItem.getMemAddress().getAddAddressLine();
            }
            if (memberDetailsItem.getMemAddress().getAddAddressLine() != null) {
                strArea = memberDetailsItem.getMemAddress().getAddArea();
            }
            if (memberDetailsItem.getMemAddress().getAddAddressLine() != null) {
                strCity = memberDetailsItem.getMemAddress().getAddCity();
            }
            if (memberDetailsItem.getMemAddress().getAddAddressLine() != null) {
                strPincode = memberDetailsItem.getMemAddress().getAddPincode();
            }
            if (memberDetailsItem.getMemAddress().getAddAddressLine() != null) {
                strState = memberDetailsItem.getMemAddress().getAddState();
            }
            if (memberDetailsItem.getMemAddress().getAddAddressLine() != null) {
                strCountry = memberDetailsItem.getMemAddress().getAddCountry();
            }
            String strFullAddress = strAddLine + ", " + strArea + ", " + strCity + "-" + strPincode + ", " + strState + ", " + strCountry + ".";
            ((TextView) findViewById(R.id.tvMemAddress)).setText(strFullAddress);
        } else ((TextView) findViewById(R.id.tvMemAddress)).setText("-");


        if (memberDetailsItem.getMemMobile() != null && !memberDetailsItem.getMemMobile().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemContact)).setText(memberDetailsItem.getMemMobile());
        } else {
            ((TextView) findViewById(R.id.tvMemContact)).setText("-");
        }
        if (memberDetailsItem.getMemEmail() != null && !memberDetailsItem.getMemEmail().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemEmail)).setText(memberDetailsItem.getMemEmail());
        } else {
            ( findViewById(R.id.tvMemEmail)).setVisibility(View.GONE);
        }
        if (memberDetailsItem.getMemBloodGroup() != null && !memberDetailsItem.getMemBloodGroup().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemBloodGroup)).setText(memberDetailsItem.getMemBloodGroup());
        } else {
            ((TextView) findViewById(R.id.tvMemBloodGroup)).setText("Do Not Know");
        }
        if (memberDetailsItem.getMemMaritalStatus() != null && !memberDetailsItem.getMemMaritalStatus().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemMaritalStatus)).setText(memberDetailsItem.getMemMaritalStatus());
        } else {
            ((TextView) findViewById(R.id.tvMemMaritalStatus)).setText("-");
        }
        if (memberDetailsItem.getMemID() != null && !memberDetailsItem.getMemID().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemMemberId)).setText("SGKS-" + memberDetailsItem.getMemSgksMainCity() + "-" + memberDetailsItem.getMemID());
        } else {
            ((TextView) findViewById(R.id.tvMemMemberId)).setText("-");
        }
        if (memberDetailsItem.getMemSgksFamilyId() != null && !memberDetailsItem.getMemSgksFamilyId().equalsIgnoreCase("null")) {
            ((TextView) findViewById(R.id.tvMemFamilyId)).setText("SGKS-" + memberDetailsItem.getMemSgksMainCity() + "-" + memberDetailsItem.getMemSgksFamilyId());
        } else {
            ((TextView) findViewById(R.id.tvMemFamilyId)).setText("-");
        }

        FloatingActionButton mFloatingLocation =  findViewById(R.id.memFloatingLocation);
        if (memberDetailsItem.getMemLatitude() != null && !memberDetailsItem.getMemLatitude().equalsIgnoreCase("null")) {
            final String strLatitude = memberDetailsItem.getMemLatitude();
            final String strLongitude = memberDetailsItem.getMemLongitude();
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
        mFloatingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verificationIntent = new Intent(MemberDetailsActivity.this,Verification.class);
                verificationIntent.putExtra("activityType","MemberDetailsActivity");
                startActivity(verificationIntent);
            }
        });

        //Loading member image from url.
        final ImageView mIvMemImage = ((ImageView) findViewById(R.id.ivMemDetImage));
        String strUrl = memberDetailsItem.getMemberImageURL();
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
