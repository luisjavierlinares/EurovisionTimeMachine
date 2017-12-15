package com.binarylemons.android.eurovisiontimemachine;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Luis on 28/11/2017.
 */

public class EuroSettings {

    private static final String PREF_PACKAGE_VERSION = "PREF_PACKAGE_VERSION";
    private static final String PREF_VIEWS_SINCE_LAST_AD = "PREF_VIEWS_SINCE_LAST_AD";
    private static final String PREF_TIME_SINCE_LAST_AD = "PREF_TIME_SINCE_LAST_AD";

    public static int getPackageVersion(Context context) {
        return context.getSharedPreferences(PREF_PACKAGE_VERSION, Context.MODE_PRIVATE)
                .getInt(PREF_PACKAGE_VERSION, -1);
    }

    public static void setPackageVersion(Context context, int version) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_PACKAGE_VERSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(PREF_PACKAGE_VERSION, version);
        editor.commit();
    }

    public static int getViewsSinceLastAd(Context context) {
        return context.getSharedPreferences(PREF_VIEWS_SINCE_LAST_AD, Context.MODE_PRIVATE)
                .getInt(PREF_VIEWS_SINCE_LAST_AD, 0);
    }

    public static void setViewsSinceLastAd(Context context, int lastCount) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_VIEWS_SINCE_LAST_AD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt(PREF_VIEWS_SINCE_LAST_AD, lastCount);
        editor.commit();
    }

    public static long getTimeLastAd(Context context) {
        return context.getSharedPreferences(PREF_TIME_SINCE_LAST_AD, Context.MODE_PRIVATE)
                .getLong(PREF_TIME_SINCE_LAST_AD, 0);
    }

    public static void setTimeLastAd(Context context, long timeSinceLastAd) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_TIME_SINCE_LAST_AD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(PREF_TIME_SINCE_LAST_AD, timeSinceLastAd);
        editor.commit();
    }
}
