package com.example.mango.focustime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.mango.focustime.Features;
import com.example.mango.focustime.service.MyService;


/**
 * Created by wenmingvs on 2016/1/13.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Features.showForeground) {
            Intent i = new Intent(context, MyService.class);
            context.startService(i);
        }

    }
}
