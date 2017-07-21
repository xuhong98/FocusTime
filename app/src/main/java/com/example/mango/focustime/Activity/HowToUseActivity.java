package com.example.mango.focustime.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mango.focustime.R;
import com.example.mango.focustime.service.MyApplication;

public class HowToUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.activityDestroy(this);
    }
}
