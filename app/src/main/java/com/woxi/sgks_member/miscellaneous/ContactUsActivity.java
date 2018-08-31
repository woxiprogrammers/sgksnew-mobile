package com.woxi.sgks_member.miscellaneous;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.woxi.interfaces.AppConstants;
import com.woxi.sgks_member.R;
import com.woxi.utils.AppCommonMethods;

/**
 * <b>public class ContactUsActivity extends AppCompatActivity</b>
 * <p>This class is used for "Contact Us" page</p>
 * Created by Rohit.
 */
public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String currentCity = AppCommonMethods.getStringPref(AppConstants.PREFS_CURRENT_CITY, getBaseContext());
        if (currentCity.equalsIgnoreCase(getString(R.string.city_name_pune))) {
            setContentView(R.layout.activity_contact_us_pune);
        } else if (currentCity.equalsIgnoreCase(getString(R.string.city_name_navsari))) {
            setContentView(R.layout.activity_contact_us_navsari);
        } else if (currentCity.equalsIgnoreCase(getString(R.string.city_name_amroli))) {
            setContentView(R.layout.activity_contact_us_amroli);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.contactUs);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
