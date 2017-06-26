package com.example.mango.focustime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.mango.focustime.R.id.second;

public class FocusModeActivity extends AppCompatActivity {

    public static int second = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_mode);

        //everytime change to this activity, set second equal 10
        second = 10;

        //start count down timer
        StartButtonListener listener = new StartButtonListener(FocusModeActivity.this, this);
        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(listener);
    }

        private void display(int number) {
            TextView quantityTextView = (TextView) findViewById(R.id.second);
            quantityTextView.setText("" + number);
        }

        public void increment(View view) {
            if (second < 1000) {
                second = second + 5;
                display(second);
            }
        }

        public void decrement(View view) {
            if (second > 0) {
                second = second - 5;
                display(second);
            }
        }

        //return button
        public void returnMain(View view) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
}

