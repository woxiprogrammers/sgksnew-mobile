package com.woxi.sgkks_member.home;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.adapters.HomeViewPagerAdapter;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.interfaces.FragmentInterface;
import com.woxi.sgkks_member.local_storage.DataSyncService;
import com.woxi.sgkks_member.local_storage.DatabaseQueryHandler;
import com.woxi.sgkks_member.miscellaneous.AccountsActivity;
import com.woxi.sgkks_member.miscellaneous.MiscellaneousViewActivity;
import com.woxi.sgkks_member.miscellaneous.SettingsActivity;
import com.woxi.sgkks_member.miscellaneous.SuggestionActivity;
import com.woxi.sgkks_member.models.CountItem;
import com.woxi.sgkks_member.models.MasterItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppSettings;
import com.woxi.sgkks_member.utils.AppURLs;
import com.woxi.sgkks_member.utils.ImageZoomDialogFragment;
import com.woxi.sgkks_member.utils.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.woxi.sgkks_member.interfaces.AppConstants.CURRENT_PAGE;
import static com.woxi.sgkks_member.interfaces.AppConstants.LANGUAGE_ENGLISH;
import static com.woxi.sgkks_member.interfaces.AppConstants.LANGUAGE_GUJURATI;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CITY_NAME_EN;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CITY_NAME_GJ;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_IS_LANGUAGE_CHANGED;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LANGUAGE_APPLIED;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CITY_NAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private static final String APP_DATABASE_CREATED = "notDatabaseCreated";
    private ViewPager mViewPager;
    private HomeViewPagerAdapter viewPagerAdapter;
    private AlertDialog alertDialog;
    private TextView mTvMemberCount, tvLableCityName;
    private LinearLayout mLL_MemberCount;
    private String TAG = "HomeActivity";
    private ArrayList<String> arrLocalMessageIds, arrMessageIds;
    private int intMessageTabIndex = 3;
    private boolean isMessageCountApplied = false;
    private TextView badgeMessageTab;
    private TextView badgeClassifiedTab;
    private ArrayList<String> arrLocalClassifiedIds, arrClassifiedIds;
    private int intClassifiedTabIndex = 4;
    private boolean isClassifiedCountApplied = false, isFromSplash=false;
    private Toolbar toolbar;
    private FloatingActionButton mFabAddNewMember, mFabMessageInfo;
    private ImageView ivLanguage, ivCity;
    int intMessageCount = 0,intClassifiedCount = 0,intBuzzId;
    private String strBuzzImageUrl;
    private  MasterItem masterItem = new MasterItem();
    private TabLayout mTabLayout;
    private DatabaseQueryHandler databaseQueryHandler, databaseQueryHandlerWrite;
    private CountItem countItem = new CountItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_coordinator);
        mContext = HomeActivity.this;
        toolbar = findViewById(R.id.toolbar);
        String lang = AppSettings.getStringPref(PREFS_LANGUAGE_APPLIED, mContext);
        Bundle extras = getIntent().getBundleExtra("bundleHome");
        boolean isFromLanguage = false;
        if (extras != null) {
            if (extras.containsKey("isFromLanguage")) {
                isFromLanguage = extras.getBoolean("isFromLanguage");
            }
            if(extras.containsKey("isFromSplash")){
                isFromSplash = extras.getBoolean("isFromSplash");
            }
        }
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
                if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                    Intent intentAdd = new Intent(mContext, VerificationActivity.class);
                    intentAdd.putExtra("activityType", getString(R.string.add_me_sgks));
                    startActivity(intentAdd);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
                // new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_suggestion:
                if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                    Intent intentSug = new Intent(mContext, SuggestionActivity.class);
                    intentSug.putExtra("activityType", getString(R.string.suggestion_box));
                    startActivity(intentSug);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
//                new AppCommonMethods(mContext).showAlert("In Progress");
                break;
            case R.id.nav_accounts:
                    Intent intentAccount = new Intent(mContext, AccountsActivity.class);
                    intentAccount.putExtra("activityType", getString(R.string.accounts));
                    startActivity(intentAccount);
                break;
            case R.id.nav_contact_us:
                if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                    Intent intentCon = new Intent(mContext, MiscellaneousViewActivity.class);
                    intentCon.putExtra("activityType", getString(R.string.contactUs));
                    startActivity(intentCon);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
                break;
            case R.id.nav_health_plus:
                if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                    Intent intentIntro = new Intent(mContext, MiscellaneousViewActivity.class);
                    intentIntro.putExtra("activityType", getString(R.string.healthPlus));
                    startActivity(intentIntro);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
                break;
            case R.id.nav_help:
                if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                    Intent intentHelp = new Intent(mContext, MiscellaneousViewActivity.class);
                    intentHelp.putExtra("activityType", getString(R.string.help));
                    startActivity(intentHelp);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
                break;
            case R.id.nav_privacy_policy:
                if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                    Intent intentPP = new Intent(mContext, MiscellaneousViewActivity.class);
                    intentPP.putExtra("activityType", getString(R.string.privacyPolicy));
                    startActivity(intentPP);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
                break;
            case R.id.nav_qa:
                if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                    Intent intentQA = new Intent(mContext, MiscellaneousViewActivity.class);
                    intentQA.putExtra("activityType", getString(R.string.q_and_a));
                    startActivity(intentQA);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
                break;
            case R.id.app_settings:
                Intent intentSettings = new Intent(mContext, SettingsActivity.class);
                intentSettings.putExtra("activityType", getString(R.string.settings));
                startActivity(intentSettings);
                //new AppCommonMethods(mContext).showAlert("In Progress");
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

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initializeViews() {
        if (new AppCommonMethods(mContext).isNetworkAvailable()){
            requestMasterApi();
        }
        databaseQueryHandler = new DatabaseQueryHandler(mContext,false);
        databaseQueryHandlerWrite = new DatabaseQueryHandler(mContext,true);

        tvLableCityName = findViewById(R.id.tvLableCityName);
        tvLableCityName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        setupCityNameInHeader();
        mViewPager =  findViewById(R.id.homeViewPager);
        mTabLayout =  findViewById(R.id.tavLayout);
        mFabAddNewMember = findViewById(R.id.fabAddNewMember);
        mFabMessageInfo = findViewById(R.id.fabMessageInfo);
        viewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), mContext);
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        ivLanguage = findViewById(R.id.ivLanguage);
        ivLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLangChangeDialog();
            }
        });
        ivCity = findViewById(R.id.ivCitySelect);
        ivCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCommonMethods.putStringPref(CURRENT_PAGE,String.valueOf(mViewPager.getCurrentItem()),mContext);
                Intent intentSelectCity = new Intent(mContext, SelectCityActivity.class);
                startActivity(intentSelectCity);
            }
        });
        if(isFromSplash){
            mViewPager.setCurrentItem(2);
        } else {
            if(AppCommonMethods.getStringPref(CURRENT_PAGE,mContext).equalsIgnoreCase("") || AppCommonMethods.getStringPref(CURRENT_PAGE,mContext).equalsIgnoreCase("2")){
                mViewPager.setCurrentItem(2);
                mFabAddNewMember.setVisibility(View.VISIBLE);
                mFabMessageInfo.setVisibility(View.GONE);
            } else if(AppCommonMethods.getStringPref(CURRENT_PAGE,mContext).equalsIgnoreCase("3")){
                mViewPager.setCurrentItem(3);
                mFabMessageInfo.setVisibility(View.VISIBLE);
                mFabAddNewMember.setVisibility(View.GONE);
            } else {
                mFabAddNewMember.setVisibility(View.GONE);
                mFabMessageInfo.setVisibility(View.GONE);
                mViewPager.setCurrentItem(Integer.valueOf(AppCommonMethods.getStringPref(CURRENT_PAGE,mContext)));
            }
        }
        mFabAddNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new AppCommonMethods(mContext).isNetworkAvailable()){
                    Intent intentAdd = new Intent(mContext, VerificationActivity.class);
                    intentAdd.putExtra("activityType", getString(R.string.add_me_sgks));
                    startActivity(intentAdd);
                } else {
                    new AppCommonMethods(mContext).showAlert(getString(R.string.noInternet));
                }
            }
        });

        mFabMessageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppCommonMethods.putStringPref(CURRENT_PAGE,String.valueOf(mViewPager.getCurrentItem()),mContext);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_message_info, null);
                builder.setView(view);
                TextView tvBuzz, tvDukhadNidhan, tvBirthday, tvAchievement, tvGeneral, tvDialogTitle;
                tvDialogTitle = view.findViewById(R.id.dialog_title);
                tvBuzz = view.findViewById(R.id.tvBuzzInfo);
                tvDukhadNidhan = view.findViewById(R.id.tvDukhadNidhanInfo);
                tvBirthday = view.findViewById(R.id.tvBirthdayInfo);
                tvAchievement = view.findViewById(R.id.tvAchievementInfo);
                tvGeneral = view.findViewById(R.id.tvGeneralInfo);
                if(AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")){
                    tvDialogTitle.setText(R.string.dialog_title_en);
                    tvBuzz.setText(R.string.buzz_en);
                    tvDukhadNidhan.setText(R.string.dukhad_nidhan_en);
                    tvBirthday.setText(R.string.birthday_en);
                    tvAchievement.setText(R.string.achievement_en);
                    tvGeneral.setText(R.string.general_en);
                } else if(AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")){
                    tvDialogTitle.setText(R.string.dialog_title_gj);
                    tvBuzz.setText(R.string.buzz_gj);
                    tvDukhadNidhan.setText(R.string.dukhad_nidhan_gj);
                    tvBirthday.setText(R.string.birthday_gj);
                    tvAchievement.setText(R.string.achievement_gj);
                    tvGeneral.setText(R.string.general_gj);
                }
                final android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
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
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
                if (position == intMessageTabIndex && isMessageCountApplied) {
                    badgeMessageTab.setVisibility(View.GONE);
                    databaseQueryHandlerWrite.updateMessageCount(0,AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext));
                    try {
                        arrLocalMessageIds.addAll(arrMessageIds);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    AppCommonMethods.putStringPref(AppConstants.PREFS_LOCAL_MESSAGE_ID, arrLocalMessageIds.toString(), mContext);
                }
                if (position == intClassifiedTabIndex && isClassifiedCountApplied) {
                    badgeClassifiedTab.setVisibility(View.GONE);
                    databaseQueryHandlerWrite.updateClassifiedCount(0,AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext));
                    try {
                        arrLocalClassifiedIds.addAll(arrClassifiedIds);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    AppCommonMethods.putStringPref(AppConstants.PREFS_LOCAL_CLASSIFIED_ID, arrLocalClassifiedIds.toString(), mContext);
                }
                if (position == 2){
                    mFabAddNewMember.setVisibility(View.VISIBLE);
                    mFabMessageInfo.setVisibility(View.GONE);
                } else if (position == 3){
                    mFabAddNewMember.setVisibility(View.GONE);
                    mFabMessageInfo.setVisibility(View.VISIBLE);
                } else {
                    mFabAddNewMember.setVisibility(View.GONE);
                    mFabMessageInfo.setVisibility(View.GONE);
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

        boolean isLocalStorageSyncEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_LOCAL_STORAGE_SYNC_ENABLED, mContext);
        if (isLocalStorageSyncEnabled) {
            if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                //Check if DataSyncService is already running
                if (!isSyncServiceRunning(DataSyncService.class)) {
                    syncLocalStorageSyncService(getApplicationContext());
                } else new AppCommonMethods(mContext).LOG(0, TAG, "Sync Service Already Running");
            } else {
                new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.alert_sync_failed));
            }
        }
        try {
            countItem = databaseQueryHandler.queryCount(AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void showLangChangeDialog(){
        AppCommonMethods.putStringPref(CURRENT_PAGE,String.valueOf(mViewPager.getCurrentItem()),mContext);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_language, null);
        builder.setView(view);
        final TextView tvValueGujurati, tvValueEnglish;
        tvValueGujurati = (TextView) view.findViewById(R.id.tvGujuratiLanguage);
        tvValueEnglish = (TextView) view.findViewById(R.id.tvEnglishLanguage);
        final String lang = AppSettings.getStringPref(PREFS_LANGUAGE_APPLIED, mContext);
        if (lang.equals(LANGUAGE_ENGLISH)) {
            tvValueEnglish.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlueDark));
            tvValueGujurati.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
        } else if (lang.equals(LANGUAGE_GUJURATI)) {
            tvValueEnglish.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
            tvValueGujurati.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlueDark));
        }
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        tvValueGujurati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lang.equals(LANGUAGE_ENGLISH)) {
                    tvValueEnglish.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
                    tvValueGujurati.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlueDark));
                    mContext = LocaleHelper.onAttach(mContext, LANGUAGE_GUJURATI);
                    AppCommonMethods.putBooleanPref(PREFS_IS_LANGUAGE_CHANGED,true,mContext);
                    restartActivity(mContext);

                }
                dialog.dismiss();
            }
        });
        //TODO: Avoid this repetitive api calls
        tvValueEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lang.equals(LANGUAGE_GUJURATI)) {
                    tvValueEnglish.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlueDark));
                    tvValueGujurati.setTextColor(ContextCompat.getColor(mContext, R.color.colorBlack));
                    mContext = LocaleHelper.onAttach(mContext, LANGUAGE_ENGLISH);
                    AppCommonMethods.putBooleanPref(PREFS_IS_LANGUAGE_CHANGED,true,mContext);
                    restartActivity(mContext);
                }
                dialog.dismiss();
            }
        });

    }

    private void restartActivity(Context context) {
        try {
            Intent intentHome = getIntent();
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    private void setClassifiedCount(TabLayout mTabLayout) {
        View view = LayoutInflater.from(this).inflate(R.layout.badge_layout_tab_count, null);
        badgeClassifiedTab = (TextView) view.findViewById(R.id.tvTabBadge);
        ImageView ivTabIcon = (ImageView) view.findViewById(R.id.ivTabIcon);
        ivTabIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_classified, null));
        intClassifiedCount = countItem.getIntClassifiedCount();
        if(intClassifiedCount != 0){
            badgeClassifiedTab.setText(String.valueOf(intClassifiedCount));
        } else {
            badgeClassifiedTab.setVisibility(View.GONE);
        }
        mTabLayout.getTabAt(intClassifiedTabIndex).setCustomView(view);
        isClassifiedCountApplied = true;
    }

    private void setMessageCount(TabLayout mTabLayout) {
        View view = LayoutInflater.from(this).inflate(R.layout.badge_layout_tab_count, null);
        badgeMessageTab = (TextView) view.findViewById(R.id.tvTabBadge);
        ImageView ivTabIcon = (ImageView) view.findViewById(R.id.ivTabIcon);
        ivTabIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_messages, null));
        intMessageCount = countItem.getIntMessageCount();
        if(intMessageCount != 0){
            badgeMessageTab.setText(String.valueOf(intMessageCount));
        } else {
            badgeMessageTab.setVisibility(View.GONE);
        }

        mTabLayout.getTabAt(intMessageTabIndex).setCustomView(view);
        isMessageCountApplied = true;
    }

    private void setupCityNameInHeader(){
        if(AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("1")){
            tvLableCityName.setText(AppCommonMethods.getStringPref(PREFS_CITY_NAME_EN,mContext).toUpperCase());
        }
        else if (AppCommonMethods.getStringPref(PREFS_LANGUAGE_APPLIED,mContext).equalsIgnoreCase("2")){
            tvLableCityName.setText(AppCommonMethods.getStringPref(PREFS_CITY_NAME_GJ,mContext).toUpperCase());
        }

    }

    private void showBuzzImage(){
        int intStoredBuzzId = AppCommonMethods.getIntPref(String.valueOf(AppConstants.PREFS_BUZZ_ID),mContext);
        if(intStoredBuzzId != intBuzzId){
            if (strBuzzImageUrl != null){
                ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(strBuzzImageUrl,intBuzzId);
                imageZoomDialogFragment.setCancelable(true);
                imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
            }
        }
    }

    private void requestMasterApi(){
        JSONObject params = new JSONObject();
        try {
            params.put("sgks_city",AppCommonMethods.getStringPref(AppConstants.PREFS_CURRENT_CITY,mContext));
            params.put("last_updated",AppCommonMethods.getStringPref(AppConstants.PREFS_LAST_UPDATED_DATE,mContext));
            Log.i(TAG, "requestMasterApi: "+AppCommonMethods.getStringPref(AppConstants.PREFS_LAST_UPDATED_DATE,mContext));
            Log.i(TAG, "requestMasterApi: params \n"+params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_MASTER, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                new AppCommonMethods(mContext).LOG(0,TAG,response.toString());
                try {
                    Object resp = AppParser.parseMasterResponse(response.toString());
                    masterItem = (MasterItem) resp;
                    strBuzzImageUrl = masterItem.getStrBuzzImageUrl();
                   /* intMessageCount = masterItem.getIntMessagesCount();
                    intClassifiedCount = masterItem.getIntMessagesCount();*/
                    intBuzzId = masterItem.getIntBuzzId();
                    showBuzzImage();
                    setMessageCount(mTabLayout);
                    setClassifiedCount(mTabLayout);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: "+error);
                //Toast.makeText(mContext,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,TAG);
    }
}
