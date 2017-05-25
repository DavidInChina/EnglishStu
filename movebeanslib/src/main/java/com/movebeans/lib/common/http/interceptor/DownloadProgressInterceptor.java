package com.movebeans.lib.common.http.interceptor;

import com.movebeans.lib.common.http.listener.DownloadProgressListener;
import com.movebeans.lib.common.http.response.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by younminx on 2017/2/21.
 * 下载拦截器
 */

public class DownloadProgressInterceptor implements Interceptor
    {
        DownloadProgressListener progressListener;

        public DownloadProgressInterceptor(DownloadProgressListener listener)
            {
                this.progressListener = listener;
            }

        @Override
        public Response intercept(Chain chain) throws IOException
            {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody (originalResponse.body(), progressListener))
                        .build();
            }
    }
