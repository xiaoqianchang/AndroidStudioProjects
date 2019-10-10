package com.changxiao.framework.util;

import android.content.Context;
import com.changxiao.framework.constants.PreferenceConstants;

public class SharedPreferencesUtil extends BaseSharedPreferencesUtil {

  private static SharedPreferencesUtil instance;

  public SharedPreferencesUtil(Context context, String name) {
    super(context, name);
  }

  public SharedPreferencesUtil(Context context, String name, int mode) {
    super(context, name, mode);
  }

  public static SharedPreferencesUtil getInstance(Context context) {
    if (instance == null) {
      instance = new SharedPreferencesUtil(context, PreferenceConstants.FILENAME_SAMPLES_DATA);
    }
    return instance;
  }

  public static SharedPreferencesUtil getInstanceForPlayer(Context context) {
    instance = new SharedPreferencesUtil(context, PreferenceConstants.FILENAME_SAMPLES_DATA);
    return instance;
  }
}
