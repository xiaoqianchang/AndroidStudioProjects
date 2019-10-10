package com.midas.websocketclient.common;

import android.app.Application;
import android.content.Context;

import com.midas.websocketclient.utils.ZRHelper;

/**
 * $desc$
 * <p/>
 * Created by Chang.Xiao on 2016/5/17.
 *
 * @version 1.0
 */
public class AppFrameworkController extends Application {

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        ZRHelper.getInstance().onInit(applicationContext);
    }
}
