package com.example.mango.focustime.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mango.focustime.PreferenceUtilities;
import com.example.mango.focustime.WhitelistSettingFragment;
import com.example.mango.focustime.processutil.Features;
import com.example.mango.focustime.service.MyApplication;
import com.example.mango.focustime.R;
import com.example.mango.focustime.StartButtonListener;
import com.example.mango.focustime.lineartimer.LinearTimer;
import com.example.mango.focustime.lineartimer.LinearTimerView;
import com.example.mango.focustime.service.MyService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.M;

//import static com.example.mango.focustime.R.id.second;

public class FocusModeActivity extends AppCompatActivity implements LinearTimer.TimerListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private Context mContext;
    private LinearTimerView linearTimerView;
    private LinearTimer linearTimer;
    private TextView motto;
    private SharedPreferences sharedPreferences;
    private Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    private TextView TVtotalFocusedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_mode);
        mContext = this;

        TVtotalFocusedTime = (TextView) findViewById(R.id.totalFocusedTime);

        linearTimerView = (LinearTimerView) findViewById(R.id.linearTimer);

        linearTimer = new LinearTimer.Builder()
                .linearTimerView(linearTimerView)
                .timerListener(this)
                .duration(10 * 1000)
                .getCountUpdate(LinearTimer.COUNT_DOWN_TIMER, 1000)
                .build();

        //start count down timer
        StartButtonListener listener = new StartButtonListener(FocusModeActivity.this, this, linearTimer);
        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(listener);

        // Set font
        EditText second = (EditText) findViewById(R.id.second);
        EditText minute = (EditText) findViewById(R.id.minute);
        TextView mark = (TextView) findViewById(R.id.mark);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");

        second.setTypeface(custom_font);
        minute.setTypeface(custom_font);
        mark.setTypeface(custom_font);

        motto = (TextView) findViewById(R.id.motto);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        linearTimerView.setCircleRadiusInDp(width / 7);

        setupSharedPreferences();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_focustime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.how_to_use:
                Intent intent = new Intent(FocusModeActivity.this, HowToUseActivity.class);
                startActivity(intent);
                return true;
            case R.id.settings:
                Intent intent2 = new Intent(FocusModeActivity.this, SettingsActivity.class);
                startActivity(intent2);
                return true;
            case R.id.todo:
                Intent intent3 = new Intent(FocusModeActivity.this, ToDoActivity.class);
                startActivity(intent3);
                return true;
            case R.id.whitelist:
                Intent intent4 = new Intent(FocusModeActivity.this, WhiteListActivity.class);
                startActivity(intent4);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // from shared preferences. Call setColor, passing in the color you got
    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        loadMottoFromSharedPreferences(sharedPreferences);
        updateTotalSecondsRecorded();
        loadUsableApps();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Features.showForeground = false;
        Intent intent = new Intent(mContext, MyService.class);
        mContext.stopService(intent);

        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

        MyApplication.activityDestroy(this);
    }


    @Override
    public void animationComplete() {

    }

    @Override
    public void timerTick(long tickUpdateInMillis) {

    }

    @Override
    public void onTimerReset() {

    }

    @Override
    public void onBackPressed() {
        //Do nothing
        return;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_motto_key))) {
            loadMottoFromSharedPreferences(sharedPreferences);
        } else if (key.equals(PreferenceUtilities.KEY_TOTAL_SECOND_RECORDED)) {
            updateTotalSecondsRecorded();
        } else if (key.equals(getString(R.string.pref_reminder_key))) {

        } else {

        }
    }

    private void loadMottoFromSharedPreferences(SharedPreferences sharedPreferences) {
        String customizedMotto = sharedPreferences.getString(getString(R.string.pref_motto_key),
                getString(R.string.pref_motto_default));
        motto.setText(customizedMotto);
    }

    private void updateTotalSecondsRecorded() {
        long totalSecondRecorded = PreferenceUtilities.getTotalSecondRecorded(this);
        long day = totalSecondRecorded / (24 * 60 * 60);
        totalSecondRecorded -= day * (24 * 60 * 60);
        long hour = totalSecondRecorded / (60 * 60);
        totalSecondRecorded -= hour * (60 * 60);
        long min = totalSecondRecorded / 60;
        totalSecondRecorded -= min * 60;
        long sec = totalSecondRecorded;
        String record = getString(R.string.total_time_summary, day + "", hour + "", min + "", sec + "");
        TVtotalFocusedTime.setText(record);
    }

    private void loadUsableApps() {
//        ArrayList<String> allPackageName = WhitelistSettingFragment.getAppsPackageName();
//        for(String app : allPackageName) {
//            MyService.changeUsableApps(app, sharedPreferences.getBoolean(app, false));
//        }
    }

}

