package com.changxiao.example.floatwindowof360demo.widget;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.changxiao.example.floatwindowof360demo.R;
import com.changxiao.example.floatwindowof360demo.service.FloatWindowService;
import com.changxiao.example.floatwindowof360demo.utils.MyWindowManager;

/**
 * Created by Chang.Xiao on 2016/4/7 11:32.
 *
 * @version 1.0
 */
public class FloatWindowBigView extends LinearLayout {

    /**
     * 记录大悬浮窗的宽度
     */
    public static float viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static float viewHeight;

    public FloatWindowBigView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.close);
        Button back = (Button) findViewById(R.id.back);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MyWindowManager.removeBigWindow(getContext());
                MyWindowManager.removeSmallWindow(getContext());
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                getContext().stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                MyWindowManager.removeBigWindow(getContext());
                MyWindowManager.createSmallWindow(getContext());
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            MyWindowManager.removeBigWindow(getContext());
            MyWindowManager.createSmallWindow(getContext());
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
