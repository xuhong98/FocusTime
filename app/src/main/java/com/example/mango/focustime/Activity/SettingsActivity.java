package com.example.mango.focustime.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mango.focustime.R;
import com.example.mango.focustime.service.MyService;

import java.util.Calendar;
import java.util.Date;

import static com.example.mango.focustime.R.drawable.calendar;
import static java.security.AccessController.getContext;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupSharedPreferences();
    }

    public void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadPhoneFromPreferences(sharedPreferences);
        loadCameraFromPreferences(sharedPreferences);
        loadCalculatorFromPreferences(sharedPreferences);
        loadCalendarFromPreferences(sharedPreferences);

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadPhoneFromPreferences(SharedPreferences sharedPreferences) {
        MyService.setPhoneUsable(sharedPreferences.getBoolean(getString(R.string.pref_phone_key),
                true));
    }

    private void loadCameraFromPreferences(SharedPreferences sharedPreferences) {
        MyService.setCameraUsable(sharedPreferences.getBoolean(getString(R.string.pref_camera_key),
                true));
    }

    private void loadCalculatorFromPreferences(SharedPreferences sharedPreferences) {
        MyService.setCalculatorUsable(sharedPreferences.getBoolean(getString(R.string.pref_cal_key),
                true));
    }

    private void loadCalendarFromPreferences(SharedPreferences sharedPreferences) {
        MyService.setCalendarUsable(sharedPreferences.getBoolean(getString(R.string.pref_calendar_key),
                true));
    }


    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_phone_key))) {
            loadPhoneFromPreferences(sharedPreferences);
        } else if (key.equals(getString(R.string.pref_camera_key))) {
            loadCameraFromPreferences(sharedPreferences);
        } else if (key.equals(getString(R.string.pref_cal_key))) {
            loadCalculatorFromPreferences(sharedPreferences);
        } else if (key.equals(getString(R.string.pref_calendar_key))) {
            loadCalendarFromPreferences(sharedPreferences);
        }
    }


//    private void setReminder() {
//        final SharedPreferences.Editor editor1 = sharedPreferences.edit();
//        String time_set = sharedPreferences.getString("time", "设置提醒");
//
//        Intent intent = new Intent();
//        intent.setAction(this.getString(R.string.alarm_goes_off));
//        final PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
//        final AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
//
//        Date date = new Date();
//        calendar.setTime(date);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                Toast.makeText(getContext(), "@string/successfully_set_reminder", Toast.LENGTH_SHORT).show();
//                if (hourOfDay < 10 && minute < 10) {
//                    String time = " 0" + hourOfDay + ":0" + minute + " ";
//                    preferenceReminder.setSummary("On:" + time);
//                    editor1.putString("time", time);
//                    editor1.commit();
//                } else if (hourOfDay >= 10 && minute < 10) {
//                    String time = " " + hourOfDay + ":0" + minute + " ";
//                    preferenceReminder.setSummary("On:" + time);
//                    editor1.putString("time", time);
//                    editor1.commit();
//                } else if (hourOfDay < 10 && minute >= 10) {
//                    String time = " 0" + hourOfDay + ":" + minute + " ";
//                    preferenceReminder.setSummary("On:" + time);
//                    editor1.putString("time", time);
//                    editor1.commit();
//                } else {
//                    String time = " " + hourOfDay + ":" + minute + " ";
//                    preferenceReminder.setSummary("On:" + time);
//                    editor1.putString("time", time);
//                    editor1.commit();
//                }
//                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                calendar.set(Calendar.MINUTE, minute);
//                calendar.set(Calendar.SECOND, 0);
//                Log.d(".......", "当前时间:" + calendar.getTime() + "||" + calendar.getTimeInMillis());
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            }
//        }, hour, minute, true).show();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
