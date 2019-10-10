package com.changxiao.reviewsample.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/9/22.
 *
 * @version 1.0
 */
public class ContextUtils {

  public static boolean checkContext(Context context) {
    if (null == context) {
      return false;
    } else if (context instanceof Activity) {
      Activity activity = (Activity)context;
      if (VERSION.SDK_INT < 17) {
        return !activity.isFinishing();
      } else {
        return !activity.isDestroyed() && !activity.isFinishing();
      }
    } else {
      return true;
    }
  }

  public static boolean checkActivity(Context context) {
    if (null == context) {
      return false;
    } else if (!(context instanceof Activity)) {
      return true;
    } else {
      Activity activity = (Activity)context;
      return !activity.isDestroyed() && !activity.isFinishing();
    }
  }
}
