package com.woxi.sgks_member.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.woxi.sgks_member.interfaces.AppConstants;

import java.util.Locale;

public class LocaleHelper implements AppConstants{
    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        if (lang.equalsIgnoreCase("")) {
            return setLocale(context, "1");
        }
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage) {
        return setLocale(context, defaultLanguage);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(PREFS_LANGUAGE_APPLIED, defaultLanguage);*/
        /*if (AGAppSettings.getStringPref(PREFS_LANGUAGE_APPLIED, context).equalsIgnoreCase("")) {
            return AGAppSettings.getStringPref(LANGUAGE_ENGLISH, context);
        }
        return AGAppSettings.getStringPref(LANGUAGE_MARATHI, context);*/
        return AppSettings.getStringPref(PREFS_LANGUAGE_APPLIED, context);
    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        return updateResourcesLegacy(context, language);
    }

    private static void persist(Context context, String language) {
        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_LANGUAGE_APPLIED, language);
        editor.apply();*/
        AppSettings.putStringPref(PREFS_LANGUAGE_APPLIED, language, context);
    }

    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }
}
