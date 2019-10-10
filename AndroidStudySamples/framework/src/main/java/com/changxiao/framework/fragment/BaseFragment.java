package com.changxiao.framework.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.changxiao.framework.util.Logger;

/**
 * Fragment的基类
 * <p>
 * Created by Chang.Xiao on 2016/7/25.
 *
 * @version 1.0
 */
public abstract class BaseFragment extends Fragment {

  private final String TAG = this.getClass().getSimpleName();

  private Unbinder mUnBinder;
  protected View mRootView;
  protected Activity mActivity;
  protected boolean hasLoadData = false; // 是否加载了数据

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mActivity = getActivity();
    mRootView = inflater.inflate(getContentViewId(), container, false);
    mUnBinder = ButterKnife.bind(this, mRootView);
    init(savedInstanceState);
    return mRootView;
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (!isAdded()) {
      return;
    }

    if (isVisibleToUser && !hasLoadData) {
      hasLoadData = true;
      loadData();
      Logger.d(TAG, this + "__setUserVisibleHint_loadData");
    }
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if (getUserVisibleHint()) {
      hasLoadData = true;
      loadData();
      Logger.d(TAG, this + "__onActivityCreated_loadData");
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mUnBinder.unbind();
  }

  protected abstract int getContentViewId();

  protected abstract void init(@Nullable Bundle savedInstanceState);

  /**
   * 在该方法中进行数据初始化，当NetworkError，点击NetworkErrorView,会自动重新执行该方法
   */
  public void loadData() {
  }
}
