package com.woxi.sgks_member.home;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woxi.sgks_member.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private static final String APP_DATABASE_CREATED = "notDatabaseCreated";
    private ViewPager mViewPager;
//    private HomeViewPagerAdapter viewPagerAdapter;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_coordinator);
        mContext = HomeActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvMemberCount = (TextView) toolbar.findViewById(R.id.tvMemberCount);
        mLL_MemberCount = (LinearLayout) findViewById(R.id.llMemberCount);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
