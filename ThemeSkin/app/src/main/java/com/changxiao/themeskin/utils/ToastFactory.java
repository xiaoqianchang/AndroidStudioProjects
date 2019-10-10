package com.changxiao.themeskin.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.changxiao.themeskin.common.MainApplication;

/**
 * ToastFactory
 * <p>
 * Created by Chang.Xiao on 2017/6/15.
 *
 * @version 1.0
 */
public class ToastFactory {

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private static class ToastTask implements Runnable {
        private Context mCtx;
        private String mContent;
        private int mDuration;

        public ToastTask(Context mContext, String text, int duration) {
            mCtx = mContext;
            mContent = text;
            if (duration == 0) {
                mDuration = Toast.LENGTH_SHORT;
            } else {
                mDuration = Toast.LENGTH_LONG;
            }
        }

        @Override
        public void run() {
            if (mCtx != null)
                Toast.makeText(mCtx, mContent, mDuration).show();
        }

    }

    public static void showToast(String text) {
//        CustomToast.showToast(text);
        showToast(null, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
//        CustomToast.showToast(resId);
        showToast(null, resId, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context mContext, String text, int duration) {
        Context ctx = mContext;
        if (ctx == null) {
            ctx = MainApplication.mApplicationContext;
        }

        if (ctx == null) {
            return;
        }

        mHandler.post(new ToastTask(ctx, text, duration));
    }

    public static void showToast(Context mContext, int resId, int duration) {
        Context ctx = mContext;
        if (ctx == null) {
            ctx = MainApplication.mApplicationContext;
        }

        if (ctx == null) {
            return;
        }
        mHandler.post(new ToastTask(ctx, ctx.getResources().getString(resId), duration));
    }
}
