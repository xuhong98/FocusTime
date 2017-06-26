package com.example.mango.focustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.mango.focustime.FocusModeActivity.second;
import static com.example.mango.focustime.R.id.minute;

/**
 * Created by chenxiaoman on 23/6/17.
 */

public class StartButtonListener implements View.OnClickListener{

    private Context context;
    private Activity activity;
    private CountDownTimer timer;


    public StartButtonListener(Context context, Activity activity){
        this.context=context;
        this.activity=activity;
    }

    @Override
    public void onClick(View view) {


        final TextView s = (TextView) view;

        if (s.getText().equals("start")) {
            final EditText second = (EditText) activity.findViewById(R.id.second);
            final EditText minute = (EditText) activity.findViewById(R.id.minute);

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
                if (Integer.parseInt(minute.getText().toString()) < 0 || Integer.parseInt(second.getText().toString()) <0) {
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
                    second.setText("0 sec");
                    Toast.makeText(context, "Focus mode ended! Relax for a while!", Toast.LENGTH_LONG).show();
                    s.setText("start");

                    minute.setEnabled(true);
                    minute.setClickable(true);


                    second.setEnabled(true);
                    second.setClickable(true);

                    minute.setText("");
                    second.setText("");
                }
            };

            timer.start();

            s.setText("cancel");

            minute.setEnabled(false);
            minute.setClickable(false);


            second.setEnabled(false);
            second.setClickable(false);


//            if (isForegroundPkgViaDetectionService("com.example.chenxiaomanxuhong.focustime")) {
//                detection.setText("The app is foreground now.");
//            } else {
//                detection.setText(DetectionService.foregroundPackageName);
//            }
            Toast.makeText(context, "Focus mode started!", Toast.LENGTH_LONG).show();

        } else {
//            timer.cancel();
//            mainText.setText("FocusMode cancelled.");
//            s.setText("start");

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            View mView = activity.getLayoutInflater().inflate(R.layout.dialog_cancel,null);
            final Button button_yes = (Button) mView.findViewById(R.id.button_Yes);
            final Button button_no = (Button) mView.findViewById(R.id.button_No);

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();

            button_yes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PunishmentActivity.class);
                    context.startActivity(intent);
                }
            });

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
