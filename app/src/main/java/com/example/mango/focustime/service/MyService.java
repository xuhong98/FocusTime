package com.example.mango.focustime.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mango.focustime.DetectionService;
import com.example.mango.focustime.FocusModeActivity;
import com.example.mango.focustime.R;
import com.example.mango.focustime.Features;
import com.example.mango.focustime.receiver.MyReceiver;
import com.example.mango.focustime.BackgroundUtil;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 2016/1/13.
 */
public class MyService extends Service {

    private static final float UPDATA_INTERVAL = 0.1f;//in seconds
    private Context mContext;
    private Notification notification;
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder mBuilder;
    private Intent mIntent;
    private NotificationManager mNotificationManager;
    private static final int NOTICATION_ID = 0x1;
    private BroadcastReceiver mScreenOffReceiver;

    private ArrayList<String> usableApps = new ArrayList<>();

    private PackageManager mPackageManager = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService", "Service onCreate()");
        mContext = this;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startNotification();
        setUsableApps();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "Service onStartCommand()");
        if (Features.showForeground) {
            synchronized (MyService.class) {
                Log.d("MyService", "Service broadcast");
                manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                int updateTime = (int) UPDATA_INTERVAL * 1000;
                long triggerAtTime = SystemClock.elapsedRealtime() + updateTime;
                Intent i = new Intent(mContext, MyReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
                updateNotification();
                if (!getAppStatus()) {
                    killApps();
                }
            }
        } else {
            stopForeground(true);
            mNotificationManager.cancelAll();
            stopSelf();
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Features.showForeground = false;
        stopForeground(true);
        Log.d("wenming", "Service onDestroy() method call");
        super.onDestroy();
    }

    private boolean getAppStatus() {
        return BackgroundUtil.isForeground(mContext, Features.BGK_METHOD, mContext.getPackageName());
    }

    private void startNotification() {
        mIntent = new Intent(mContext, FocusModeActivity.class);
        pendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("App处于" + ": " + DetectionService.getForegroundPackageName())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notification = mBuilder.build();
        startForeground(NOTICATION_ID, notification);
    }

    private void updateNotification() {
        mBuilder.setContentTitle("App处于" + ": " + DetectionService.getForegroundPackageName());
        mBuilder.setContentText("...");
        notification = mBuilder.build();
        mNotificationManager.notify(NOTICATION_ID, notification);
    }

    private void setUsableApps() {
        usableApps.add("com.android.systemui");
        usableApps.add("com.android.launcher3");
        usableApps.add("com.android.dialer");
        usableApps.add("com.android.settings");
    }

    private boolean isKillableApp(String name) {
        for(int i = 0; i < usableApps.size(); i++) {
            if (usableApps.get(i).equals(name)) {
                Log.v(name, "is not killable");
                return false;
            }
        }
        Log.v(name, "is killable");
        return true;
    }



    private void killApps() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String currentForegroundPackageName = DetectionService.getForegroundPackageName();

        if(currentForegroundPackageName != null && isKillableApp(currentForegroundPackageName)) {

            Log.v("MyService", "Killing " + currentForegroundPackageName);

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(startMain);

            am.killBackgroundProcesses(currentForegroundPackageName);
        } else {
            Log.v("MyService", "Not killing " + currentForegroundPackageName);
        }
    }

}
