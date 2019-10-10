package com.changxiao.androidstudysamples.common.manager;

import android.app.Activity;
import com.changxiao.androidstudysamples.MainActivity;
import com.changxiao.androidstudysamples.MainApplication;
import com.changxiao.framework.util.Logger;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/1/13.
 *
 * @version 1.0
 */
public class ActivityManager extends com.changxiao.framework.common.manager.ActivityManager {

  public static MainActivity getMainActivity() {
    for (int i = 0; i < ACTIVITY_STACK.size(); i++) {
      WeakReference<Activity> weakReference = ACTIVITY_STACK.get(i);
      if (weakReference != null) {
        Activity activity = weakReference.get();
        if (activity instanceof MainActivity) {
          return (MainActivity) activity;
        }
      }
    }
    return null;
  }

  public synchronized static void finishAll(MainApplication context) {
    Iterator<WeakReference<Activity>> iterator = ACTIVITY_STACK.iterator();
    boolean isMainActivityAlive = false;
    while (iterator.hasNext()) {
      WeakReference<Activity> weakReference = iterator.next();
      if (weakReference != null) {
        Activity activity = weakReference.get();
        if (activity != null) {
          iterator.remove();
          if (activity instanceof MainActivity) {
            isMainActivityAlive = true;
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.finish();
          } else {
            activity.finish();
          }
          Logger.i(TAG, "finish activity " + activity.getClass());
        }
      }
    }
    if (!isMainActivityAlive) {
      context.exit();
    }
  }

}
