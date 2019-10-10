package com.changxiao.rxjavatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSocketLive();
    }

    private void checkSocketLive() {
        Subscriptions.unsubscribed();
        final AtomicReference<Subscription> subscriptionAtomicReference = new AtomicReference<>();
        subscriptionAtomicReference.set(
                Observable.interval(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                Log.i("INTERVAL", String.valueOf(aLong));
                                subscriptionAtomicReference.get().unsubscribe(); // 解除订阅
                            }
                        })
//                        .publish()
//                        .autoConnect()
                        .subscribe()
        );
    }
}
