package com.example.mango.focustime;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Build;
        import android.os.CountDownTimer;
        import android.provider.Settings;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.mango.focustime.service.MyService;

        import static com.example.mango.focustime.StartButtonListener.timer;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private View mView;
    private CheckBox checkBox6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Features.showForeground = true;
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_main, container, false);
        initCheckBox();
        //initTextView();
        layoutClick();
        return mView;
    }

    private void startService() {
        Features.showForeground = true;
        Intent intent = new Intent(mContext, MyService.class);
        mContext.startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Features.showForeground = false;
        Intent intent = new Intent(mContext, MyService.class);
        mContext.stopService(intent);
    }

    private void initCheckBox() {
        checkBox6 = (CheckBox) mView.findViewById(R.id.checkbox6);


        checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
//                    if (!AccessibilityUtil.isAccessibilitySettingsOn(getContext())) {
//                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//                        startActivity(intent);
//                    }

                    startService();
                    deselectAll();
                    checkBox6.setChecked(true);
                    Features.BGK_METHOD = BackgroundUtil.BKGMETHOD_VIA_DETECTION_SERVICE;

                }
            }
        });

    }

    public void layoutClick() {
        TextView textView = (TextView) mView.findViewById(R.id.clearservice);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Features.showForeground = false;
                Intent intent = new Intent(mContext, MyService.class);
                mContext.stopService(intent);
                deselectAll();
            }
        });
    }

    private void deselectAll() {
        checkBox6.setChecked(false);
    }
}
