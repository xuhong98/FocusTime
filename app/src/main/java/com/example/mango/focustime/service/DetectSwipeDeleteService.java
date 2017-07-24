package com.example.mango.focustime.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mango.focustime.StartButtonListener;
import com.example.mango.focustime.util.PreferenceUtilities;

/**
 * Created by chenxiaoman on 24/7/17.
 */

public class DetectSwipeDeleteService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.v("DetectSwipeDelete", "onTaskRemoved called");
        super.onTaskRemoved(rootIntent);

        if (StartButtonListener.FocusModeStarted()) {
            PreferenceUtilities.setForceQuit(getBaseContext(), true);
            Log.v("DetectSwipeDelete", "set force quit true");
        } else {
            Log.v("DetectSwipeDelete", "did not set force quit true");
        }
        //stop service
        this.stopSelf();
    }
}
