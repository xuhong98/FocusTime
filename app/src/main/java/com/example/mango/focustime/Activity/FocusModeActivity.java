package com.example.mango.focustime.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mango.focustime.PreferenceUtilities;
import com.example.mango.focustime.processutil.Features;
import com.example.mango.focustime.service.MyApplication;
import com.example.mango.focustime.R;
import com.example.mango.focustime.StartButtonListener;
import com.example.mango.focustime.lineartimer.LinearTimer;
import com.example.mango.focustime.lineartimer.LinearTimerView;
import com.example.mango.focustime.service.MyService;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import static com.example.mango.focustime.R.id.second;

public class FocusModeActivity extends AppCompatActivity implements LinearTimer.TimerListener, SharedPreferences.OnSharedPreferenceChangeListener{

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
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Oswald-Regular.ttf");

        second.setTypeface(custom_font);
        minute.setTypeface(custom_font);
        mark.setTypeface(custom_font);

        motto = (TextView) findViewById(R.id.motto);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        linearTimerView.setCircleRadiusInDp(width / 7);

        setupSharedPreferences();



        final SharedPreferences.Editor editor1 = sharedPreferences.edit();
        String time_set = sharedPreferences.getString("time", "设置提醒");

        Intent intent = new Intent();
        intent.setAction(this.getString(R.string.alarm_goes_off));
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Button btnAlarmSet = (Button) findViewById(R.id.btn_alarm_set);
        btnAlarmSet.setText(time_set);
        btnAlarmSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                new TimePickerDialog(FocusModeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast toast = Toast.makeText(FocusModeActivity.this, "设置提醒成功", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        if (hourOfDay < 10 && minute < 10) {
                            String time = " 0" + hourOfDay + ":0" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        } else if (hourOfDay >= 10 && minute < 10) {
                            String time = " " + hourOfDay + ":0" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        } else if (hourOfDay < 10 && minute >= 10) {
                            String time = " 0" + hourOfDay + ":" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        } else {
                            String time = " " + hourOfDay + ":" + minute + " ";
                            btnAlarmSet.setText(time);
                            editor1.putString("time", time);
                            editor1.commit();
                        }
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        Log.d(".......", "当前时间:" + calendar.getTime() + "||" + calendar.getTimeInMillis());
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }, hour, minute, true).show();
            }
        });
        Button btnAlarmCancle = (Button) findViewById(R.id.btn_alarm_cancle);
        btnAlarmCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAlarmSet.setText("设置提醒");
                editor1.putString("time", "设置提醒");
                editor1.commit();
                alarmManager.cancel(pendingIntent);
            }
        });
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
        } else if (key.equals(PreferenceUtilities.KEY_TOTAL_SECOND_RECORDED)){
            updateTotalSecondsRecorded();
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
        long hour = totalSecondRecorded  / (60 * 60);
        totalSecondRecorded -= hour * (60 * 60);
        long min = totalSecondRecorded / 60;
        totalSecondRecorded -= min * 60;
        long sec = totalSecondRecorded;
        String record = "" + day + "days, " + hour + "hour, " + min + "min and " + sec + "sec";
        TVtotalFocusedTime.setText(record);
    }

}

