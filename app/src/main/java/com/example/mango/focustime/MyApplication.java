package com.example.mango.focustime;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import static com.example.mango.focustime.MyApplication.onReceive;

/**
 * Created by chenxiaoman on 1/7/17.
 */

public class MyApplication extends Application {

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        if (StartButtonListener.FocusModeStarted()) {
            // Create an AlertDialog.Builder and set the message, and click listeners
            // for the postivie and negative buttons on the dialog.

            final CountDownTimer timer = StartButtonListener.timer;
            final Context context = StartButtonListener.context;
            final Button s = (Button) StartButtonListener.activity.findViewById(R.id.start);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Quit focus mode?");
            builder.setMessage("You will be punished. Are you sure to quit?");
            builder.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    timer.cancel();
                    s.setText("start");
                    Intent intent = new Intent(context, PunishmentActivity.class);
                    context.startActivity(intent);
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

            Intent intent = StartButtonListener.getIntent();

            onReceive(context, intent);
        }
    }

    public static void onReceive(Context context, Intent intent) {
        final WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.alpha = 1.0f;
        layoutParams.packageName = context.getPackageName();
        layoutParams.buttonBrightness = 1f;
        layoutParams.windowAnimations = android.R.style.Animation_Dialog;

        final View view = View.inflate(context.getApplicationContext(), R.layout.dialog_cancel, null);
        Button yesButton = (Button) view.findViewById(R.id.button_Yes);
        Button noButton = (Button) view.findViewById(R.id.button_No);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.removeView(view);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                manager.removeView(view);
            }
        });
        manager.addView(view, layoutParams);
    }


    private static boolean activityVisible;
}
