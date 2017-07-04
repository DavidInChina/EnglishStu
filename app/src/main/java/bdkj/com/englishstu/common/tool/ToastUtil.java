package bdkj.com.englishstu.common.tool;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by younminx on 2017/2/13.
 * 提示框类
 */

public class ToastUtil {

    private static Toast toast;

    /**
     * 返回显示自定义提示框类
     *
     * @param content 提示内容
     * @return Toast 提示框
     */
    public static Toast show(Context mContext, String content) {
        if (null != toast) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView view = new TextView(mContext);
        view.setText(content);
        toast.setView(view);
        toast.show();
        return toast;
    }
}
