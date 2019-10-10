package com.changxiao.framework.common.manager;

import android.app.Activity;
import com.changxiao.framework.util.Logger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jack.qin on 2017/3/1.
 */

public class ActivityManager {
    public static List<WeakReference<Activity>> ACTIVITY_STACK = new ArrayList<>();
    protected static final String TAG = "ActivityManagerStack";

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
            Logger.i(TAG, "add activity " + activity.getClass());
            ACTIVITY_STACK.add(new WeakReference<Activity>(activity));
        }
    }

    public synchronized static void removeActivity(Activity activity) {
        Iterator<WeakReference<Activity>> iterator = ACTIVITY_STACK.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> weakReference = iterator.next();
            if (weakReference != null) {
                Activity activity1 = weakReference.get();
                if (activity1 != null) {
                    if (activity1 == activity) {
                        Logger.i(TAG, "remove activity " + activity.getClass());
                        iterator.remove();
                    }
                }
            }
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
}
