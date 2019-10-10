package com.changxiao.bottomnavigationdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changxiao.bottomnavigationdemo.R;
import com.changxiao.framework.fragment.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends BaseFragment {

  @Override
  protected int getContentViewId() {
    return R.layout.fragment_web_view;
  }

  @Override
  protected void init(@Nullable Bundle savedInstanceState) {

  }

}
