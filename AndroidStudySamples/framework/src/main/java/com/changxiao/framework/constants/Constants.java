package com.changxiao.framework.constants;

import com.changxiao.framework.BuildConfig;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/1/13.
 *
 * @version 1.0
 */
public class Constants {

  //不能为final，会被编译器优化
  public static boolean isDebug = BuildConfig.DEBUG;
  public static boolean isLogToFile = false;

  // 服务器数据地址
  public static final String SERVER_URL = "http://gank.io/api/";
}
