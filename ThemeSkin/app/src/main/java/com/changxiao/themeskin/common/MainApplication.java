package com.changxiao.themeskin.common;

import android.app.Application;
import android.content.Context;

import cn.feng.skin.manager.loader.SkinManager;
import com.changxiao.themeskin.BuildConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Custom application.
 * <p>
 * Created by Chang.Xiao on 2016/7/1.
 *
 * @version 1.0
 */
public class MainApplication extends Application {

    private static final String TAG = "QuickFrame";

    public static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
        LeakCanary.install(this);

        SkinManager.getInstance().init(this);
//        SkinManager.getInstance().load();

        // Logger Settings
        // Note: Use LogLevel.NONE for the release versions.
        Logger
                .init(TAG)                       // default PRETTYLOGGER or use just init()
                .methodCount(2)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE) // default LogLevel.FULL
                .methodOffset(2);               // default 0

    }
}
