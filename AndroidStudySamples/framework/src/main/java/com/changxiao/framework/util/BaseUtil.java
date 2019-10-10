/**
 * BaseUtil.java
 * com.ximalaya.ting.android.opensdk.util
 *
 * Function： TODO
 *
 * ver     date      		author
 * ──────────────────────────────────
 * 2015-12-16 		jack.qin
 *
 * Copyright (c) 2015, TNT All Rights Reserved.
 */

package com.changxiao.framework.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.AppTask;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.changxiao.framework.BaseApplication;
import com.changxiao.framework.R;
import com.changxiao.framework.constants.Constants;
import com.changxiao.framework.constants.PreferenceConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BaseUtil {

  static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
  static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
  /**
   * 获得当前进程名
   *
   * @param context
   * @return
   */
  public static String curProcessName;

  public static int isInTime(String time) throws IllegalArgumentException {
    if (TextUtils.isEmpty(time) || !time.contains("-")
        || !time.contains(":")) {
      if (Constants.isDebug) {
        throw new IllegalArgumentException("Illegal Argument arg:" + time);
      } else {
        return -2;
      }
    }
    String[] args = time.split("-");
    boolean onlyHasHour = (args[0].split(":")).length == 2;
    boolean hasDay = (args[0].split(":")).length == 3;
    boolean hasYear = (args[0].split(":")).length == 5;
    SimpleDateFormat sdf = null;
    if (hasDay) {
      sdf = new SimpleDateFormat("dd:HH:mm", Locale.getDefault());
    } else if (hasYear) {
      sdf = new SimpleDateFormat("yy:MM:dd:HH:mm", Locale.getDefault());
    } else if (onlyHasHour) {
      sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }
    if (sdf != null) {
      String nowStr = sdf.format(new Date(System.currentTimeMillis()));
      try {
        long now = sdf.parse(nowStr).getTime();
        long start = sdf.parse(args[0]).getTime();
        if (args[1].contains("00:00") && hasDay) {
          args[1] = (args[1].split(":"))[0] + ":" + "23:59";
        } else if (args[1].contains("00:00") && hasYear) {
          args[1] = (args[1].split(":"))[0] + ":"
              + (args[1].split(":"))[1] + ":"
              + (args[1].split(":"))[2] + ":" + "23:59";
        } else if (args[1].contains("00:00") && onlyHasHour) {
          args[1] = "23:59";
        }
        long end = sdf.parse(args[1]).getTime();
        if (now >= end) {
          return -1;
        } else if (now >= start && now < end) {
          return 0;
        } else {
          return 1;
        }
      } catch (ParseException e) {
        e.printStackTrace();
        if (Constants.isDebug) {
          throw new IllegalArgumentException("Illegal Argument arg:"
              + time);
        } else {
          return -2;
        }
      }
    }
    return -2;
  }

  public static String[] getYDTDayNum() {
    String[] days = new String[3];

    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    int index = -1;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy:MM:dd");

    for (int i = 0; i < 3; i++) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DATE, index);

      days[i] = simpleDateFormat.format(calendar.getTime());
      index++;
    }
    return days;
  }

  /**
   * 判断当前时间是否为22:00~06:00 夜间时间
   *
   * @return true 当前时间为夜间时间
   */
  public static boolean isNight() {

    SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
    String curTime = sdf.format(System.currentTimeMillis()).trim();
    int hour = Integer.valueOf(curTime);
    // 22:00~06:00
    if (hour == 22 || hour == 23 || (hour >= 0 && hour < 6)) {
      return true;
    } else {
      return false;
    }
  }

  public static String getCurProcessName(Context context) {
    if (context == null) {
      return "";
    }
    if (!TextUtils.isEmpty(curProcessName)) {
      return curProcessName;
    }
    int pid = android.os.Process.myPid();
    ActivityManager mActivityManager = (ActivityManager) context
        .getSystemService(Context.ACTIVITY_SERVICE);
    List<RunningAppProcessInfo> runningAppProcesses = null;
    try {
      runningAppProcesses = mActivityManager.getRunningAppProcesses();
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (runningAppProcesses != null) {
      for (ActivityManager.RunningAppProcessInfo appProcess : runningAppProcesses) {
        if (appProcess.pid == pid) {
          curProcessName = appProcess.processName;
          break;
        }
      }
    }

    if (TextUtils.isEmpty(curProcessName)) {
      curProcessName = getProcessName();
    }

    if (TextUtils.isEmpty(curProcessName)) {
      curProcessName = context.getPackageName();
    }
    return curProcessName;
  }

  private static String getProcessName() {
    try {
      File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
      BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
      String processName = mBufferedReader.readLine().trim();
      mBufferedReader.close();
      return processName;
    } catch (Throwable e) {
      e.printStackTrace();
      return null;
    }
  }

  static Class<?>[] nullToEmpty(final Class<?>[] array) {
    if (array == null || array.length == 0) {
      return EMPTY_CLASS_ARRAY;
    }
    return array;
  }

  static Object[] nullToEmpty(final Object[] array) {
    if (array == null || array.length == 0) {
      return EMPTY_OBJECT_ARRAY;
    }
    return array;
  }

  /**
   * 是否是主进程
   */
  public static boolean isMainProcess(Context context) {
    String processName = getCurProcessName(context);
    if (!TextUtils.isEmpty(processName) && processName.equals(context.getPackageName())) {
      return true;
    }
    return false;
  }

  public static boolean isProcessRunning(Context context, String processName) {
    ActivityManager activityManager = (ActivityManager) context
        .getSystemService(Context.ACTIVITY_SERVICE);
    List<RunningAppProcessInfo> appProcesses = null;
    try {
      appProcesses = activityManager.getRunningAppProcesses();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (appProcesses == null) {
      return false;
    }
    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//			Logger.log("process processName:"+appProcess.processName);
      if (appProcess.processName.equals(processName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断主进程还在不在系统最近任务栏里面，有可能会用户强行划掉
   */
  public static boolean isProcessInRecentTasks(Context context, String processName) {
    if (context == null || processName == null) {
      return true;
    }
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (am == null) {
      return true;
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      List<AppTask> tasks = am.getAppTasks();

      if (tasks == null || tasks.isEmpty()) {
        return false;
      }

      for (ActivityManager.AppTask task : tasks) {
        Logger.log("process RecentTaskInfo processName0:" +
            task.getTaskInfo().baseIntent.getComponent().getPackageName());

        ActivityManager.RecentTaskInfo info = null;
        if (task != null) {
          info = task.getTaskInfo();
        }

        if (info != null && info.baseIntent != null && info.baseIntent.getComponent() != null
            && processName.equals(info.baseIntent.getComponent().getPackageName())) {
          return true;
        }

      }
      return false;
    } else {

      List<RecentTaskInfo> list = am.getRecentTasks(100, ActivityManager.RECENT_IGNORE_UNAVAILABLE);

      if (list == null || list.isEmpty()) {
        return false;
      }

      for (int i = 0; i < list.size(); i++) {
        ActivityManager.RecentTaskInfo info = list.get(i);
        Logger.log("process RecentTaskInfo processName1:" + info.baseIntent.getComponent()
            .getPackageName());
        if (info.baseIntent != null && info.baseIntent.getComponent() != null
            && processName.equals(info.baseIntent.getComponent().getPackageName())) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * 判断MainActivity是否活动
   *
   * @param context 一个context
   * @param activityName 要判断Activity
   * @return boolean
   */
  public static boolean isActivityAlive(Context context, String activityName) {
    if (context == null) {
      return false;
    }

    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<RunningTaskInfo> list = am.getRunningTasks(100);
    for (ActivityManager.RunningTaskInfo info : list) {
      // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
      if (info.topActivity.toString().equals(activityName) || info.baseActivity.toString()
          .equals(activityName)) {
//				Log.i(TAG,info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
        return true;
      }
    }
    return false;
  }

  public static boolean isPushProcess(Context context) {
    String processName = getCurProcessName(context);
    if (!TextUtils.isEmpty(processName) && processName.contains("pushservice")) {
      return true;
    }
    return false;
  }

  /**
   * 打印当前线程的调用堆栈
   */
  public static String printTrack() {
    StackTraceElement[] st = Thread.currentThread().getStackTrace();
    if (st == null) {
      return "无堆栈...";
    }
    StringBuffer sbf = new StringBuffer();
    for (StackTraceElement e : st) {
      if (sbf.length() > 0) {
        sbf.append(" <- ");
        sbf.append(System.getProperty("line.separator"));
      }
      sbf.append(java.text.MessageFormat.format("{0}.{1}() {2}"
          , e.getClassName()
          , e.getMethodName()
          , e.getLineNumber()));
    }
    return sbf.toString();
  }

  /***************************************************************************************/
  public static int dp2px(Context context, float dipValue) {
    if (context == null) {
      return (int) (dipValue * 1.5);
    }
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  public static float dp2pxReturnFloat(Context context, float dipValue) {
    if (context == null) {
      return dipValue * 1.5f;
    }
    final float scale = context.getResources().getDisplayMetrics().density;
    return dipValue * scale + 0.5f;
  }

  public static int sp2px(Context context, float dipValue) {
    if (context == null) {
      return (int) (dipValue * 1.5);
    }
    final float scale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (dipValue * scale + 0.5f);
  }

  public static int px2dip(Context context, float pxValue) {
    if (context == null) {
      return (int) (pxValue * 1.5);
    }
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static int px2sp(Context context, float pxValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  /**
   * get the width of the device screen
   */
  public static int getScreenWidth(Context context) {
    if (context == null) {
      return 1;
    }

    int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    int screenHeight = context.getResources().getDisplayMetrics().heightPixels;

    return screenWidth < screenHeight ? screenWidth : screenHeight;
  }

  /**
   * get the height of the device screen
   */
  public static int getScreenHeight(Context context) {
    if (context == null) {
      return 1;
    }

    int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    int screenHeight = context.getResources().getDisplayMetrics().heightPixels;

    return screenWidth > screenHeight ? screenWidth : screenHeight;
  }

  public static String geDeviceInfo(Context ctx) {
    StringBuffer deviceId = new StringBuffer();

    deviceId.append(SerialInfo.getPhoneModel()).append("/");

    deviceId.append(SerialInfo.getSDkVersion()).append("/");

    deviceId.append(SerialInfo.getVersionName(ctx));

    return deviceId.toString();
  }

  /**
   * 获取状态栏的高度
   */
  public static int mStatusBarHeight = 0;
  private static boolean mIsStatusBarHeightCached = false;

  public static int getStatusBarHeight(Context context) {
    if (context == null) {
      return 0;
    }
    if (mStatusBarHeight != 0) {
      return mStatusBarHeight;
    }
    try {
      Class<?> clazz = Class.forName("com.android.internal.R$dimen");
      Object obj = clazz.newInstance();
      Field field = clazz.getField("status_bar_height");
      int temp = Integer.parseInt(field.get(obj).toString());
      mStatusBarHeight = context.getResources()
          .getDimensionPixelSize(temp);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return mStatusBarHeight;
  }

  /**
   * 最前面页面的是否是本app
   */
  public static boolean isForegroundIsMyApplication(Context context) {
    if (context == null) {
      return false;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (context.getApplicationContext() instanceof BaseApplication) {
        BaseApplication baseApplication = (BaseApplication) context.getApplicationContext();
        return baseApplication.getAppCount() > 0 ? true : false;
      }
      return false;
    } else {

      ActivityManager am = (ActivityManager) context.getSystemService(Application.ACTIVITY_SERVICE);
      if (am == null) {
        return false;
      }
      List<RunningTaskInfo> runningTaskInfo = am.getRunningTasks(1);
      if (runningTaskInfo == null || runningTaskInfo.size() == 0) {
        return false;
      }
      RunningTaskInfo foregroundTaskInfo = runningTaskInfo.get(0);
      String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
      if (foregroundTaskPackageName.equals(context.getPackageName())) {
        return true;
      }
      return false;
    }

  }

  /**
   * 是否是平板
   */
  public static boolean isTabletDevice(Context context) {
    if (context == null) {
      return false;
    }
    return (context.getResources().getConfiguration().screenLayout
        & Configuration.SCREENLAYOUT_SIZE_MASK) >=
        Configuration.SCREENLAYOUT_SIZE_LARGE;
  }

  //字节转换为KB或MB
  public static String byteToMb(double bytes) {
    String result;
    double kb = bytes / 1024;
    if (kb > 1024) {
      double mb = kb / 1024;
      DecimalFormat df = new DecimalFormat("0.0");
      result = df.format(mb) + "MB/s";
      Log.e("byteToMb", "1");
    } else {
      DecimalFormat df = new DecimalFormat("0.0");
      result = df.format(bytes / 1024) + "KB/s";
      Log.e("byteToMb", "2");
    }
    Log.e("byteToMb", result);
    return result;
  }

  public static void getTileBarView(ViewGroup viewGroup, Context context, View[] result) {
    if (viewGroup == null) {
      return;
    }
    int count = viewGroup.getChildCount();
    for (int i = 0; i < count; i++) {
      View view = viewGroup.getChildAt(i);
      if (!TextUtils.isEmpty(view.getContentDescription())
          && view.getContentDescription()
          .equals(context.getString(R.string.framework_title_bar_contentDescription))) {
        result[0] = view;
        return;
      } else if (view instanceof ViewGroup) {
        getTileBarView((ViewGroup) view, context, result);
      }
    }
  }


  /**
   * 获取虚拟按键栏高度
   */
  public static int getNavigationBarHeight(Context context) {
    if (isMeizu()) {
      return getSmartBarHeight(context);
    }
    int result = 0;
    if (hasNavBar(context)) {
      Resources res = context.getResources();
      int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
      if (resourceId > 0) {
        result = res.getDimensionPixelSize(resourceId);
      }
    }
    return result;
  }

  /**
   * 检查是否存在虚拟按键栏
   */
  private static boolean hasNavBar(Context context) {
    if (context == null) {
      return false;
    }

    Resources res = context.getResources();
    int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
    if (resourceId != 0) {
      boolean hasNav = res.getBoolean(resourceId);
      // check override flag
      String sNavBarOverride = getNavBarOverride();
      if ("1".equals(sNavBarOverride)) {
        hasNav = false;
      } else if ("0".equals(sNavBarOverride)) {
        hasNav = true;
      }
      return hasNav;
    } else { // fallback
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        return !ViewConfiguration.get(context).hasPermanentMenuKey();
      } else {
        return false;
      }
    }
  }

  /**
   * 判断虚拟按键栏是否重写
   */
  private static String getNavBarOverride() {
    String sNavBarOverride = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      try {
        Class c = Class.forName("android.os.SystemProperties");
        Method m = c.getDeclaredMethod("get", String.class);
        m.setAccessible(true);
        sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
      } catch (Throwable e) {
      }
    }
    return sNavBarOverride;
  }

  /**
   * 判断是否meizu手机
   */
  public static boolean isMeizu() {
    return Build.BRAND.equals("Meizu");
  }

  /**
   * 获取魅族手机底部虚拟键盘高度
   */
  public static int getSmartBarHeight(Context context) {
    if (context == null) {
      return 0;
    }

    try {
      Class c = Class.forName("com.android.internal.R$dimen");
      Object obj = c.newInstance();
      Field field = c.getField("mz_action_button_min_height");
      int height = Integer.parseInt(field.get(obj).toString());
      return context.getResources().getDimensionPixelSize(height);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }


  public static PackageInfo getPackageArchiveInfo(PackageManager packageManager,
      String archiveFilePath, int flags) {
    // Workaround for https://code.google.com/p/android/issues/detail?id=9151#c8

    return packageManager.getPackageArchiveInfo(archiveFilePath, flags);
  }

  public static boolean verifyPluginFileSignature(Context context, String pluginFilepath) {
    File pluginFile = new File(pluginFilepath);
    if (!pluginFile.exists()) {
      return false;
    }

    try {
      PackageManager packageManager = context.getPackageManager();
      PackageInfo newInfo = getPackageArchiveInfo(packageManager, pluginFilepath,
          PackageManager.GET_ACTIVITIES | PackageManager.GET_SIGNATURES);
      PackageInfo mainPkgInfo = packageManager.getPackageInfo(
          context.getPackageName(), PackageManager.GET_SIGNATURES);
      return checkSignatures(newInfo, mainPkgInfo) == PackageManager.SIGNATURE_MATCH;
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return false;
  }

  private static int checkSignatures(PackageInfo pluginPackageInfo,
      PackageInfo mainPackageInfo) {

    Signature[] pluginSignatures = pluginPackageInfo.signatures;
    Signature[] mainSignatures = mainPackageInfo.signatures;
    boolean pluginSigned = pluginSignatures != null
        && pluginSignatures.length > 0;
    boolean mainSigned = mainSignatures != null
        && mainSignatures.length > 0;

    if (pluginSignatures != null && mainSignatures != null) {
      Logger.d("checkSignatures ", pluginSignatures.length + "  " + mainSignatures.length);
    }

    if (!pluginSigned && !mainSigned) {
      return PackageManager.SIGNATURE_NEITHER_SIGNED;
    } else if (!pluginSigned && mainSigned) {
      return PackageManager.SIGNATURE_FIRST_NOT_SIGNED;
    } else if (pluginSigned && !mainSigned) {
      return PackageManager.SIGNATURE_SECOND_NOT_SIGNED;
    } else {
      if (pluginSignatures.length == mainSignatures.length) {
        for (int i = 0; i < pluginSignatures.length; i++) {
          Signature s1 = pluginSignatures[i];
          Signature s2 = mainSignatures[i];
          if (!Arrays.equals(s1.toByteArray(), s2.toByteArray())) {
            return PackageManager.SIGNATURE_NO_MATCH;
          }
        }
        return PackageManager.SIGNATURE_MATCH;
      } else {
        return PackageManager.SIGNATURE_NO_MATCH;
      }
    }
  }

  public static boolean activityIsLive(Activity activity) {
    if (activity == null) {
      return false;
    }
    if (activity.isFinishing()) {
      return false;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      if (activity.isDestroyed()) {
        return false;
      }
    }
    return true;
  }

  private static int startCount = 0;
  public static boolean hasReadStart = false;

  /**
   * 是否是第一次安装软件
   */
  public static boolean isFirstInstallApp(Context context) {
    if (!hasReadStart) {
      SharedPreferencesUtil sharedPreferencesUtil = SharedPreferencesUtil.getInstance(context);
      startCount = sharedPreferencesUtil.getInt(PreferenceConstants.KEY_APP_START_COUNT, 0);
      sharedPreferencesUtil.saveInt(PreferenceConstants.KEY_APP_START_COUNT, startCount + 1);
      hasReadStart = true;
      return startCount == 0;
    } else {
      return startCount == 0;
    }
  }

  public static int getSceenWidthForDp(Context context) {
    if (context == null) {
      return 1;
    }
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    int width = dm.widthPixels;// 屏幕宽度（像素）
    float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
    //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
    int screenWidth = (int) (width / density);//屏幕宽度(dp)
    return screenWidth;
  }

  public static int getRealHeight(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    if (wm == null) {
      return 0;
    }

    Display display = wm.getDefaultDisplay();
    int screenHeight = 0;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      DisplayMetrics dm = new DisplayMetrics();
      display.getRealMetrics(dm);
      screenHeight = dm.heightPixels;
    }

    return screenHeight;
  }
}
