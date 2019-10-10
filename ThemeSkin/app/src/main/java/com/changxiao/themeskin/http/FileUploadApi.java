package com.changxiao.themeskin.http;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * 文件上传
 * <p>
 * Created by Chang.Xiao on 2016/9/14.
 *
 * @version 1.0
 */
public interface FileUploadApi {

    @Multipart
    @POST
    Call<ResponseBody> uploadAvatar(@Url String url, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST
    Call<Object> uploadImage(@Url String url, @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST
    Call<ResponseBody> uploadAvatar(@Url String url, @Part("sid") String sid, @Part("deviceid") String deviceid, @Part("rid") String rid, @Part("pathKey") String pathKey, @Part("fileName") String description, @Part("file\"; filename=\"image.png\"") RequestBody imgs);
}
