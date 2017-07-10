package com.example.mango.focustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


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
//        if (StartButtonListener.FocusModeStarted()) {
//            // Create an AlertDialog.Builder and set the message, and click listeners
//            // for the postivie and negative buttons on the dialog.
//
//            final CountDownTimer timer = StartButtonListener.timer;
//            final Context context = StartButtonListener.context;
//            final Button s = (Button) StartButtonListener.activity.findViewById(R.id.start);
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Quit focus mode?");
//            builder.setMessage("You will be punished. Are you sure to quit?");
//            builder.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    timer.cancel();
//                    s.setText("start");
//                    Intent intent = new Intent(context, PunishmentActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User clicked the "Cancel" button, so dismiss the dialog
//                    // and continue editing the pet.
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                }
//            });
//
//            // Create and show the AlertDialog
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
//        }
    }


    private static boolean activityVisible;

    private int appCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d("MyApplication", "onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d("MyApplication", "onActivityStarted");
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.d("MyApplication", "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.d("MyApplication", "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d("MyApplication", "onActivityStopped");
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d("MyApplication", "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d("MyApplication", "onActivityDestroyed");
            }
        });
    }

    public int getAppCount() {
        return appCount;
    }

    public void setAppCount(int appCount) {
        this.appCount = appCount;
    }
}
