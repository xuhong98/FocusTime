package com.example.mango.focustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
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
        final Button s = (Button) view;
        final TextView mainText = (TextView) activity.findViewById(R.id.time_left);
//        final TextView detection = (TextView) activity.findViewById(R.id.detection);

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
//            timer.cancel();
//            mainText.setText("FocusMode cancelled.");
//            s.setText("start");

            //dialog for cancel or not
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            View mView = activity.getLayoutInflater().inflate(R.layout.dialog_cancel,null);
            final Button button_yes = (Button) mView.findViewById(R.id.button_Yes);
            final Button button_no = (Button) mView.findViewById(R.id.button_No);

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();

            //click yes and change to punishment
            button_yes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PunishmentActivity.class);
                    context.startActivity(intent);
                }
            });

            //click no and go back to focusModeActivity
            button_no.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(context, FocusModeActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
