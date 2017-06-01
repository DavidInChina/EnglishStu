/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bdkj.com.englishstu.common.tool;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import java.util.List;

/**
 * App 相关信息，包括版本名称、版本号、包名等等
 *
 * @author venshine
 */
public class AppUtils {
    /**
     * Get version name
     *
     * @param context the context
     * @return version name
     */
    public static String getVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * Get version code
     *
     * @param context the context
     * @return version code
     */
    public static int getVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * Get app version name
     *
     * @param context     the context
     * @param packageName the package name
     * @return app version name
     */
    public static String getAppVersionName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get app version code
     *
     * @param context     the context
     * @param packageName the package name
     * @return app version code
     */
    public static int getAppVersionCode(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get app name
     *
     * @param context     the context
     * @param packageName the package name
     * @return app name
     */
    public static String getAppName(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            return info.loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Judge whether an app is in background
     * 原理
     * 当一个 App 处于前台的时候，会处于 RunningTask 的这个栈的栈顶，所以我们可以取出 RunningTask 的栈顶的任务进程，看他与我们的想要判断的 App 的包名是否相同，来达到效果
     * 缺点
     * getRunningTask 方法在 Android5.0 以上已经被废弃，只会返回自己和系统的一些不敏感的 task，不再返回其他应用的 task，用此方法来判断自身 App 是否处于后台，仍然是有效的，但是无法判断其他应用是否位于前台，因为不再能获取信息
     *
     * @param context the context
     * @return boolean
     */
    @Deprecated
    public static boolean isAppInBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null
                    && !topActivity.getPackageName().equals(
                    context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断应用是否在前台
     * 原理
     * 通过 runningProcess 获取到一个当前正在运行的进程的 List，我们遍历这个 List 中的每一个进程，判断这个进程的一个 importance 属性是否是前台进程，并且包名是否与我们判断的 APP 的包名一样，如果这两个条件都符合，那么这个 App 就处于前台
     * 缺点：
     * 在聊天类型的 App 中，常常需要常驻后台来不间断的获取服务器的消息，这就需要我们把 Service 设置成 START_STICKY，kill 后会被重启（等待 5 秒左右）来保证 Service 常驻后台。如果 Service 设置了这个属性，这个 App 的进程就会被判断是前台，代码上的表现就是 appProcess.importance 的值永远是 ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND，这样就永远无法判断出到底哪个是前台了。
     *
     * @param context the context
     * @return boolean
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = manager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * Activity是否在栈顶
     *
     * @param context the context
     * @return boolean
     */
    public static boolean isTopActivityInApp(Context context) {
        boolean isTop = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(Integer.MAX_VALUE);
        if (list != null && list.size() > 0) {
            for (ActivityManager.RunningTaskInfo runningTaskInfo : list) {
                if (runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                    ComponentName cn = runningTaskInfo.topActivity;
                    Log.d("PackageUtils", "isTopActivityInApp::::" + cn.toString());
                    if (cn.getClassName().equals(context.getClass().getName())) {
                        isTop = true;
                        break;
                    }
                }
            }
        }
        return isTop;
    }
}
