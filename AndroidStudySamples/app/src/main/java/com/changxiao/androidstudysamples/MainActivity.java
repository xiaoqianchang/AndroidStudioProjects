package com.changxiao.androidstudysamples;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.changxiao.bottomnavigationdemo.activity.BottonNavigationActivity;
import com.changxiao.framework.activity.BaseActivity;

public class MainActivity extends BaseActivity {

  @Override
  protected int getContentViewId() {
    return R.layout.activity_main;
  }

  @Override
  protected void init(Bundle savedInstanceState) {

  }

  public void onBottomNavigationClick(View view) {
    startActivity(new Intent(this, BottonNavigationActivity.class));
  }
}
