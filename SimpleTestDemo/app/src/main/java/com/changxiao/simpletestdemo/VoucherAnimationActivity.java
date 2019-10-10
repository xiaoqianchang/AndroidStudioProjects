package com.changxiao.simpletestdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

public class VoucherAnimationActivity extends AppCompatActivity {

    private ImageView mIvVoucherLogo;

    private Handler handleShake = new ShakeHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_animation);

        mIvVoucherLogo = (ImageView) findViewById(R.id.main_iv_voucher_logo);
//        beginAnimation();

        handleShake.sendEmptyMessage(0);

//        mIvVoucherLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake1));

//        image2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake2));
    }

    private void beginAnimation() {
        TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(100);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        mIvVoucherLogo.startAnimation(animation);
    }

    /**
     * static handler
     */
    private static class ShakeHandler extends Handler {

        private SoftReference<VoucherAnimationActivity> mRef;
        private Animation mAnimationShake;

        ShakeHandler(VoucherAnimationActivity fragment) {
            mRef = new SoftReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {

            if (mRef == null)
                return;

            final VoucherAnimationActivity theFragment = mRef.get();

            if (theFragment == null)
                return;

            if (mAnimationShake == null) {
                mAnimationShake = AnimationUtils.loadAnimation(theFragment, R.anim.rotate);
                mAnimationShake.setInterpolator(new CycleInterpolator(1));
                mAnimationShake.setDuration(1000);
                mAnimationShake.setRepeatCount(-1);
            }
            theFragment.mIvVoucherLogo.startAnimation(mAnimationShake);

            mAnimationShake.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (theFragment.mIvVoucherLogo.getAnimation() == animation) {
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }
}
