package com.example.mango.focustime;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenxiaoman on 19/7/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class PreferenceUtilities {

    public static final String KEY_TOTAL_SECOND_RECORDED = "total-second-recorded";

    private static final int DEFAULT_COUNT = 0;

    synchronized public static void setSecondRecorded(Context context, long totalSecondsPassed) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_TOTAL_SECOND_RECORDED, totalSecondsPassed);
        editor.apply();
    }

    public static long getTotalSecondRecorded(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long TotalSecondRecorded = prefs.getLong(KEY_TOTAL_SECOND_RECORDED, DEFAULT_COUNT);
        return TotalSecondRecorded;
    }

//    synchronized public static void setCamera(Context context, boolean usable) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean(getString(R.string.pref_motto_key), usable);
//        editor.apply();
//    }

}
