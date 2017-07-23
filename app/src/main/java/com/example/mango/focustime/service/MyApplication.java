package com.example.mango.focustime.service;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;


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

    }

    public static void activityDestroy(Context context) {
//        if (StartButtonListener.FocusModeStarted()) {
//            PreferenceUtilities.setForceQuit(context, true);
//            Log.v("MyApplication", "set force quit true");
//        }
//        Log.v("MyApplication", "set force quit true");
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
