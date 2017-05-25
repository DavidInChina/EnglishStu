package com.movebeans.lib.common.tool;

import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by younminx on 2017/2/14.
 * 毫秒工具类
 */

public class TimeUtil
    {
        /**
         * 毫秒转日期，默认格式yyyy-MM-dd HH:mm:ss
         *
         * @param time 毫秒数
         * @return 日期字符串
         */
        public static String time2Date (long time)
            {
                Date date = new Date (time);
                SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                return format.format (date);
            }

        /**
         * 毫秒转日期，指定格式
         *
         * @param time    毫秒数
         * @param formate 日期格式 如：yyyy-MM-dd HH:mm:ss
         * @return 日期字符串
         */
        public static String time2Date (long time, String formate)
            {
                Date date = new Date (time);
                SimpleDateFormat format = new SimpleDateFormat (formate);
                return format.format (date);
            }

        /**
         * 毫秒转多久之前
         *
         * @param time 毫秒数
         * @return 多久之前 如:1分钟之前
         */
        public static String time2Ago (long time)
            {
                Date date = new Date (time);
                long current = System.currentTimeMillis ();
                if (current - time < 1000)
                    {
                        return "1秒前";
                    } else if (current - time < 60000)
                    {
                        return (current - time) / 1000 + "秒前";
                    } else if (current - time < 3600000)
                    {
                        return (current - time) / 1000 / 60 + "分钟前";
                    } else if (current - time < 86400000)
                    {
                        return (current - time) / 1000 / 60 / 60 + "小时前";
                    } else if (current - time < 2592000000l)
                    {
                        return (current - time) / 1000 / 60 / 60 / 24 + "天前";
                    } else if (current - time < 31104000000l)
                    {
                        return (current - time) / 1000 / 60 / 60 / 24 / 30 + "个月前";
                    } else
                    {
                        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                        return format.format (date);
                    }
            }

        /**
         * 获取当前时间是否大于12：30
         */
        public static boolean isRightTime ()
            {
                Time t = new Time (); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
                t.setToNow (); // 取得系统时间。
                int hour = t.hour; // 0-23
                int minute = t.minute;
                return hour > 12 || (hour == 12 && minute >= 30);
            }

    }
