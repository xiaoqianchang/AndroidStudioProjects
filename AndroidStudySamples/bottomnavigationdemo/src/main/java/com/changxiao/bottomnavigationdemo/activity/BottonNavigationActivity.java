package com.changxiao.bottomnavigationdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.changxiao.bottomnavigationdemo.R;
import com.changxiao.framework.activity.BaseActivity;

public class BottonNavigationActivity extends BaseActivity {

  @Override
  protected int getContentViewId() {
    return R.layout.activity_botton_navigation;
  }

  @Override
  protected void init(Bundle savedInstanceState) {

  }

  public void onTabLayoutFragmentClick(View view) {
    startActivity(new Intent(this, TabLayoutFragmentActivity.class));
  }
}
