package com.example.mango.focustime;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PunishmentActivity extends AppCompatActivity {
    private MediaPlayer mp=new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punishment);
        MediaPlayer mp = MediaPlayer.create(this, R.raw.gnstyle);
        mp.start();

        //finish playing music and go back to mainActivity
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                Intent i = new Intent(PunishmentActivity.this, FocusModeActivity.class);
                startActivity(i);
            }
        });
    }
    }
