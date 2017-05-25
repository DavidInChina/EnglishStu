package com.movebeans.lib.common.http.listener;

/**
 * Created by younminx on 2017/2/21.
 * 下载监听
 */

public interface DownloadProgressListener
    {
        /**
         * 进度回调
         *
         * @param bytesRead     已经下载或上传字节数
         * @param contentLength 总字节数
         * @param done          是否完成
         */
        void onProgress (long bytesRead, long contentLength, boolean done);
    }
