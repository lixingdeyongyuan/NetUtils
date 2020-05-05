package com.aixue.netutilslib.http.api;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 请求通用的API
 * 添加其他请求类型
 */
public interface CommonApi {

    @GET
    Observable<ResponseBody> get(@Url String url);

    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> options);

    @POST
    Observable<ResponseBody> post(@Url String url, @Body RequestBody body);

    @POST
    Observable<ResponseBody> post(@Url String url, @Body RequestBody body, @HeaderMap Map<String, String> headers);

}
