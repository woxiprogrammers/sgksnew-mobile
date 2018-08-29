package com.woxi.sgks_member.miscellaneous;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.woxi.sgks_member.R;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.utils.AppCommonMethods;

/**
 * <b>public class SettingsActivity extends AppCompatActivity</b>
 * <p>This class is used to manage App's Settings</p>
 * Created by Rohit.
 */
public class SettingsActivity extends AppCompatActivity {
    private TextView mTvDatabaseSyncInfo, mTvOfflineSupportInfo;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = SettingsActivity.this;

        Switch mSwitchDatabaseSync = (Switch) findViewById(R.id.toggleDatabaseSync);
        Switch mSwitchOfflineSupport = (Switch) findViewById(R.id.toggleOfflineSupport);
        mTvDatabaseSyncInfo = (TextView) findViewById(R.id.tvDatabaseSyncInfo);
        mTvOfflineSupportInfo = (TextView) findViewById(R.id.tvOfflineSupportInfo);

        boolean isLocalStorageSyncEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_LOCAL_STORAGE_SYNC_ENABLED, mContext);
        if (isLocalStorageSyncEnabled) {
            mSwitchDatabaseSync.setChecked(true);
            mTvDatabaseSyncInfo.setText(getString(R.string.databse_sync_enabled_info));
        } else {
            mSwitchDatabaseSync.setChecked(false);
            mTvDatabaseSyncInfo.setText(getString(R.string.databse_sync_disabled_info));
        }
        boolean isOfflineSupportEnabled = AppCommonMethods.getBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, mContext);
        if (isOfflineSupportEnabled) {
            mSwitchOfflineSupport.setChecked(true);
            mTvOfflineSupportInfo.setText(getString(R.string.offline_support_enabled_info));
        } else {
            mSwitchOfflineSupport.setChecked(false);
            mTvOfflineSupportInfo.setText(getString(R.string.offline_support_disabled_info));
        }

        mSwitchDatabaseSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_LOCAL_STORAGE_SYNC_ENABLED, true, mContext);
                    mTvDatabaseSyncInfo.setText(getString(R.string.databse_sync_enabled_info));
                    Toast.makeText(mContext, getString(R.string.toast_databse_sync_enabled), Toast.LENGTH_SHORT).show();
                } else {
                    AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_LOCAL_STORAGE_SYNC_ENABLED, false, mContext);
                    mTvDatabaseSyncInfo.setText(getString(R.string.databse_sync_disabled_info));
                    Toast.makeText(mContext, getString(R.string.toast_databse_sync_disabled), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSwitchOfflineSupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, true, mContext);
                    mTvOfflineSupportInfo.setText(getString(R.string.offline_support_enabled_info));
                    Toast.makeText(mContext, getString(R.string.toast_offline_support_enabled), Toast.LENGTH_SHORT).show();
                } else {
                    AppCommonMethods.putBooleanPref(AppConstants.PREFS_IS_OFFLINE_SUPPORT_ENABLED, false, mContext);
                    mTvOfflineSupportInfo.setText(getString(R.string.offline_support_disabled_info));
                    Toast.makeText(mContext, getString(R.string.toast_offline_support_disabled), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
