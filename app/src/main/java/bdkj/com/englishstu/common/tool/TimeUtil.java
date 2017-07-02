package bdkj.com.englishstu.common.tool;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by younminx on 2017/2/14.
 * 毫秒工具类
 */

public class TimeUtil {
    /**
     * 默认的时间格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 毫秒转日期，默认格式yyyy-MM-dd HH:mm:ss
     *
     * @param time 毫秒数
     * @return 日期字符串
     */
    public static String time2Date(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * Date 转日期
     */
    public static String date2String(Date date) {
        String result = "";
        result = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
        return result;
    }

    /**
     * @return 返回当前毫秒数转换后的字符串
     */
    public static String getCurrentTimeInString() {
        return getCurrentTimeInString(DEFAULT_DATE_FORMAT);
    }

    /**
     * 返回当前毫秒数转换后的字符串
     *
     * @param format 转换格式
     * @return
     */
    public static String getCurrentTimeInString(String format) {
        return formatMillisTo(getCurrentMillis(), format);
    }

    public static String getBeforeDayTimeInString(String format) {
        return formatMillisTo(getCurrentMillis() - 24 * 60 * 60 * 1000, format);//获取前一天
    }

    public static String getNextDayTimeInString(String format) {
        return formatMillisTo(getCurrentMillis() + 24 * 60 * 60 * 1000, format);//获取后一天
    }

    public static String getBeforeMonthTimeInString(String format) {
        long month = 30L * 24 * 60 * 60 * 1000;//定义长整形变量
        return formatMillisTo(getCurrentMillis() - month, format);//获取前一个月，30天前
    }

    /**
     * @return 返回当前毫秒数
     */
    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 按指定格式去格式化毫秒数
     *
     * @param timeInMillis 毫秒数
     * @param format       转换格式
     * @return 返回转换后的字符串
     */
    public static String formatMillisTo(long timeInMillis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 毫秒转日期，指定格式
     *
     * @param time    毫秒数
     * @param formate 日期格式 如：yyyy-MM-dd HH:mm:ss
     * @return 日期字符串
     */
    public static String time2Date(long time, String formate) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(formate);
        return format.format(date);
    }

    /**
     * 毫秒转多久之前
     *
     * @param time 毫秒数
     * @return 多久之前 如:1分钟之前
     */
    public static String time2Ago(long time) {
        Date date = new Date(time);
        long current = System.currentTimeMillis();
        if (current - time < 1000) {
            return "1秒前";
        } else if (current - time < 60000) {
            return (current - time) / 1000 + "秒前";
        } else if (current - time < 3600000) {
            return (current - time) / 1000 / 60 + "分钟前";
        } else if (current - time < 86400000) {
            return (current - time) / 1000 / 60 / 60 + "小时前";
        } else if (current - time < 2592000000l) {
            return (current - time) / 1000 / 60 / 60 / 24 + "天前";
        } else if (current - time < 31104000000l) {
            return (current - time) / 1000 / 60 / 60 / 24 / 30 + "个月前";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }
    }

    /**
     * 秒数转成00：00格式分钟
     *
     * @param time
     * @return
     */
    public static String time2Minutes(long time) {
        String result = "";
        int second = (int) (time % 60);
        int min = (int) (time / 60);
        if (min < 10) {
            result += "0" + min;
        } else {
            result += min;
        }
        if (second < 10) {
            result += ":0" + second;
        } else {
            result += ":" + second;
        }
        return result;
    }

    /**
     * 00:00格式分钟转换为秒
     *
     * @param minutes
     * @return
     */
    public static long minutes2Time(String minutes) {
        long result = 0;
        String[] times = minutes.split(":");
        int second = Integer.parseInt(times[1]);
        int min = Integer.parseInt(times[0]);
        result = min * 60 + second;
        return result;
    }

    /**
     * 获取当前时间是否大于12：30
     */
    public static boolean isRightTime() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int hour = t.hour; // 0-23
        int minute = t.minute;
        return hour > 12 || (hour == 12 && minute >= 30);
    }

}
