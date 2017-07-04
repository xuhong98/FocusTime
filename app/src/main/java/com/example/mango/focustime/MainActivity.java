package com.example.mango.focustime;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.CountDownTimer;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import static com.example.mango.focustime.StartButtonListener.timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void focusMode(View view){
        Intent i = new Intent(this,FocusModeActivity.class);
        startActivity(i);
    }

    public void toDoList(View view){
        Intent i = new Intent(this,ToDoActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
