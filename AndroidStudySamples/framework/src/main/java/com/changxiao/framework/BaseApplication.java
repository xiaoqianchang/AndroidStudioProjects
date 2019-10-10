package com.changxiao.framework;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import com.changxiao.framework.util.ActivityManagerDetacher;
import com.changxiao.framework.util.BaseUtil;
import java.lang.ref.SoftReference;

/**
 * 放入公共处理代码
 *
 * Created by Chang.Xiao on 2019/1/13.
 *
 * @version 1.0
 */
public abstract class BaseApplication extends Application {

  public static BaseApplication mAppInstance;
  public static long startTime;
  private static SoftReference<Activity> mTopActivityReference;
  protected boolean hasInit = false;
  private ActivityManagerDetacher mActivityManagerDetacher;

  public static final Context getMyApplicationContext() {
    return mAppInstance;
  }

  public static @Nullable
  Activity getTopActivity() {
    if (mTopActivityReference == null) {
      return null;
    }
    Activity activity = mTopActivityReference.get();
    if (activity == null || activity.isFinishing()) {
      return null;
    }
    return activity;
  }

  public static void setTopActivity(Activity topActivity) {
    mTopActivityReference = new SoftReference<Activity>(topActivity);
  }

  public final static boolean isTopActivityAvaliable() {
    if (mTopActivityReference == null) {
      return false;
    }
    Activity activity = mTopActivityReference.get();
    if (null != activity && !activity.isFinishing()) {
      return true;
    }

    return false;
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    mAppInstance = this;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mAppInstance = this;
    startTime = System.currentTimeMillis();
    System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    if (BaseUtil.isMainProcess(this)) {
      mActivityManagerDetacher = new ActivityManagerDetacher();
      registerActivityLifecycleCallbacks(mActivityManagerDetacher);
    }
  }

  public final void init() {

    if (hasInit) {
      return;
    }
    hasInit = true;

    initApp();
  }

  public abstract void initApp();

  protected abstract void exitApp();

  public final void exit() {
    hasInit = false;
    exitApp();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public int getAppCount() {
    if(mActivityManagerDetacher!=null) {
      return mActivityManagerDetacher.getAppCount();
    }
    return 0;
  }
}
