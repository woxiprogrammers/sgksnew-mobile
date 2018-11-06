package com.woxi.sgks_member.home;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.adapters.HomeViewPagerAdapter;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.interfaces.FragmentInterface;
import com.woxi.sgks_member.local_storage.DataSyncService;
import com.woxi.sgks_member.miscellaneous.AccountsActivity;
import com.woxi.sgks_member.miscellaneous.AddMeToSgksActivity;
import com.woxi.sgks_member.miscellaneous.MiscellaneousViewActivity;
import com.woxi.sgks_member.miscellaneous.SettingsActivity;
import com.woxi.sgks_member.miscellaneous.SuggestionActivity;
import com.woxi.sgks_member.utils.AppCommonMethods;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private static final String APP_DATABASE_CREATED = "notDatabaseCreated";
    private ViewPager mViewPager;
    private HomeViewPagerAdapter viewPagerAdapter;
    private AlertDialog alertDialog;
    private TextView mTvMemberCount;
    private LinearLayout mLL_MemberCount;
    private String TAG = "HomeActivity";
    private ArrayList<String> arrLocalMessageIds, arrMessageIds;
    private int intMessageTabIndex = 3;
    private boolean isMessageCountApplied = false;
    private TextView badgeMessageTab;
    private TextView badgeClassifiedTab;
    private ArrayList<String> arrLocalClassifiedIds, arrClassifiedIds;
    private int intClassifiedTabIndex = 4;
    private boolean isClassifiedCountApplied = false;
    private Toolbar toolbar;
    private FloatingActionButton mFabAddNewMember;
    private Spinner spLanguage;
    private ArrayList arrLanguage;
    private int intLanguageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_coordinator);
        mContext = HomeActivity.this;
        toolbar = findViewById(R.id.toolbar);
//        mTvMemberCount = toolbar.findViewById(R.id.tvMemberCount);
        mLL_MemberCount = findViewById(R.id.llMemberCount);
        setSupportActionBar(toolbar);
        setupActionBar();
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeViews();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_add_me_sgks:
                Intent intentAdd = new Intent(mContext, Verification.class);
                intentAdd.putExtra("activityType", getString(R.string.add_me_sgks));
                startActivity(intentAdd);
                break;
            case R.id.nav_suggestion:
                /*Intent intentSug = new Intent(mContext, SuggestionActivity.class);
                intentSug.putExtra("activityType", getString(R.string.suggestion_box));
                startActivity(intentSug);*/
                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_accounts:
                /*Intent intentAccount = new Intent(mContext, AccountsActivity.class);
                intentAccount.putExtra("activityType", getString(R.string.accounts));
                startActivity(intentAccount);*/
                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_contact_us:
                /*Intent intentCon = new Intent(mContext, MiscellaneousViewActivity.class);
                intentCon.putExtra("activityType", getString(R.string.contactUs));
                startActivity(intentCon);*/
                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_health_plus:
                /*Intent intentIntro = new Intent(mContext, MiscellaneousViewActivity.class);
                intentIntro.putExtra("activityType", getString(R.string.healthPlus));
                startActivity(intentIntro);*/
                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_help:
                /*Intent intentHelp = new Intent(mContext, MiscellaneousViewActivity.class);
                intentHelp.putExtra("activityType", getString(R.string.help));
                startActivity(intentHelp);*/
                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_privacy_policy:
                /*Intent intentPP = new Intent(mContext, MiscellaneousViewActivity.class);
                intentPP.putExtra("activityType", getString(R.string.privacyPolicy));
                startActivity(intentPP);*/
                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_qa:
                /*Intent intentQA = new Intent(mContext, MiscellaneousViewActivity.class);
                intentQA.putExtra("activityType", getString(R.string.q_and_a));
                startActivity(intentQA);*/
                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.app_settings:
                Intent intentSettings = new Intent(mContext, SettingsActivity.class);
                intentSettings.putExtra("activityType", getString(R.string.settings));
                startActivity(intentSettings);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Stop Data Sync Api
        HomeActivity.stopLocalStorageSyncService(getApplicationContext());
        new AppCommonMethods(getBaseContext()).LOG(0, TAG, "Destroyed DataSyncService");
    }

    public void setLanguageSpinner(){
        arrLanguage.add(0,R.string.english);
        arrLanguage.add(1,R.string.gujarati);
        ArrayAdapter<String> arrayAdapter = getStringArrayAdapter(arrLanguage);
        spLanguage.setAdapter(arrayAdapter);

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intLanguageId = parent.getSelectedItemPosition()+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayAdapter<String> getStringArrayAdapter(ArrayList<String> arrayList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorTextHint, null));
                } else {
                    tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorTextMain, null));
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initializeViews() {
        mViewPager =  findViewById(R.id.homeViewPager);
        TabLayout mTabLayout =  findViewById(R.id.tavLayout);
        mFabAddNewMember = findViewById(R.id.fabAddNewMember);
        spLanguage = findViewById(R.id.spLang);
        viewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), mContext);
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //To start Committee Home
        mViewPager.setCurrentItem(2);
        mFabAddNewMember.setVisibility(View.VISIBLE);
        mFabAddNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(mContext, Verification.class);
                intentAdd.putExtra("activityType", getString(R.string.add_me_sgks));
                startActivity(intentAdd);
            }
        });

        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position) {
                FragmentInterface fragment = (FragmentInterface) viewPagerAdapter.instantiateItem(mViewPager, position);
                Log.i("@@@", "onPageSelected: "+position);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
                if (position == intMessageTabIndex && isMessageCountApplied) {
                    badgeMessageTab.setVisibility(View.GONE);
                    try {
                        arrLocalMessageIds.addAll(arrMessageIds);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AppCommonMethods.putStringPref(AppConstants.PREFS_LOCAL_MESSAGE_ID, arrLocalMessageIds.toString(), mContext);
                }
                if (position == intClassifiedTabIndex && isClassifiedCountApplied) {
                    badgeClassifiedTab.setVisibility(View.GONE);
                    try {
                        arrLocalClassifiedIds.addAll(arrClassifiedIds);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AppCommonMethods.putStringPref(AppConstants.PREFS_LOCAL_CLASSIFIED_ID, arrLocalClassifiedIds.toString(), mContext);
                }
                if (position == 2){
                    mFabAddNewMember.setVisibility(View.VISIBLE);
                } else {
                    mFabAddNewMember.setVisibility(View.GONE);
                    mViewPager.setCurrentItem(2);
                    //Other tabs not in use for 1st app release
                    //remove other tabs are ready
                    if(position == 0){
                        new AppCommonMethods(mContext).showAlert("Events Comming Soon.....");
                    }
                    if(position == 1){
                        new AppCommonMethods(mContext).showAlert("Committies Comming Soon.....");
                    }
                    if(position == 3){
                        new AppCommonMethods(mContext).showAlert("Messages Comming Soon.....");
                    }
                    if(position == 4){
                        new AppCommonMethods(mContext).showAlert("Classiffieds Comming Soon.....");
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        boolean notDataBaseCreated = AppCommonMethods.getBooleanPref(APP_DATABASE_CREATED, mContext);
        if (!notDataBaseCreated) {
            //Local Storage Sync is enabled by-default.
            AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_LOCAL_STORAGE_SYNC_ENABLED, true, mContext);
            AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, true, mContext);
            AppCommonMethods.putBooleanPref(APP_DATABASE_CREATED, true, mContext);
        }
//        boolean isLocalStorageSyncEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_LOCAL_STORAGE_SYNC_ENABLED, mContext);
//        if (isLocalStorageSyncEnabled) {
//            if (new AppCommonMethods(mContext).isNetworkAvailable()) {
//                //Check if DataSyncService is already running
//                if (!isSyncServiceRunning(DataSyncService.class)) {
//                    syncLocalStorageSyncService(getApplicationContext());
//                } else new AppCommonMethods(mContext).LOG(0, TAG, "Sync Service Already Running");
//            } else {
//                new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.alert_sync_failed));
//            }
//        }
    }

    private boolean isSyncServiceRunning(Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void syncLocalStorageSyncService(Context applicationContext) {
        Intent intentSyncService = new Intent(applicationContext, DataSyncService.class);
        applicationContext.startService(intentSyncService);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(applicationContext);
        mBuilder.setSmallIcon(R.drawable.ic_sync);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setContentTitle(getString(R.string.notification_title_database_sync_in_progress));
        mBuilder.setContentText(getString(R.string.notification_detail_please_wait));
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 1, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setOngoing(true);
        // Builds the notification and issues it.
        NotificationManager mNotifyMgr = (NotificationManager) applicationContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(AppConstants.SYNC_NOTIFICATION_ID, mBuilder.build());
    }

    public static void stopLocalStorageSyncService(Context applicationContext) {
        AppController.getInstance().cancelPendingRequests(applicationContext.getString(R.string.tag_local_storage_sync));
        Intent intentSyncService = new Intent(applicationContext, DataSyncService.class);
        applicationContext.stopService(intentSyncService);
        NotificationManager mNotifyMgr = (NotificationManager) applicationContext.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(AppConstants.SYNC_NOTIFICATION_ID);
    }


}
