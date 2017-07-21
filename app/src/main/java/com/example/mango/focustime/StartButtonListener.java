package com.example.mango.focustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mango.focustime.Activity.PunishmentActivity;
import com.example.mango.focustime.lineartimer.LinearTimerStates;
import com.example.mango.focustime.processutil.Features;
import com.example.mango.focustime.service.MyService;

import com.example.mango.focustime.lineartimer.LinearTimer;
import com.example.mango.focustime.PreferenceUtilities;

/**
 * Created by chenxiaoman on 23/6/17.
 */

public class StartButtonListener implements View.OnClickListener {

    protected static Context context;
    protected static Activity activity;
    public static CountDownTimer timer;
    private static boolean timerStarted = false;

    private final EditText second;
    private final EditText minute;
    private final Button s;
    private Intent intent;
    private Notification notification;
    private NotificationCompat.Builder mBuilder;
    private PendingIntent pendingIntent;

    private int totalSecond = 0;

    private LinearTimer linearTimer;

    private SharedPreferences sharedPreferences;
    private long secondLeft;
    private long totalSecondPassed;


    public StartButtonListener(Context context, Activity activity, LinearTimer linearTimer) {
        this.context = context;
        this.activity = activity;
        this.intent = activity.getIntent();
        this.linearTimer = linearTimer;

        second = (EditText) activity.findViewById(R.id.second);
        minute = (EditText) activity.findViewById(R.id.minute);
        s = (Button) activity.findViewById(R.id.start);


    }

    @Override
    public void onClick(View view) {

        if (s.getText().equals(context.getResources().getString(R.string.start_button))) {

            if (convertUserInputToTotalSecondsValid()) {
                if (isAccessibilitySettingsOn(context)) {
                    startTimer();
                } else {
                    openAccessibilityService();
                }
            }
        } else {
            showAlertForQuitting();
        }
    }

    public static boolean FocusModeStarted() {
        return timerStarted;
    }

    private boolean convertUserInputToTotalSecondsValid() {
        if (minute.getText().toString().equals("") && second.getText().toString().equals("")) {
            Toast.makeText(context, R.string.please_enter_time, Toast.LENGTH_LONG).show();
            return false;
        } else if (minute.getText().toString().equals("")) {
            if (Integer.parseInt(second.getText().toString()) <= 0) {
                Toast.makeText(context, R.string.invalid_number, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                totalSecond = Integer.parseInt(second.getText().toString());
                return true;
            }
        } else if (second.getText().toString().equals("")) {
            if (Integer.parseInt(minute.getText().toString()) <= 0) {
                Toast.makeText(context, R.string.invalid_number, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                totalSecond = Integer.parseInt(minute.getText().toString()) * 60;
                return true;
            }
        } else {
            if (Integer.parseInt(minute.getText().toString()) <= 0 && Integer.parseInt(second.getText().toString()) <= 0) {
                Toast.makeText(context, R.string.invalid_number, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                totalSecond = Integer.parseInt(minute.getText().toString()) * 60 + Integer.parseInt(second.getText().toString());
                return true;
            }
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(totalSecond * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondLeft = millisUntilFinished / 1000;
                long m = secondLeft / 60;
                long s = secondLeft - m * 60;

                String M = "" + m;
                String S = "" + s;

                //Format
                if(M.length() == 1) {
                    M = "0" + M;
                }
                if(S.length() == 1) {
                    S = "0" + S;
                }

                minute.setText(M);
                second.setText(S);
            }

            public void onFinish() {
                Toast.makeText(context, R.string.relax, Toast.LENGTH_LONG).show();
                clearTimer();
            }
        };

        openAccessibilityService();

        //linearTimer.builder.

        timer.start();
        linearTimer.startTimer((totalSecond - 1) * 1000);
        timerStarted = true;


        // Start detection service
        Features.showForeground = true;
        Intent intent = new Intent(context, MyService.class);
        startService();


        s.setText(R.string.cancel);

        minute.setEnabled(false);
        minute.setClickable(false);


        second.setEnabled(false);
        second.setClickable(false);

        Toast.makeText(context, R.string.mode_started, Toast.LENGTH_LONG).show();
    }

    private void startService() {
        Features.showForeground = true;
        Intent intent = new Intent(context, MyService.class);
        context.startService(intent);
    }

    // Create an AlertDialog.Builder and set the message, and click listeners
    // for the postivie and negative buttons on the dialog.
    private void showAlertForQuitting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.quit_mode);
        builder.setMessage(R.string.sure_to_quit);
        builder.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clearTimer();
                Intent intent = new Intent(context, PunishmentActivity.class);
                context.startActivity(intent);
            }
        });
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void clearTimer() {

        timer.cancel();
        if (linearTimer.getState().equals(LinearTimerStates.ACTIVE)) {
            linearTimer.pauseTimer();
        }

        linearTimer.resetTimer();

        timerStarted = false;
        s.setText(R.string.start_button);

        minute.setEnabled(true);
        minute.setClickable(true);
        second.setEnabled(true);
        second.setClickable(true);

        minute.setText("");
        second.setText("");

        // Stop detection service
        Features.showForeground = false;
        Intent i = new Intent(context, MyService.class);
        context.stopService(i);

        storeTotalSecondsPassed();
    }

    private void countTotalSecondsPassed() {
        totalSecondPassed = totalSecond - secondLeft;
    }

    private void storeTotalSecondsPassed() {
        countTotalSecondsPassed();
        Long totalSecondRecorded = PreferenceUtilities.getTotalSecondRecorded(context);
        totalSecondRecorded += totalSecondPassed + 1;
        PreferenceUtilities.setSecondRecorded(context, totalSecondRecorded);
    }

    final static String TAG = "AccessibilityUtil";

    // Check whether the accessibilitySetting is on
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

    private void openAccessibilityService() {
        // 判断辅助功能是否开启
        if (!isAccessibilitySettingsOn(context)) {

            AlertDialog.Builder noPermissionDialog;

            noPermissionDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.accessibility)
                    .setMessage(R.string.please_open_access)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            // 引导至辅助功能设置页面
                            activity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            noPermissionDialog.show();
        } else {
            return;
        }
    }
}
