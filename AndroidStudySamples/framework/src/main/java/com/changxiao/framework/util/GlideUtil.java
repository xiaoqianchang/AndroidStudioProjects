package com.changxiao.framework.util;

import android.app.Activity;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BaseTarget;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/1/14.
 *
 * @version 1.0
 */
public class GlideUtil {

  public static void loadImage(Activity activity, String url, BaseTarget target) {
    if (null != activity && !TextUtils.isEmpty(url)) {
      Glide.with(activity).load(url).asBitmap().into(target);
    }
  }
}
