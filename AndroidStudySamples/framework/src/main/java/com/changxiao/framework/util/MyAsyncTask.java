package com.changxiao.framework.util;

import android.os.AsyncTask;
import com.changxiao.framework.constants.Constants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MyAsyncTask<Params, Progress, Result> extends
    AsyncTask<Params, Progress, Result> {

  public static String TAG = "MyAsyncTask";

  private static final BlockingQueue<Runnable> mPoolWorkQueue =
      new LinkedBlockingQueue<Runnable>();

  private static final ThreadFactory mThreadFactory = new ThreadFactory() {
    private final AtomicInteger mCount = new AtomicInteger(1);

    public Thread newThread(Runnable r) {
      return new Thread(r, "MyAsyncTask #" + mCount.getAndIncrement());
    }
  };

  private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
  private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
  private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
  private static final int KEEP_ALIVE_SECONDS = 30;

  public static final ThreadPoolExecutor MY_THREAD_POOL_EXECUTOR;

  static {
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAXIMUM_POOL_SIZE,
        KEEP_ALIVE_SECONDS,
        TimeUnit.SECONDS,
        mPoolWorkQueue,
        mThreadFactory);

    threadPoolExecutor.allowCoreThreadTimeOut(true);
    MY_THREAD_POOL_EXECUTOR = threadPoolExecutor;
  }

  public final MyAsyncTask<Params, Progress, Result> myexec(Params... params) {
    Logger.i("MyAsyncTask", "MyAsyncTask --- myexec " + this);
    handleThreadException();
    executeOnExecutor(MY_THREAD_POOL_EXECUTOR, params);
    return this;
  }

  public static void execute(Runnable runnable) {
    handleThreadException();
    MY_THREAD_POOL_EXECUTOR.execute(runnable);
  }

  /**
   * 不是关键任务，当任务过重的时候可以抛弃
   *
   * @param canIgnore 是否可以抛弃
   */
  public static void execute(Runnable runnable, boolean canIgnore) {
    handleThreadException();
    int size = MY_THREAD_POOL_EXECUTOR.getQueue().size();
    if (size < 10 || !canIgnore) {
      execute(runnable);
    }
  }

  private static void handleThreadException() {

    if (MY_THREAD_POOL_EXECUTOR == null || MY_THREAD_POOL_EXECUTOR.isShutdown()) {
      throw new RuntimeException(
          "MY_THREAD_POOL_EXECUTOR is shutdown by unknown reason MY_THREAD_POOL_EXECUTOR:"
              + MY_THREAD_POOL_EXECUTOR);
    }

    if (!Constants.isDebug) {
      return;
    }
    if (!isCheckOpen()) {
      return;
    }
    int size = MY_THREAD_POOL_EXECUTOR.getQueue().size();
    long completeCount = MY_THREAD_POOL_EXECUTOR.getCompletedTaskCount();
    long totalConut = MY_THREAD_POOL_EXECUTOR.getTaskCount();
    Logger.i(TAG,
        "MyAsyncTask1 workQueue size:" + size + " completeCount:" + completeCount + " totalConut:"
            + totalConut);
    if (size > 10) {
      throw new RuntimeException(
          "警告:too many MyAsyncTask,注意检查线程池是否放入过多的耗时任务 size:" + size + " All Thread Stack:"
              + dumpAllThreadStack());
    }
  }

  public static String dumpAllThreadStack() {

    Map liveThreads = Thread.getAllStackTraces();

    if (liveThreads == null) {
      return null;
    }

    StringBuilder msg = new StringBuilder();
    for (Iterator i = liveThreads.keySet().iterator(); i.hasNext(); ) {
      Thread thread = (Thread) i.next();
      if (thread == null) {
        continue;
      }
      long tid = thread.getId();
      msg.append(
          "thread id: " + tid + ", name: " + thread.getName() + ", state:" + thread.getState()
              + ", priority:" + thread.getPriority()).append("\n");
      StackTraceElement[] stackTraces = thread.getStackTrace();
      if (stackTraces == null) {
        continue;
      }
      for (StackTraceElement stackTrace : stackTraces) {
        msg.append("\t").append(stackTrace).append("\n");
      }
    }
    return msg.toString();
  }

  /**
   * --------------- for debug -----------------
   */

  private static Class<?> testActivity;
  private static Method checkMethod;

  private static boolean isCheckOpen() {
    long start = System.currentTimeMillis();
    Logger.d(TAG, "Check start ");
    if (testActivity != null && checkMethod != null) {
      boolean open = true;
      try {
        open = (boolean) checkMethod.invoke(null);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      Logger.d(TAG, "isCheckPoolOpen " + open);
      Logger.d(TAG, "check success  consume time = " + (System.currentTimeMillis() - start));
      return open;
    }
    try {
      testActivity = Class.forName("com.ximalaya.ting.android.main.activity.test.TestActivity");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      if (testActivity != null) {
        try {
          checkMethod = testActivity.getMethod("isCheckPoolOpen");
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        } finally {
          if (checkMethod != null) {
            checkMethod.setAccessible(true);
          }
        }
      }
    }
    Logger.d(TAG, "Check fail consume time = " + (System.currentTimeMillis() - start));
    return true;
  }
}
