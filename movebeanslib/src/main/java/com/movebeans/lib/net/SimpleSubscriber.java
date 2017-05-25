package com.movebeans.lib.net;

import com.movebeans.lib.common.tool.LogUtil;

import rx.Subscriber;

/**
 * ClassName: SimpleSubscriber
 * Description: say something
 * Creator: chenwei
 * Date: 16/9/18 11:32
 * Version: 1.0
 */
public class SimpleSubscriber<T>
        extends Subscriber<T>
    {
        @Override
        public void onCompleted ()
            {
                LogUtil.d ("SuNongTong----->>", "完成请求");
            }

        @Override
        public void onError (Throwable e)
            {
                LogUtil.d ("SuNongTong----->>", "完成错误");
            }

        @Override
        public void onNext (T t)
            {
                LogUtil.d ("SuNongTong----->>", "请求完成");
            }
    }
