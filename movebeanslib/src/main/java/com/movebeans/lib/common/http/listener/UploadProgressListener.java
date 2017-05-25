package com.movebeans.lib.common.http.listener;

/**
 * Created by younminx on 2017/2/22.
 * 上传监听
 */

public interface UploadProgressListener
    {
        /**
         * 上传进度
         *
         * @param currentBytesCount
         * @param totalBytesCount
         */
        void onProgress (long currentBytesCount, long totalBytesCount);
    }
