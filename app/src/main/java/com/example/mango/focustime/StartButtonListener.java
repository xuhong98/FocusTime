package com.example.mango.focustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mango.focustime.service.MyService;

import static com.example.mango.focustime.R.id.minute;
import static com.example.mango.focustime.R.id.second;

/**
 * Created by chenxiaoman on 23/6/17.
 */

public class StartButtonListener implements View.OnClickListener{

    protected static Context context;
    protected static Activity activity;
    public static CountDownTimer timer;
    private static boolean timerStarted;

    private final EditText second;
    private final EditText minute;
    private final Button s;


    public StartButtonListener(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        second = (EditText) activity.findViewById(R.id.second);
        minute = (EditText) activity.findViewById(R.id.minute);
        s = (Button) activity.findViewById(R.id.start);
    }

    @Override
    public void onClick(View view) {

        if (s.getText().equals("start")) {


            int totalSecond = 0;

            if(minute.getText().toString().equals("") && second.getText().toString().equals("")) {
                Toast.makeText(context, "Please enter your time", Toast.LENGTH_LONG).show();
                return;
            } else if (minute.getText().toString().equals("")) {
                if (Integer.parseInt(second.getText().toString()) <= 0) {
                    Toast.makeText(context, "Invalid number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    totalSecond = Integer.parseInt(second.getText().toString());
                }
            } else if (second.getText().toString().equals("")) {
                if (Integer.parseInt(minute.getText().toString()) <= 0) {
                    Toast.makeText(context, "Invalid number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    totalSecond = Integer.parseInt(minute.getText().toString()) * 60;
                }
            } else {
                if (Integer.parseInt(minute.getText().toString()) <= 0 || Integer.parseInt(second.getText().toString()) <= 0) {
                    Toast.makeText(context, "Invalid number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    totalSecond = Integer.parseInt(minute.getText().toString()) * 60 + Integer.parseInt(second.getText().toString());
                }
            }

            timer = new CountDownTimer(totalSecond * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    long secondLeft  = millisUntilFinished / 1000;
                    long m = secondLeft/60;
                    long s = secondLeft - m * 60;

                    minute.setText("" + m + " min");
                    second.setText("" + s + " sec");
                }

                public void onFinish() {
                    Toast.makeText(context, "Focus mode ended! Relax for a while!", Toast.LENGTH_LONG).show();
                    clearTimer();
                }
            };

            timer.start();
            timerStarted = true;


            // Start detection service
            Features.showForeground = true;
            Intent intent = new Intent(context, MyService.class);
            startService();


            s.setText("cancel");

            minute.setEnabled(false);
            minute.setClickable(false);


            second.setEnabled(false);
            second.setClickable(false);

            Toast.makeText(context, "Focus mode started!", Toast.LENGTH_LONG).show();

        } else {
            // Create an AlertDialog.Builder and set the message, and click listeners
            // for the postivie and negative buttons on the dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Quit focus mode?");
            builder.setMessage("You will be punished. Are you sure to quit?");
            builder.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clearTimer();
                }
            });
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
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
    }

    public static boolean FocusModeStarted() {
        return timerStarted;
    }

    private void startService() {
        Features.showForeground = true;
        Intent intent = new Intent(context, MyService.class);
        context.startService(intent);
    }

    private void clearTimer() {
        timer.cancel();

        timerStarted = false;
        s.setText("start");

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

        s.setText("start");
        Intent intent = new Intent(context, PunishmentActivity.class);
        context.startActivity(intent);
    }

}
