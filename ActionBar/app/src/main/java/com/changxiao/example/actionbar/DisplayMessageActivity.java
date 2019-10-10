package com.changxiao.example.actionbar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Chang.Xiao on 2016/3/25 16:02.
 *
 * @version 1.0
 */
public class DisplayMessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaymessage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
