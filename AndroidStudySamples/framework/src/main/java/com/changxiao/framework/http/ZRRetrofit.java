package com.changxiao.framework.http;

import com.changxiao.framework.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public static final int PAGE_SIZE = 20;

    private static ZRRetrofit mInstance;
    private static Retrofit.Builder builder;
    private static ZRNetApi mNetApi;
    protected static final Object monitor = new Object();
    private static OkHttpClient.Builder okHttpBuilder;
    private static OkHttpClient client;
    private static Gson gson;

    private static FileUploadApi mFileUploadApi; // 文件上传

    private ZRRetrofit() {

    }

    public static ZRRetrofit getInstance() {
        if (null == mInstance) {
            mInstance = new ZRRetrofit();
        }
        return mInstance;
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
//                    .addHeader("Connection", "close")
                    .build();
            return chain.proceed(authorised);
        }
    };

    static {
        // 证书
//        int[] certficates = new int[] {R.raw.duocaijr_com_crt};
        // OkHttp3
        okHttpBuilder = new OkHttpClient().newBuilder();
        if (Constants.isDebug) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(interceptor);
            // print Log
//            okHttpBuilder.addInterceptor(new LoggingInterceptor());
        }

        okHttpBuilder.retryOnConnectionFailure(false);
        okHttpBuilder.readTimeout(10, TimeUnit.SECONDS);
        okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS);
        okHttpBuilder.addNetworkInterceptor(mTokenInterceptor);
        // https支持
//        okHttpBuilder.sslSocketFactory(SSLClientVerify.getSSLSocketFactory(ZRApplication.applicationContext, certficates));
        client = okHttpBuilder.build();
        /*client = new OkHttpClient.Builder() // 这种方式属性有默认值
                // print Log
//                .addInterceptor(interceptor)
//                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new LoggingInterceptor())
//                .addNetworkInterceptor(new LogInterceptor())
                // 设置出现错误进行重新连接(慎用)
                .retryOnConnectionFailure(false)
                // set time out interval
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                // 所有网络请求都附上你的拦截器
                .addNetworkInterceptor(mTokenInterceptor)
                .build();*/

        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        builder = new Retrofit.Builder();
    }

    /**
     * 设置连接超时时间
     *
     * @param timeOut
     */
    public void setConnectTimeout(int timeOut) {
        okHttpBuilder.connectTimeout(timeOut, TimeUnit.SECONDS);
    }

    public void setReadTimeout(int timeOut) {
        okHttpBuilder.readTimeout(timeOut, TimeUnit.SECONDS);
    }

    /**
     * Get NetApi instance
     *
     * @return
     */
    public static ZRNetApi getNetApiInstance() {
        return getNetApiInstance(Constants.SERVER_URL);
    }

    /**
     * Get NetApi instance
     *
     * @param serverUrl
     * @return
     */
    public static ZRNetApi getNetApiInstance(String serverUrl) {
        synchronized (monitor) {
            Retrofit retrofit = builder
                    .client(client)
                    .baseUrl(serverUrl) // 注意：retrofit2.0后：BaseUrl要以/结尾；@GET 等请求不要以/开头；@Url: 可以定义完整url，不要以 / 开头。
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mNetApi = retrofit.create(ZRNetApi.class);
            return mNetApi;
        }
    }

    /**
     * FileUpoad NetApi instance
     *
     * @return
     */
    public static FileUploadApi getFileUploadApiInstance() {
        return getFileUploadApiInstance(Constants.SERVER_URL);
    }

    public static FileUploadApi getFileUploadApiInstance(String serverUrl) {
        synchronized (monitor) {
            if (null == mFileUploadApi) {
                Retrofit retrofit = builder
                        .client(client)
                        .baseUrl(serverUrl + "/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                mFileUploadApi = retrofit.create(FileUploadApi.class);
            }
            return mFileUploadApi;
        }
    }

}
