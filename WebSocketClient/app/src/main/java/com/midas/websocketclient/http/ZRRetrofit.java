package com.midas.websocketclient.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.midas.websocketclient.utils.AppConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求对象
 * <p>
 * Created by Chang.Xiao on 2016/5/30.
 *
 * @version 1.0
 */
public class ZRRetrofit {

    private static Retrofit.Builder builder;
    private static ZRNetApi mNetApi;
    protected static final Object monitor = new Object();
    private static OkHttpClient client;
    private static Gson gson;

    private ZRRetrofit() {

    }

    /**
     * 拦截器，给请求头添加参数
     */
    static Interceptor mTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            // 参加参数
            Request authorised = originalRequest.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("charset", "utf-8")
                    .build();
            return chain.proceed(authorised);
        }
    };

    static {
        // OkHttp3
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                // print Log
                .addInterceptor(interceptor)
                // 设置出现错误进行重新连接
                .retryOnConnectionFailure(true)
                // set time out interval
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                // 所有网络请求都附上你的拦截器
                .addNetworkInterceptor(mTokenInterceptor)
                .build();

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        builder = new Retrofit.Builder();
    }

    /**
     * Get NetApi instance
     *
     * @return
     */
    public static ZRNetApi getNetApiInstance() {
        return getNetApiInstance(AppConfig.SERVER_URL);
    }

    /**
     * Get NetApi instance
     *
     * @param serverUrl
     * @return
     */
    public static ZRNetApi getNetApiInstance(String serverUrl) {
        synchronized (monitor) {
            if (null == mNetApi) {
                Retrofit retrofit = builder
                        .client(client)
                        .baseUrl(serverUrl)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                mNetApi = retrofit.create(ZRNetApi.class);
            }
            return mNetApi;
        }
    }

}
