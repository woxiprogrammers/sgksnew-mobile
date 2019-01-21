package com.woxi.sgkks_member.home;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.adapters.AccountAndEventDetailsAdapter;
import com.woxi.sgkks_member.models.ClassifiedDetailsItem;
import com.woxi.sgkks_member.models.EventDataItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.ImageZoomDialogFragment;

import java.io.File;
import java.util.ArrayList;

import static com.woxi.sgkks_member.utils.ImageZoomDialogFragment.strFinalImageUrl;

/**
 * <b>public class EventAndClassifiedDetailActivity extends AppCompatActivity</b>
 * <p>This class is used to show Images of Event in Grid</p>
 * Created by Rohit.
 */
public class EventAndClassifiedDetailActivity extends AppCompatActivity {
    public static View.OnClickListener onGridImageClickListener;
    private RecyclerView mRvEventGallery;
    private ArrayList<String> arrEventImageUrls;
    private Context mContext;
    private boolean isFromEventHome;
    private boolean isFromClassifiedHome;
    private BroadcastReceiver onDownloadComplete;
    private String TAG = "EventAndClassifiedDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mContext = EventAndClassifiedDetailActivity.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("eventDetails")) {
                isFromEventHome = true;
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.event_details_title));
                }
                EventDataItem eventDataItem = (EventDataItem) bundle.getSerializable("eventDetails");
                //call function to setup screen for event details screen
                functionForEventDetails(eventDataItem);
            } else if (bundle.containsKey("currentClassifiedDetail")) {
                isFromClassifiedHome = true;
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(getString(R.string.classified_details_title));
                }
                ClassifiedDetailsItem mClassifiedDetailsItem = (ClassifiedDetailsItem) bundle.getSerializable("currentClassifiedDetail");
                //call function to setup screen for classified details screen
                functionForClassifiedDetails(mClassifiedDetailsItem);
            }
        }
        //Function for event image click listener
        onGridImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View selectedView) {
                if (isFromEventHome) {
                    String strImageUrl = arrEventImageUrls.get(mRvEventGallery.getChildAdapterPosition(selectedView));
                    ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(strImageUrl);
                    imageZoomDialogFragment.setCancelable(true);
                    imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
                } else if (isFromClassifiedHome) {
                    String strImageUrl = arrEventImageUrls.get(mRvEventGallery.getChildAdapterPosition(selectedView));
                    ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(true, strImageUrl);
                    imageZoomDialogFragment.setCancelable(true);
                    imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
                }
            }
        };
    }

    private void functionForClassifiedDetails(ClassifiedDetailsItem mClassifiedDetailsItem) {
        TextView tvEventVenue = findViewById(R.id.tvEventVenue);
        tvEventVenue.setVisibility(View.GONE);
        String strTitle = mClassifiedDetailsItem.getClassifiedTitle();
        if (strTitle != null) {
            ((TextView) findViewById(R.id.tvEventAndClassifiedName)).setText(strTitle);
        }
        arrEventImageUrls = mClassifiedDetailsItem.getArrClassifiedImages();
        //Set-up RecyclerView
        mRvEventGallery = findViewById(R.id.rvEventAndClassifiedGallery);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRvEventGallery.setLayoutManager(layoutManager);
        AccountAndEventDetailsAdapter eventAndAccountDetailsAdapter = new AccountAndEventDetailsAdapter(true, arrEventImageUrls);
        mRvEventGallery.setAdapter(eventAndAccountDetailsAdapter);
        String strEventDescription = mClassifiedDetailsItem.getClassifiedDescription();
        TextView tvEventDescription = findViewById(R.id.tvEventAndClassifiedDescription);
        if (strEventDescription != null && !strEventDescription.equalsIgnoreCase("")) {
            tvEventDescription.setVisibility(View.VISIBLE);
            tvEventDescription.setText(strEventDescription);
        } else {
            tvEventDescription.setVisibility(View.GONE);
        }
    }

    private void functionForEventDetails(EventDataItem eventDataItem) {
        if (eventDataItem != null) {
            String strTitle = eventDataItem.getEventName();
            if (strTitle != null) {
                ((TextView) findViewById(R.id.tvEventAndClassifiedName)).setText(strTitle);
            }
            arrEventImageUrls = eventDataItem.getArrEventImageURLs();
            //Set-up RecyclerView
            mRvEventGallery = findViewById(R.id.rvEventAndClassifiedGallery);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            mRvEventGallery.setLayoutManager(layoutManager);
            AccountAndEventDetailsAdapter eventAndAccountDetailsAdapter = new AccountAndEventDetailsAdapter(true, arrEventImageUrls);
            mRvEventGallery.setAdapter(eventAndAccountDetailsAdapter);
            //Set Description
            String strEventDescription = eventDataItem.getEventDescription();
            TextView tvEventDescription = findViewById(R.id.tvEventAndClassifiedDescription);
            if (strEventDescription != null && !strEventDescription.equalsIgnoreCase("")) {
                tvEventDescription.setVisibility(View.VISIBLE);
                tvEventDescription.setText(strEventDescription);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvEventDescription.setElevation(5);
                }
            } else {
                tvEventDescription.setVisibility(View.GONE);
            }
            //Set Venue
            String strEventVenue = eventDataItem.getVenue();
            TextView tvEventVenue = findViewById(R.id.tvEventVenue);
            tvEventVenue.setVisibility(View.VISIBLE);
            if (strEventVenue != null && !strEventVenue.equalsIgnoreCase("")) {
                tvEventVenue.setVisibility(View.VISIBLE);
                tvEventVenue.setText("Venue: "+strEventVenue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tvEventVenue.setElevation(5);
                }
            } else {
                tvEventVenue.setVisibility(View.GONE);
            }
        }
    }

    /**
     * public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
     * To check if storage permission is granted or denied.
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults
     *                     Created By Rohit
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Download Started", Toast.LENGTH_SHORT).show();
                downloadImageFromUrl(strFinalImageUrl);
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(EventAndClassifiedDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                    Toast.makeText(mContext, getResources().getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(mContext, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void downloadImageFromUrl(String strFinalImageUrl) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/SGKS Member");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(strFinalImageUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("SGKS Member")
                .setDescription("Event Image")
                .setDestinationInExternalPublicDir("/SGKS Member", "sgks event.jpg");
        downloadManager.enqueue(request);
        onDownloadComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "Image Downloaded", Toast.LENGTH_SHORT).show();
                unregisterReceiver(onDownloadComplete);
            }
        };
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
