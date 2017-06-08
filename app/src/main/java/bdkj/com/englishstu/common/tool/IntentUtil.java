package bdkj.com.englishstu.common.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by davidinchina on 2017/6/8.
 */

public class IntentUtil {
    /**
     * 禁止构造
     */
    public IntentUtil() {
        throw new AssertionError();
    }

    /**
     * 最底层跳转方法
     *
     * @param mContext    原页面上下文，可以使Activity或者Fragment
     * @param toClass     目标页面
     * @param extras      附加数据
     * @param requestCode 请求code
     * @param flag        启动flag，是否清除所有顶部页面
     */
    public static void launcher(Context mContext, Class<?> toClass, Bundle extras, int requestCode, int flag) {
        Intent intent = new Intent(mContext, toClass);
        intent.setFlags(flag);
        if (null != extras) {
            intent.putExtras(extras);
        }
        if (requestCode != -1) {
            ((Activity) mContext).startActivityForResult(intent, requestCode);
        } else {
            mContext.startActivity(intent);
        }
    }

    public static void launcher(Fragment mContext, Class<?> toClass, Bundle extras, int requestCode, int flag) {
        Intent intent = new Intent(mContext.getActivity(), toClass);
        intent.setFlags(flag);
        if (null != extras) {
            intent.putExtras(extras);
        }
        if (requestCode != -1) {
            mContext.startActivityForResult(intent, requestCode);
        } else {
            mContext.getActivity().startActivity(intent);
        }
    }

    public static void launcher(Context mContext, Class<?> toClass, Bundle extrasx) {
        launcher(mContext, toClass, extrasx, -1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static void launcher(Context mContext, Class<?> toClass) {
        launcher(mContext, toClass, null, -1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static void launcher(Fragment mContext, Class<?> toClass, Bundle extrasx) {
        launcher(mContext, toClass, extrasx, -1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public static void launcher(Fragment mContext, Class<?> toClass) {
        launcher(mContext, toClass, null, -1, Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

}
