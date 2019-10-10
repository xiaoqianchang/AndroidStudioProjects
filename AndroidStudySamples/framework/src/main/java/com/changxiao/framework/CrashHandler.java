package com.changxiao.framework;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.changxiao.framework.constants.Constants;
import com.changxiao.framework.util.BaseUtil;
import com.changxiao.framework.util.SerialInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {

  /**
   * 获取CrashHandler实例 ,单例模式
   */
  public static CrashHandler getInstance() {

    if (crashHandler == null) {
      synchronized (CrashHandler.class) {
        crashHandler = new CrashHandler();
      }
    }
    return crashHandler;

  }

  private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
  private static CrashHandler crashHandler;// CrashHandler实例
  private Context mContext;// 程序的Context对象

  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");

  /**
   * 保证只有一个CrashHandler实例
   */
  private CrashHandler() {

  }

  public File getLogFilePath(Context ctx) {
    String sdStatus = Environment.getExternalStorageState();
    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
      return null;
    }

    String pathName = Environment.getExternalStorageDirectory().getPath()
        + "/Android/data/" + ctx.getPackageName()
        + "/files/error/"
        + SerialInfo.getPhoneModel() + "_"
        + SerialInfo.getVersionName(ctx) + simpleDateFormat.format(new Date()) + ".log";

    File path = new File(pathName);
    if (!path.getParentFile().exists()) {
      path.getParentFile().mkdirs();
    }

    return path;
  }

  /**
   * 初始化
   */
  public void init(Context context) {
    mContext = context;
    mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
    Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
  }

  /**
   * 当UncaughtException发生时会转入该重写的方法来处理
   */
  public void uncaughtException(Thread thread, Throwable ex) {
    if (thread == null || ex == null || mDefaultHandler == null) {
      System.exit(0);
      return;
    }

    if (Constants.isDebug) {
      writeLog(thread, ex);
    }
    mDefaultHandler.uncaughtException(thread, ex);
  }

  private void writeLog(Thread thread, Throwable ex) {
    File savePathFile = getLogFilePath(mContext);

    String logMessage = String
        .format("CustomUncaughtExceptionHandler.uncaughtException: Thread %d Message %s",
            thread.getId(), ex.getMessage());
    PrintWriter printWriter = null;

    try {
      printWriter = new PrintWriter(new FileWriter(savePathFile, true));

      logMessage = String
          .format(
              "%s\r\n\r\n%s\r\n\r\nThread: %d\r\n\r\nMessage:\r\n\r\n%s\r\n\r\nStack Trace:\r\n\r\n%s",
              BaseUtil.geDeviceInfo(mContext), new Date(),
              thread.getId(), ex.getMessage(),
              Log.getStackTraceString(ex));

      printWriter.print(logMessage);
      printWriter.print(
          "\n\n---------------------------------------------------------------------------\n\n");
    } catch (Throwable tr2) {
    } finally {
      if (printWriter != null) {
        printWriter.close();
      }
    }
  }
}
