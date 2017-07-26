package bdkj.com.englishstu.common.tool;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by younminx on 2017/2/14.
 * 网络工具类
 */

public class NetUtil {
    /**
     * 判断是有网络连接
     *
     * @param context 上下文
     * @return 是否有网络可用
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断是有wifi连接
     *
     * @param context 上下文
     * @return 是否有wifi可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity, int requestCode) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction(Intent.ACTION_VIEW);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context 上下文
     * @return MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型
     *
     * @param context 上下文
     * @return 网络类型
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                               /*
                              有这些类型
                               ConnectivityManager.TYPE_MOBILE,
                               ConnectivityManager.TYPE_WIFI,
                               ConnectivityManager.TYPE_WIMAX,
                               ConnectivityManager.TYPE_ETHERNET,
                               ConnectivityManager.TYPE_BLUETOOTH
                               */
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * Network type is unknown
     * (不知道网络类型)
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     * (2.5G）移动和联通
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     * (2.75G)2.5G到3G的过渡    移动和联通
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     * (3G)联通
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     * (2G 电信)
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     * ( 3G )电信
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     * (3.5G) 属于3G过渡
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     * ( 2G )
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     * (3.5G )
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     * ( 3.5G )
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     * ( 3G )联通
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     * (2G )
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     * 3G-3.5G
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     * (4G)
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     * 3G(3G到4G的升级产物)
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     * ( 3G )
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    /**
     * Current network is GSM
     * (2G)
     */
    public static final int NETWORK_TYPE_GSM = 16;

    /**
     * 获取当前网络类型名称
     *
     * @param context 上下文
     * @return 网络类型
     */
    public static String getConnectedTypeName(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable())
                if (mNetworkInfo.getType() == 0) {
                    switch (mNetworkInfo.getSubtype()) {
                        case NETWORK_TYPE_GPRS:
                        case NETWORK_TYPE_GSM:
                        case NETWORK_TYPE_EDGE:
                        case NETWORK_TYPE_CDMA:
                        case NETWORK_TYPE_1xRTT:
                        case NETWORK_TYPE_IDEN:
                            return "2g";
                        case NETWORK_TYPE_UMTS:
                        case NETWORK_TYPE_EVDO_0:
                        case NETWORK_TYPE_EVDO_A:
                        case NETWORK_TYPE_HSDPA:
                        case NETWORK_TYPE_HSUPA:
                        case NETWORK_TYPE_HSPA:
                        case NETWORK_TYPE_EVDO_B:
                        case NETWORK_TYPE_EHRPD:
                        case NETWORK_TYPE_HSPAP:
                            return "3g";
                        case NETWORK_TYPE_LTE:
                            return "4g";
                    }
                } else if (mNetworkInfo.getType() == 1) {
                    return "wifi";
                }
        }
        return "未知";
    }
}
