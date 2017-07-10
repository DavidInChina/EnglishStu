package bdkj.com.englishstu.common.tool;

import com.youdao.sdk.ydtranslate.WebExplain;

import java.util.List;

/**
 * Created by younminx on 2017/2/13.
 * 字符串工具类
 */

public class StringUtil {
    public static String listStr(List<String> list) {
        StringBuilder sb = new StringBuilder();

        if (list != null) {
            for (String s : list) {
                sb.append(s).append("\n");
            }
        }

        return sb.toString();
    }
    public static String webMeans(List<WebExplain> explains) {
        StringBuilder sb = new StringBuilder();
        if (explains != null) {
            for (WebExplain s : explains) {
                sb.append(s.getKey()).append(":").append(listStr(s.getMeans())).append("\n");
            }
        }

        return sb.toString();
    }

}
