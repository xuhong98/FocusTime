package com.example.mango.focustime;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;

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
}
