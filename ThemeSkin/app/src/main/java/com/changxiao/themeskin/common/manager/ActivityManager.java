package com.changxiao.themeskin.common.manager;

import android.app.Activity;

import com.changxiao.themeskin.activity.MainActivity;
import com.changxiao.themeskin.common.MainApplication;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jack.qin on 2017/3/1.
 */

public class ActivityManager {
    public static List<WeakReference<Activity>> ACTIVITY_STACK = new ArrayList<>();
    private static final String TAG = "ActivityManagerStack";

    public synchronized static void addActivity(Activity activity) {
        boolean hasInsert = false;
        Iterator<WeakReference<Activity>> iterator = ACTIVITY_STACK.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> weakReference = iterator.next();
            if (weakReference != null) {
                Activity activity1 = weakReference.get();
                if (activity1 != null) {
                    if (activity1 == activity) {
                        hasInsert = true;
                    }
                }
            }
        }
        if(!hasInsert) {
            ACTIVITY_STACK.add(new WeakReference<Activity>(activity));
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (ACTIVITY_STACK.size() == 0)
            return null;
        WeakReference<Activity> weakReference = ACTIVITY_STACK.get(ACTIVITY_STACK.size() - 1);
        if (weakReference != null) {
            Activity activity = weakReference.get();
            return activity;
        }
        return null;
    }

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

    public synchronized static void removeActivity(Activity activity) {
        Iterator<WeakReference<Activity>> iterator = ACTIVITY_STACK.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> weakReference = iterator.next();
            if (weakReference != null) {
                Activity activity1 = weakReference.get();
                if (activity1 != null) {
                    if (activity1 == activity) {
                        iterator.remove();
                    }
                }
            }
        }
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
                    if(activity instanceof MainActivity) {
                        isMainActivityAlive = true;
                        MainActivity mainActivity = (MainActivity) activity;
                        mainActivity.finish();
                    }else {
                        activity.finish();
                    }
                }
            }
        }
        if(!isMainActivityAlive) {
//            context.exit();
        }
    }
}
