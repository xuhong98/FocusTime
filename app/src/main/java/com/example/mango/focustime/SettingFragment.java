package com.example.mango.focustime;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by chenxiaoman on 17/7/17.
 */

public class SettingFragment extends PreferenceFragmentCompat implements
        OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        // Go through all of the preferences, and set up their preference summary.
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            // You don't need to set up preference summaries for checkbox preferences because
            // they are already set up in xml using summaryOff and summary On
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }

        Preference preference = findPreference(getString(R.string.pref_motto_key));
        preference.setOnPreferenceChangeListener(this);
        setPreferenceSummary(preference, sharedPreferences.getString(getResources().getString(R.string.pref_motto_key), getResources().getString(R.string.pref_motto_default)));

        Preference preferenceReminder = findPreference(getString(R.string.pref_reminder_key));
        preference.setOnPreferenceChangeListener(this);

        Preference preferenceCamera = findPreference(getString(R.string.pref_camera_key));
        preference.setOnPreferenceChangeListener(this);

        Preference preferenceCalculator = findPreference(getString(R.string.pref_cal_key));
        preference.setOnPreferenceChangeListener(this);

        Preference preferenceCalendar = findPreference(getString(R.string.pref_calendar_key));
        preference.setOnPreferenceChangeListener(this);

        Preference preferencePhone = findPreference(getString(R.string.pref_phone_key));
        preference.setOnPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Figure out which preference was changed
        Preference preference = findPreference(key);
        if (null != preference) {
            // Updates the summary for the preference
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    /**
     * Updates the summary for the preference
     *
     * @param preference The preference to be updated
     * @param value      The value that the preference was updated to
     */
    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference.getKey().equals(getString(R.string.pref_motto_key))) {
            String stringMotto = (String) newValue;
            if (stringMotto.isEmpty()) {
                return false;
            } else {
                setPreferenceSummary(preference, stringMotto);
                Toast.makeText(getContext(), "Motto set", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else if (preference.getKey().equals(getString(R.string.pref_phone_key))) {
            boolean usable = (boolean) newValue;
        } else if (preference.getKey().equals(getString(R.string.pref_camera_key))) {
            boolean usable = (boolean) newValue;
        } else if (preference.getKey().equals(getString(R.string.pref_calendar_key))) {
            boolean usable = (boolean) newValue;
        } else if (preference.getKey().equals(getString(R.string.pref_cal_key))) {
            boolean usable = (boolean) newValue;
        } else if (preference.getKey().equals(getString(R.string.pref_reminder_key))) {
            boolean setReminder = (boolean) newValue;
        }
        return false;

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
