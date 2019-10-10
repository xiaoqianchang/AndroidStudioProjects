package com.changxiao.example.floatwindowof360demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.changxiao.example.floatwindowof360demo.service.FloatWindowService;

/**
 * 主界面
 * <p/>
 * Created by Chang.Xiao on 2016/4/6 22:59.
 *
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private Button startFloatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFloatWindow = (Button) findViewById(R.id.btn_float_window);
        startFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                startService(intent);
                finish();
            }
        });
    }
}
