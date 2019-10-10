package com.changxiao.simpletestdemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.changxiao.simpletestdemo.flipcard.FlipCardAnimation;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mTvUpdateNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置 更新数 入场退场动画
        ViewGroup hintContainer = (ViewGroup)findViewById(R.id.main_fl_hint_container);
        LayoutTransition transition = new LayoutTransition();
        int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());// BaseUtil.dp2px(getActivity(), 30);
        ObjectAnimator inAnimator = ObjectAnimator.ofFloat(null, "translationY", -viewHeight, 0);
        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(null, "translationY", 0, -viewHeight);
        transition.setAnimator(LayoutTransition.APPEARING, inAnimator);
        transition.setAnimator(LayoutTransition.DISAPPEARING, outAnimator);
        hintContainer.setLayoutTransition(transition);

        mTvUpdateNum = (TextView)findViewById(R.id.main_tv_update_num);
        mTvUpdateNum.postDelayed(new Runnable() {
            @Override
            public void run() {
                showUpdateNum(20);
            }
        }, 3000);



        ImageView bgLoading = (ImageView) findViewById(R.id.iv_loading);
        Drawable drawable = bgLoading.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }

        TextView viewById = (TextView) findViewById(R.id.tv_bg_loading);
        Drawable drawable1 = viewById.getCompoundDrawables()[0];
        if (drawable1 instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable1).start();
        }

        final TextView liveStatus = (TextView) findViewById(R.id.tv_animation_view);
        final LottieDrawable lottieDrawable = new LottieDrawable();
        String lottiePath = "live_ic_gif_play.json";
        LottieComposition.Factory.fromAssetFileName(this, lottiePath, new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                lottieDrawable.setComposition(composition);
                lottieDrawable.setScale(0.5f);
                liveStatus.setText("正在直播");
                lottieDrawable.setBounds(0, 0, lottieDrawable.getMinimumWidth(), lottieDrawable.getMinimumHeight());
                liveStatus.setCompoundDrawables(lottieDrawable, null, null, null);
                liveStatus.setCompoundDrawablePadding(30);
//                ImageView iv_animation = (ImageView) findViewById(R.id.iv_animation);
//                iv_animation.setImageDrawable(lottieDrawable);
                lottieDrawable.loop(true);
            }
        });
        liveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lottieDrawable.isAnimating()) {
                    lottieDrawable.cancelAnimation();
                } else {
                    lottieDrawable.cancelAnimation();
                    liveStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MainActivity.this, R.drawable.cat_loading_1), null, null, null);
                    liveStatus.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lottieDrawable.setBounds(0, 0, lottieDrawable.getMinimumWidth(), lottieDrawable.getMinimumHeight());
                            liveStatus.setCompoundDrawables(lottieDrawable, null, null, null);
                            liveStatus.setCompoundDrawablePadding(30);
                            lottieDrawable.playAnimation();
                        }
                    }, 3000);
                }
            }
        });

        LottieAnimationView animation_view = (LottieAnimationView) findViewById(R.id.animation_view);
        animation_view.playAnimation();

        initFlipCard();

        TextView tv_auto_fit_text = (TextView) findViewById(R.id.tv_auto_fit_text);
        tv_auto_fit_text.setText(getStr());

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(MainActivity.this, LinkedScrollActivity.class));
//            }
//        }, 1000);

        Toast.makeText(this, "sssssssssssssssssssssssssssssssssssssssssssssss", Toast.LENGTH_LONG).show();

        SharedPreferences xc = getSharedPreferences("xc", 0);
        long lastShowTime = xc.getLong("time", System.currentTimeMillis());
        int dayDuration = getDayDuration(lastShowTime);
        SharedPreferences.Editor edit = xc.edit();
        edit.putLong("time", System.currentTimeMillis());
        edit.apply();
        Log.e("xc" , subZeroAndDot(new Double(2.99888), 2));

    }

    private static int getDayDuration(long lastShowTime) {
        long currentTimeMillis = System.currentTimeMillis();
        return (int) ((currentTimeMillis - lastShowTime) / 1000 / 60 / 60 / 24);
    }

    private String getStr() {
        return "123";
    }

    public void jumpToVoucherAnimation(View view) {
        startActivity(new Intent(this, VoucherAnimationActivity.class));
    }

    public void verticalReversal(View view) {
//        ObjectAnimator rotationY = ObjectAnimator.ofFloat(view, "rotationX", -90f, 0f);
//        rotationY.setStartDelay(150);
//        rotationY.setDuration(600);
//        rotationY.start();

//        flipCard(tv_flip);

        startAnimation(animation_item, tv_flip, 180);
    }

    private TextView tv_flip;
    private boolean mIsShowBack;
    private AnimatorSet mRightOutSet; // 右出动画
    private AnimatorSet mLeftInSet; // 左入动画

    private void initFlipCard() {
        tv_flip = (TextView) findViewById(R.id.tv_flip);
        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离
    }

    // 设置动画
    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_in);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
//                mFlContainer.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                mFlContainer.setClickable(true);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        tv_flip.setCameraDistance(scale);
    }

    // 翻转卡片
    public void flipCard(View view) {
        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(tv_flip);
            mLeftInSet.setTarget(tv_flip);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
        } else { // 背面朝上
            mRightOutSet.setTarget(tv_flip);
            mLeftInSet.setTarget(tv_flip);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
        }
    }

    public void jumpToLocalWeb(View view) {
        startActivity(new Intent(this, LocalWebActivity.class));
    }





    /****************方法三 flipcaeds ****************/
    FlipCardAnimation animation_item;
    FlipCardAnimation animation_item1;
    private void startAnimation(FlipCardAnimation animation, final TextView llyt_item, int degree) {
        if (animation != null) {
            animation.setCanContentChange();
            llyt_item.startAnimation(animation);
        } else {
            int width = llyt_item.getWidth() / 2;
            int height = llyt_item.getHeight() / 2;
            animation = new FlipCardAnimation(0, degree, width, height);
//            animation.setInterpolator(new AnticipateOvershootInterpolator());
            animation.setInterpolator(new LinearInterpolator());
//            animation.setDuration(3000);
            animation.setDuration(500);
            animation.setFillAfter(false);
//            animation.setRepeatCount(-1);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                    ((FlipCardAnimation)animation).setCanContentChange();
                }
            });
            animation.setOnContentChangeListener(new FlipCardAnimation.OnContentChangeListener() {
                @Override
                public void contentChange() {
                    llyt_item.setText("￥" + new Random().nextInt(500));
                }
            });
            llyt_item.startAnimation(animation);
        }

        Animation animation1 = new Animation() {
            @Override
            public void initialize(int width, int height, int parentWidth, int parentHeight) {
                super.initialize(width, height, parentWidth, parentHeight);
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
            }
        };
    }

    // 条数控件显示方法
    private void showUpdateNum(int num){
        String updateNum;
        if(num > 0){
            updateNum =  String.format(Locale.US, "为您更新了%d条内容", num);
        }else {
            updateNum = "暂无更多内容";
        }
        mTvUpdateNum.setText(updateNum);
        mTvUpdateNum.setVisibility(View.VISIBLE);
        mGoneUpdateNumTaskKey = System.currentTimeMillis();
        GoneUpdateNumTask task = new GoneUpdateNumTask(this, mGoneUpdateNumTaskKey);
        mTvUpdateNum.postDelayed(task, 3000);
    }

    private long mGoneUpdateNumTaskKey;

    // 条数控件延时消失方法
    private static class GoneUpdateNumTask implements Runnable{
        private WeakReference<MainActivity> mRef;
        private long mTaskKey;

        GoneUpdateNumTask(MainActivity fragment, long taskKey){
            mRef = new WeakReference<>(fragment);
            mTaskKey = taskKey;
        }

        @Override
        public void run() {
            MainActivity fragment = mRef.get();
            if(fragment == null){
                return;
            }

            if(fragment.mGoneUpdateNumTaskKey == mTaskKey){
                fragment.mTvUpdateNum.setVisibility(View.GONE);
            }
        }
    }

    public String subZeroAndDot(double number, int scale) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(scale);
        return df.format(number);
    }

    public static double getDecimalDoubleByFormat(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "0";
        }
        return Double.parseDouble(str);
    }
}
