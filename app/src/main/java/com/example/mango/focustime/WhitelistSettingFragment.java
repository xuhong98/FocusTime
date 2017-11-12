package com.example.mango.focustime;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.mango.focustime.util.PreferenceUtilities;

import static android.R.attr.label;

/**
 * Created by chenxiaoman on 17/7/17.
 */

public class WhitelistSettingFragment extends PreferenceFragmentCompat implements
        OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    private SharedPreferences sharedPreferences;
    private PreferenceCategory preferenceCategory;
    private PackageManager pm;
    private List<ApplicationInfo> apps;
    private ArrayList<String> socialMediaAppsName;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_usable_apps);

        sharedPreferences = getPreferenceScreen().getSharedPreferences();

        preferenceCategory = (PreferenceCategory) findPreference(getString(R.string.pref_app_you_want_key));

        setUpSocialMediaAppsName();

        setUpAllApps();

    }

    private void setUpAllApps() {

        pm = getActivity().getPackageManager();
        apps = pm.getInstalledApplications(0);
        for (ApplicationInfo app : apps) {
            if (((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (app.flags & ApplicationInfo.FLAG_SYSTEM) == 0
                    || isSocialMediaApps(app)) && !isFocusTime(app) ) {
                //retrieveAppInfo(app);
                String label = (String) pm.getApplicationLabel(app);
                Drawable icon = pm.getApplicationIcon(app);
                String packageName = app.packageName;

                CheckBoxPreference checkBoxPref = new CheckBoxPreference(getContext());
                checkBoxPref.setTitle(label);
                checkBoxPref.setKey(packageName);
                checkBoxPref.setSummaryOn(R.string.disable);
                checkBoxPref.setSummaryOff(R.string.enable);
                checkBoxPref.setIcon(icon);
                checkBoxPref.setChecked(false);

                preferenceCategory.addPreference(checkBoxPref);
            }
        }
    }

    private void setUpSocialMediaAppsName() {
        socialMediaAppsName = new ArrayList<>();
        socialMediaAppsName.add("YouTube");
        socialMediaAppsName.add("Facebook");
        socialMediaAppsName.add("WhatsApp");
    }

    private boolean isFocusTime(ApplicationInfo app) {
        String label = (String) pm.getApplicationLabel(app);
        return label.equals("FocusTime");
    }

    private boolean isSocialMediaApps(ApplicationInfo app) {
        return socialMediaAppsName.contains(label);
    }

    private void retrieveAppInfo(ApplicationInfo app) {

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof CheckBoxPreference) {
            boolean inBlacklist = sharedPreferences.getBoolean(key, false);
            changeBlacklistApps(key, inBlacklist);
        }

    }

    private void changeBlacklistApps(String key, boolean inBlacklist) {
        Set<String> blackListApps = sharedPreferences.getStringSet(PreferenceUtilities.KEY_BLACKLIST_APPS, new HashSet<String>());
        if (inBlacklist) {
            blackListApps.add(key);
            Log.v("Whitelist", "in blacklist " + key);
        } else {
            blackListApps.remove(key);
            Log.v("Whitelist", "Not in blacklist " + key);
        }

        PreferenceUtilities.setBlacklistApps(getContext(), blackListApps);

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
