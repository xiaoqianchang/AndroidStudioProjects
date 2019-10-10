package com.changxiao.freemarkerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.changxiao.freemarkerdemo.bean.BaseData;
import com.changxiao.freemarkerdemo.http.ZRRetrofit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_request)
    Button btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_request)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_request:
                ZRRetrofit.getNetApiInstance().getData(10)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object object) {
                                Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}
