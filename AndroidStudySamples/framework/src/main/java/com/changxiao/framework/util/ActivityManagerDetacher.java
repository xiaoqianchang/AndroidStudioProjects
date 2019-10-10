package com.changxiao.framework.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/1/13.
 *
 * @version 1.0
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityManagerDetacher implements ActivityLifecycleCallbacks {

  private int appCount = 0;

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public int getAppCount() {
    return appCount;
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

  }

  @Override
  public void onActivityStarted(Activity activity) {
    if(appCount == 0){  //应用回到前台
      for(AppStatusListener listener : mAppStatusListeners){
        listener.onAppGoToForeground(activity);
      }
    }
    appCount++;
  }

  @Override
  public void onActivityResumed(Activity activity) {}

  @Override
  public void onActivityPaused(Activity activity) {}

  @Override
  public void onActivityStopped(Activity activity) {
    appCount--;
    if(appCount == 0){ //应用退至后台
      for(AppStatusListener listener : mAppStatusListeners){
        listener.onAppGoToBackground(activity);
      }
    }
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

  @Override
  public void onActivityDestroyed(Activity activity) {
    try {
      detachActivityFromActivityManager(activity);
    } catch (NoSuchFieldException e) {
      Logger.log("Samsung activity leak fix has to be removed as ActivityManager field has changed"+e);
    } catch (IllegalAccessException e) {
      Logger.log("Samsung activity leak fix did not work, probably activity has leaked"+e);
    }
  }

  /**
   * fuck Samsung,On Samsung, Activity reference is stored into static mContext field of ActivityManager
   * resulting in activity leak.
   */
  private void detachActivityFromActivityManager(Activity activity) throws
      NoSuchFieldException, IllegalAccessException {
    ActivityManager activityManager = (ActivityManager) activity.
        getSystemService(Context.ACTIVITY_SERVICE);

    Field contextField = activityManager.getClass().getDeclaredField("mContext");

    int modifiers = contextField.getModifiers();
    if ((modifiers | Modifier.STATIC) == modifiers) {
      // field is static on Samsung devices only
      contextField.setAccessible(true);

      if (contextField.get(null) == activity) {
        contextField.set(null, null);
      }
    }
  }

  public interface AppStatusListener{
    void onAppGoToForeground(Activity startedActivity);

    void onAppGoToBackground(Activity stoppedActivity);
  }

  private List<AppStatusListener> mAppStatusListeners = new ArrayList<>();

  public void addAppStatusListener(AppStatusListener listener){
    if(listener != null && !mAppStatusListeners.contains(listener)){
      mAppStatusListeners.add(listener);
    }
  }

  public void removeAppStatusListener(AppStatusListener listener){
    if(listener != null){
      mAppStatusListeners.remove(listener);
    }
  }
}
