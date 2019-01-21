package com.woxi.sgkks_member.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.interfaces.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Sharvari.
 */
public class AppCommonMethods implements AppConstants {
    private Context mContext;
    private boolean isLogEnabled = true;

    public AppCommonMethods() {
    }

    public AppCommonMethods(Context context) {
        mContext = context;
    }

    /**
     * @param key
     * @param value
     * @param context
     */
    public static void putStringPref(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * @param key
     * @param context
     * @return
     */
    public static String getStringPref(String key, Context context) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString(key, "");
    }

    /**
     * @param key
     * @param value
     * @param context
     */
    public static void putIntPref(String key, int value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * @param key
     * @param context
     * @return
     */
    public static int getIntPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, 0);
    }

    /**
     * @param key
     * @param value
     * @param context
     */
    public static void putBooleanPref(String key, boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * @param key
     * @param context
     * @return
     */
    public static boolean getBooleanPref(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    /**
     * <b>public void LOG</b>
     * <p>This function used to create log</p>
     *
     * @param type    Log type
     * @param tag     Current activity name / tag for log
     * @param message Message to be printed.
     */
    public void LOG(int type, String tag, String message) {
        if (isLogEnabled) {
            switch (type) {
                case 0:
                    Log.d(tag, message);
                    break;
                case 1:
                    Log.e(tag, message);
                    break;
                case 2:
                    Log.i(tag, message);
                    break;
                case 3:
                    Log.v(tag, message);
                    break;
            }
        }
    }

    /**
     * <b>public AlertDialog showAlert</b>
     * <p>This function used to show different alert messages</p>
     *
     * @param message Message to be shown in alert
     * @return alert dialog
     */
    public AlertDialog showAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(mContext.getString(R.string.app_name));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return alertDialog;
    }

    /**
     * <b>public void hideKeyBoard</b>
     * <p>This function used to hide keyboard on particular view</p>
     *
     * @param view target view
     */
    public void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * <b>public String convertDateFormat</b>
     * <p>This function used to convert format of date</p>
     *
     * @param currentDateFormat  currentDateFormat
     * @param expectedDateFormat expectedDateFormat
     * @param currentDate        currentDate
     * @return converted date
     */
    public String convertDateFormat(String currentDateFormat, String expectedDateFormat, String currentDate) {
        try {
            SimpleDateFormat read = new SimpleDateFormat(currentDateFormat);
            SimpleDateFormat write = new SimpleDateFormat(expectedDateFormat);
            String str;
            str = write.format(read.parse(currentDate));
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <b>public String convertTimeFormat</b>
     * <p>This function used to convert format of time</p>
     *
     * @param currentTimeFormat  currentTimeFormat
     * @param expectedTimeFormat expectedTimeFormat
     * @param time               time
     * @return converted time
     */
    public String convertTimeFormat(String currentTimeFormat, String expectedTimeFormat, String time) {
        String newTime = null;
        SimpleDateFormat actual = new SimpleDateFormat(currentTimeFormat);
        SimpleDateFormat target = new SimpleDateFormat(expectedTimeFormat);
        Date date;
        try {
            date = actual.parse(time);
            newTime = target.format(date);
            return newTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <b>public boolean isNetworkAvailable()</b>
     * <p>This function used to check current network connection availability</p>
     *
     * @return current status of network connectivity
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String toTitleCase(String str) {

        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }
}
