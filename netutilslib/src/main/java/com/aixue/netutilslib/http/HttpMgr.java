package com.aixue.netutilslib.http;

import com.aixue.netutilslib.http.retry.RetryRxJava2CallAdapterFactory;
import com.aixue.netutilslib.log.NetLogProxy;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http请求管理类
 */
public class HttpMgr {

    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;

    private static String sBaseUrl = "";
    private static String sCacheDirPath;
    private static boolean sIsPrintHttpLog = false;
    private static long sDefaultTimeOut = 10;// 连接超时时间，默认10s
    private static long sHttpCacheSize = 20 * 1024 * 1024; // HTTP缓存大小，默认20 MB


    public static void init(String cacheDirPath, String baseUrl, boolean isPrintHttpLog) {
        sCacheDirPath = cacheDirPath;
        sBaseUrl = baseUrl;
        sIsPrintHttpLog = isPrintHttpLog;
    }

    private HttpMgr() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(sDefaultTimeOut, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        if (sIsPrintHttpLog) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    NetLogProxy.getInstance().debug("http_log", message);
                }
            });
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }
        okHttpClientBuilder.cache(new Cache(new File(sCacheDirPath, "cache"),
                sHttpCacheSize));
        mOkHttpClient = okHttpClientBuilder.build();
        mRetrofit = newRetrofit(sBaseUrl);
    }

    private static class SingletonHolder {
        private static final HttpMgr INSTANCE = new HttpMgr();
    }

    public static HttpMgr instance() {
        return SingletonHolder.INSTANCE;
    }

    public Retrofit defaultRetrofit() {
        return mRetrofit;
    }

    public Retrofit newRetrofit(String baseUrl) {
        return newRetrofit(baseUrl, new Gson());
    }

    public Retrofit newRetrofit(String baseUrl, Gson gson) {
        return newRetrofit(baseUrl, gson, mOkHttpClient);
    }

    public Retrofit newRetrofit(String baseUrl, Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RetryRxJava2CallAdapterFactory.create(RxJava2CallAdapterFactory.create()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public <T> T getApi(Class<T> apiClass) {
        return defaultRetrofit().create(apiClass);
    }

    public <T> T getApi(String baseUrl, Class<T> apiClass) {
        return newRetrofit(baseUrl).create(apiClass);
    }

    public <T> T getApi(String baseUrl, Class<T> apiClass, Gson gson) {
        return newRetrofit(baseUrl, gson).create(apiClass);
    }


}
