package com.changxiao.bottomnavigationdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changxiao.bottomnavigationdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralWebViewFragment extends Fragment {


  public GeneralWebViewFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_general_web_view, container, false);
  }

}
