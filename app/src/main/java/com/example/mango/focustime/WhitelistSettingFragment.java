package com.example.mango.focustime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.view.Gravity;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mango.focustime.Activity.FocusModeActivity;
import com.example.mango.focustime.service.MyService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.attr.label;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by chenxiaoman on 17/7/17.
 */

public class WhitelistSettingFragment extends PreferenceFragmentCompat implements
        OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private PreferenceCategory preferenceCategory;
    private PackageManager pm;
    private List<ApplicationInfo> apps;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_usable_apps);

        sharedPreferences = getPreferenceScreen().getSharedPreferences();

        preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.pref_app_you_want_key));

        setUpAllApps();

    }

    private void setUpAllApps() {

        pm = getActivity().getPackageManager();
        apps = pm.getInstalledApplications(0);
        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (app.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String label = (String) pm.getApplicationLabel(app);
                Drawable icon = pm.getApplicationIcon(app);
                String packageName = app.packageName;

                CheckBoxPreference checkBoxPref = new CheckBoxPreference(getContext());
                checkBoxPref.setTitle(label);
                checkBoxPref.setKey(packageName);
                checkBoxPref.setSummaryOn(R.string.pref_on);
                checkBoxPref.setSummaryOff(R.string.pref_off);
                checkBoxPref.setIcon(icon);
                checkBoxPref.setChecked(false);

                preferenceCategory.addPreference(checkBoxPref);
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean inBlacklist = sharedPreferences.getBoolean(key, false);
        MyService.changeBlacklistApps(key, inBlacklist);
        if (inBlacklist) {
            Log.v("Whitelist", "in blacklist " + key);
        } else {
            Log.v("Whitelist", "Not in blacklist " + key);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
