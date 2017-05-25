package com.movebeans.lib.common.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by younminx on 2017/2/14.
 * 日期工具类
 */

public class DateUtil
    {
        /**
         * 日期转毫秒，默认格式yyyy-MM-dd HH:mm:ss
         *
         * @param dateStr 日期字符串
         * @return 毫秒数
         * @throws ParseException 格式转换异常
         */
        public static long date2Time (String dateStr)
            {
                SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                try
                    {
                        Date date = format.parse (dateStr);
                        return date.getTime ();
                    } catch (ParseException e)
                    {
                        e.printStackTrace ();
                        return 0;
                    }
            }

        /**
         * 日期转毫秒，指定日期格式
         *
         * @param dateStr 日期字符串
         * @param formate 日期格式 如：yyyy-MM-dd HH:mm:ss
         * @return 毫秒数
         */
        public static long date2TimeWithFormat (String dateStr, String formate)
            {
                SimpleDateFormat format = new SimpleDateFormat (formate);
                try
                    {
                        Date date = format.parse (dateStr);
                        return date.getTime ();
                    } catch (ParseException e)
                    {
                        e.printStackTrace ();
                        return 0;
                    }
            }
    }
