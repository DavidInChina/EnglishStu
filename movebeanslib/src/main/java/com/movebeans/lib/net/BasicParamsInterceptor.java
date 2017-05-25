package com.movebeans.lib.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 用于添加公共参数的拦截器
 * <p>
 * Created by zhangfei on 2017/3/23.
 */

public class BasicParamsInterceptor implements Interceptor
    {
    Map<String, Object> queryParamsMap;
    Map<String, Object> paramsMap;
    Map<String, Object> headerParamsMap;
    List<Object> headerLinesList;

    private BasicParamsInterceptor() {
        this.queryParamsMap = new HashMap();
        this.paramsMap = new HashMap();
        this.headerParamsMap = new HashMap();
        this.headerLinesList = new ArrayList();
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Request.Builder requestBuilder = request.newBuilder();
        okhttp3.Headers.Builder headerBuilder = request.headers().newBuilder();
        Iterator formBodyBuilder;
        if (this.headerParamsMap.size() > 0) {
            formBodyBuilder = this.headerParamsMap.entrySet().iterator();

            while (formBodyBuilder.hasNext()) {
                Map.Entry formBody = (Map.Entry) formBodyBuilder.next();
                headerBuilder.add((String) formBody.getKey(), (String) formBody.getValue());
            }
        }

        if (this.headerLinesList.size() > 0) {
            formBodyBuilder = this.headerLinesList.iterator();

            while (formBodyBuilder.hasNext()) {
                String formBody1 = (String) formBodyBuilder.next();
                headerBuilder.add(formBody1);
            }
        }

        requestBuilder.headers(headerBuilder.build());
        if (this.queryParamsMap.size() > 0) {
            this.injectParamsIntoUrl(request, requestBuilder, this.queryParamsMap);
        }

        if (request.method().equals("POST") && request.body().contentType().subtype().equals("x-www-form-urlencoded")) {
            okhttp3.FormBody.Builder formBodyBuilder1 = new okhttp3.FormBody.Builder();
            if (this.paramsMap.size() > 0) {
                Iterator formBody2 = this.paramsMap.entrySet().iterator();

                while (formBody2.hasNext()) {
                    Map.Entry postBodyString = (Map.Entry) formBody2.next();
                    formBodyBuilder1.add((String) postBodyString.getKey(), String.valueOf(postBodyString.getValue()));
                }
            }

            FormBody formBody3 = formBodyBuilder1.build();
            String postBodyString1 = bodyToString(request.body());
            postBodyString1 = postBodyString1 + (postBodyString1.length() > 0 ? "&" : "") + bodyToString(formBody3);
            requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString1));
        } else {
            this.injectParamsIntoUrl(request, requestBuilder, this.paramsMap);
        }

        request = requestBuilder.build();
        return chain.proceed(request);
    }

    private void injectParamsIntoUrl(Request request, okhttp3.Request.Builder requestBuilder, Map<String, Object> paramsMap) {
        okhttp3.HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
        }

        requestBuilder.url(httpUrlBuilder.build());
    }

    private static String bodyToString(RequestBody request) {
        try {
            Buffer buffer = new Buffer ();
            if (request != null) {
                request.writeTo(buffer);
                return buffer.readUtf8();
            } else {
                return "";
            }
        } catch (IOException var3) {
            return "did not work";
        }
    }

    public static class Builder {
        BasicParamsInterceptor interceptor = new BasicParamsInterceptor();

        public Builder() {
        }

        public BasicParamsInterceptor.Builder addParam(String key, Object value) {
            this.interceptor.paramsMap.put(key, value);
            return this;
        }

        public BasicParamsInterceptor.Builder addParamsMap(Map<String, Object> paramsMap) {
            this.interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public BasicParamsInterceptor.Builder addHeaderParam(String key, Object value) {
            this.interceptor.headerParamsMap.put(key, value);
            return this;
        }

        public BasicParamsInterceptor.Builder addHeaderParamsMap(Map<String, Object> headerParamsMap) {
            this.interceptor.headerParamsMap.putAll(headerParamsMap);
            return this;
        }

        public BasicParamsInterceptor.Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            } else {
                this.interceptor.headerLinesList.add(headerLine);
                return this;
            }
        }

        public BasicParamsInterceptor.Builder addHeaderLinesList(List<Object> headerLinesList) {
            Iterator i$ = headerLinesList.iterator();

            while (i$.hasNext()) {
                String headerLine = (String) i$.next();
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }

                this.interceptor.headerLinesList.add(headerLine);
            }

            return this;
        }

        public BasicParamsInterceptor.Builder addQueryParam(String key, Object value) {
            this.interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public BasicParamsInterceptor.Builder addQueryParamsMap(Map<String, Object> queryParamsMap) {
            this.interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public BasicParamsInterceptor build() {
            return this.interceptor;
        }
    }
}
