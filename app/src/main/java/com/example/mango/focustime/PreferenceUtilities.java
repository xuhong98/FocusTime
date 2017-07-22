package com.example.mango.focustime;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chenxiaoman on 19/7/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public final class PreferenceUtilities {

    public static final String KEY_TOTAL_SECOND_RECORDED = "total-second-recorded";
    public static final String KEY_FORCE_QUIT = "force_quit_key";
    public static final String KEY_BLACKLIST_APPS = "key_blacklist_apps";

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

    synchronized public static void setBlacklistApps(Context context, Set<String> blacklistApps) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(KEY_BLACKLIST_APPS, blacklistApps);
        editor.apply();
    }

    public static Set<String> getBlacklistApps(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> blacklistApps = prefs.getStringSet(KEY_BLACKLIST_APPS, new HashSet<String>());
        return blacklistApps;
    }

    synchronized public static void setForceQuit(Context context, boolean usable) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_FORCE_QUIT, usable);
        editor.apply();
    }

    public static boolean getForceQuit(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean forceQuit = prefs.getBoolean(KEY_FORCE_QUIT, false);
        return forceQuit;
    }

}
