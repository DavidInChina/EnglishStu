package com.movebeans.lib.base;

import android.support.multidex.MultiDexApplication;

/**
 * Created by zhangfei on 2017/3/22.
 * <p>
 * 可在build.gradle里进行配置，可分包处理
 */

public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
