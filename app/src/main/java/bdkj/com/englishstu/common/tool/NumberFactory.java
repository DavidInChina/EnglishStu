package bdkj.com.englishstu.common.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/20.
 */

public class NumberFactory {

    public static String getNumber(String maxOrderno) {
        String Orderno = null;
    // maxOrderno = "NO20160127001"; // 从数据库查询出的最大编号
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); // 时间字符串产生方式
        String uid_pfix = "NO" + format.format(new Date()); // 组合流水号前一部分，NO+时间字符串，如：NO20160126
        if (maxOrderno != null && maxOrderno.contains(uid_pfix)) {
            String uid_end = maxOrderno.substring(10, 13); // 截取字符串最后四位，结果:001
            int endNum = Integer.parseInt(uid_end); // 把String类型的001转化为int类型的1
            int tmpNum = 1000 + endNum + 1; // 结果1002
            Orderno = uid_pfix +subStr("" + tmpNum, 1);// 把1002首位的1去掉，再拼成NO20160126002字符串
        } else {
            Orderno = uid_pfix + "001";
        }
       return Orderno;
    }
    public static String getStuNumber(String maxOrderno) {
        String Orderno = null;
        // maxOrderno = "NO20160127001"; // 从数据库查询出的最大编号
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); // 时间字符串产生方式
        String uid_pfix = "ST" + format.format(new Date()); // 组合流水号前一部分，NO+时间字符串，如：NO20160126
        if (maxOrderno != null && maxOrderno.contains(uid_pfix)) {
            String uid_end = maxOrderno.substring(10, 13); // 截取字符串最后四位，结果:001
            int endNum = Integer.parseInt(uid_end); // 把String类型的001转化为int类型的1
            int tmpNum = 1000 + endNum + 1; // 结果1002
            Orderno = uid_pfix +subStr("" + tmpNum, 1);// 把1002首位的1去掉，再拼成NO20160126002字符串
        } else {
            Orderno = uid_pfix + "001";
        }
        return Orderno;
    }
    /**
     * 把10002首位的1去掉的实现
     * @param str
     * @param start
     * @return
     */
    public static String subStr(String str, int start) {
        if (str == null || str.equals("") || str.length() == 0)
            return "";
        if (start < str.length()) {
            return str.substring(start);
        } else {
            return "";
        }

    }
}
