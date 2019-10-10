package com.changxiao.framework.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.changxiao.framework.BaseApplication;
import com.changxiao.framework.R;
import com.changxiao.framework.common.manager.ActivityManager;
import com.changxiao.framework.util.Logger;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * 基类FragmentActivity，抽取通用的方法
 * <p>
 * Created by Chang.Xiao on 2016/7/25.
 *
 * @version 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {

  protected final String TAG = BaseActivity.class.getSimpleName();

  protected Context mContext;
  protected Activity mActivity;
  protected InputMethodManager mManager;
  private Unbinder unbinder;
  private Bundle mSavedState;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Logger.i(TAG, "onCreate");
    BaseApplication.setTopActivity(this);
    ((BaseApplication) getApplication()).init();
    setContentView(getContentViewId());
    unbinder = ButterKnife.bind(this);
    mContext = this;
    mActivity = this;
    init(savedInstanceState);
    ActivityManager.addActivity(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      setTranslucentStatus(true);
    }
    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setStatusBarTintResource(getStatusBarColorResId());
    mManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

//    if (savedInstanceState == null) {
//      this.mSavedState = getIntent().getExtras();
//    } else {
//      this.mSavedState = savedInstanceState;
//    }
  }

  protected abstract int getContentViewId();

  protected abstract void init(Bundle savedInstanceState);

  protected int getStatusBarColorResId() {
    return R.color.colorPrimaryDark;
  }

  @TargetApi(19)
  private void setTranslucentStatus(boolean on) {
    Window win = getWindow();
    WindowManager.LayoutParams winParams = win.getAttributes();
    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    if (on) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    win.setAttributes(winParams);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.i(TAG, "onRestart");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.i(TAG, "onStart");
  }

  /**
   * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
   */
  private boolean isShouldHideKeyboard(View v, MotionEvent event) {
    if (v != null && (v instanceof EditText)) {
      int[] l = {0, 0};
      v.getLocationInWindow(l);
      int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
          + v.getWidth();
      if (event.getX() > left && event.getX() < right
          && event.getY() > top && event.getY() < bottom) {
        // 点击EditText的事件，忽略它。
        return false;
      } else {
        return true;
      }
    }
    // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
    return false;
  }

  /**
   * 多种隐藏软件盘方法的其中一种
   */
  protected void hideSoftInput(IBinder token) {
    if (token != null) {
      mManager.hideSoftInputFromWindow(token, 0);
      hideSoftInputAction();
    }
  }

  protected void hideSoftInputAction() {

  }

  /**
   * 获取Intent传过来的参数，用于替代方法getIntent().getExtras() <br>
   * 用于解决Activity重新启动后，原有的Intent中的参数丢失问题，已做容错处理，可以放心调用
   */
  public Bundle getIntentExtras() {
    if (mSavedState == null) {
      mSavedState = new Bundle();
    }
    return mSavedState;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mSavedState != null) {
      outState.putAll(mSavedState);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.i(TAG, "onResume");
    BaseApplication.setTopActivity(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.i(TAG, "onPause");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.i(TAG, "onStop");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
    ActivityManager.removeActivity(this);
    // 取消网络请求
    Log.i(TAG, "onDestroy");
  }

}
