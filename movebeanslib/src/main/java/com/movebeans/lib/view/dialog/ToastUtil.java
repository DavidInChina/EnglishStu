package com.movebeans.lib.view.dialog;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by younminx on 2017/2/13.
 * 提示框类
 */

public class ToastUtil
    {
        /**
         * 返回显示自定义提示框类
         *
         * @param context 上下文
         * @param content 提示内容
         * @return Toast 提示框
         */
        public static Toast show(Context context, String content)
            {
                Toast toast = new Toast(context);
                TextView view = new TextView(context);
                view.setText(content);
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                return toast;
            }
    }
