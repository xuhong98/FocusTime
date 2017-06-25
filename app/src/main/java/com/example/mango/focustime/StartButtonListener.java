package com.example.mango.focustime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by chenxiaoman on 23/6/17.
 */

public class StartButtonListener implements View.OnClickListener{

    private Context context;
    private Activity activity;
    private CountDownTimer timer;
    private int second;


    public StartButtonListener(Context context, Activity activity){
        this.context=context;
        this.activity=activity;
    }

    @Override
    public void onClick(View view) {
        final TextView s = (TextView) view;
        final TextView mainText = (TextView) activity.findViewById(R.id.time_left);
        final TextView detection = (TextView) activity.findViewById(R.id.detection);

        this.second= FocusModeActivity.second;

        if (s.getText().equals("start")) {

            timer = new CountDownTimer(second * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    mainText.setText("You are in the FocusMode! \n seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    mainText.setText("Good job! Relax for a while!");
                    s.setText("start");
                }
            };

            timer.start();

            s.setText("cancel");

//            if (isForegroundPkgViaDetectionService("com.example.chenxiaomanxuhong.focustime")) {
//                detection.setText("The app is foreground now.");
//            } else {
//                detection.setText(DetectionService.foregroundPackageName);
//            }

        } else {
            timer.cancel();
            mainText.setText("FocusMode cancelled.");
            s.setText("start");

            Intent i = new Intent(context, AreYouSureCancel.class);
            activity.startActivity(i);
        }
    }
//
//    public static boolean isForegroundPkgViaDetectionService(String packageName) {
//        return packageName.equals(DetectionService.foregroundPackageName);
//    }
}
