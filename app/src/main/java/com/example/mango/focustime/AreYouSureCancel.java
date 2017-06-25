package com.example.mango.focustime;

import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

public class AreYouSureCancel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_are_you_sure_cancel);
    }

    //click Yes and play music
    public void punish(View view) {
        Intent intent = new Intent(this, PunishmentActivity.class);
        startActivity(intent);
    }

    //click No and go back to FocusMode
    public void goBack(View view){
        Intent intent = new Intent(this, FocusModeActivity.class);
        startActivity(intent);
    }
}
