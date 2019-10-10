package com.changxiao.freemarkerdemo.http;

import com.changxiao.freemarkerdemo.bean.BaseData;

import java.util.Map;

import retrofit2.Callback;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Net api
 * <p>
 * Created by Chang.Xiao on 2016/5/30.
 *
 * @version 1.0
 */
public interface ZRNetApi {

    @GET(value = "data/福利/" + 10 + "/{page}")
    Observable<Object> getData(@Path("page") int page);
}
