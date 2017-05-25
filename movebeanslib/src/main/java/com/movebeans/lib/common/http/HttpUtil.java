package com.movebeans.lib.common.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by younminx on 2017/2/15.
 * 网络通信单例
 */

public class HttpUtil {
    private volatile static OkHttpClient okHttpClient = null;
    private volatile static Retrofit retrofit = null;
    private final static String BASEURL = "https://api.douban.com/";

    /**
     * 获取网络请求单例
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (Retrofit.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASEURL)
                            //添加string返回
                            .addConverterFactory(ScalarsConverterFactory
                                    .create())
                            .addConverterFactory(GsonConverterFactory.create())
                            //这里要注意，retrofit对于解析器是由添加的顺序分别试用的，解析成功就直接返回，失败则调用下一个解析器
                            .addCallAdapterFactory(RxJavaCallAdapterFactory
                                    .create())
                            .client(getOkHttpClient())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * 获取okhttpclient单例
     */
    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .addInterceptor
                                    (new HttpLoggingInterceptor().setLevel
                                            (HttpLoggingInterceptor
                                                    .Level.BODY)).build();
                }
            }
        }
        return okHttpClient;
    }

    /**
     * 指定地址进行网络请求
     */
    public static Retrofit getClient(String baseurl) {
        return new Retrofit.Builder()
                .baseUrl(baseurl)
                //这里要注意，retrofit对于解析器是由添加的顺序分别试用的，解析成功就直接返回，失败则调用下一个解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory
                        .create())
                .client(getOkHttpClient())
                .build();
    }
}
