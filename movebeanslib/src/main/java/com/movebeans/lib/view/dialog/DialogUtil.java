package com.movebeans.lib.view.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by younminx on 2017/3/15.
 * 弹框工具类
 */

public class DialogUtil
    {
        public final static int ENSURE = 0;//确定
        public final static int CANCEL = 1;//确定和取消
        public final static int NEUTRAL = 3;//确定、取消、中立

        /**
         * 文字弹框构造器
         *
         * @param context  上下文
         * @param text     描述文字，长度必须为5,别为标题、内容、确定、取消、中立的文字
         * @param listener 点击事件
         * @return Dialog 弹框
         */
        public static Builder getTextBuilder (Context context, String[] text, DialogInterface.OnClickListener listener)
            {
                Builder builder = new Builder (context);
                if (text.length == 5)
                    {
                        if (!TextUtils.isEmpty (text[0]))
                            builder.setTitle (text[0]);
                        if (!TextUtils.isEmpty (text[1]))
                            builder.setMessage (text[1]);
                        if (!TextUtils.isEmpty (text[2]))
                            builder.setPositiveButton (text[2], listener);
                        if (!TextUtils.isEmpty (text[3]))
                            builder.setNegativeButton (text[3], listener);
                        if (!TextUtils.isEmpty (text[4]))
                            builder.setNeutralButton (text[4], listener);
                    } else
                    {
                        builder.setMessage ("初始化异常");
                    }
                return builder;
            }

        /**
         * 文字弹框
         *
         * @param context  上下文
         * @param text     描述文字，长度必须为5,别为标题、内容、确定、取消、中立的文字
         * @param listener 点击事件
         * @return Dialog 弹框
         */
        public static Dialog getTextDialog (Context context, String[] text, DialogInterface.OnClickListener listener)
            {
                return getTextBuilder (context, text, listener).create ();
            }

        /**
         * 自定义view弹框
         *
         * @param context  上下文
         * @param text     描述文字，长度必须为5,别为标题、内容、确定、取消、中立的文字
         * @param view     自定义view
         * @param listener 点击事件
         * @return Dialog 弹框
         */
        public static Dialog getViewDialog (Context context, String[] text, View view, DialogInterface.OnClickListener listener)
            {
                Builder builder = getTextBuilder (context, text, listener);
                if (view != null)
                    {
                        builder.setView (view);
                    }
                return builder.create ();
            }

        /***
         * 获取一个耗时等待对话框
         *
         * @param context 上下文
         * @param message 描述文字
         * @return
         */
        public static ProgressDialog getWaitDialog (Context context, String message)
            {
                ProgressDialog waitDialog = new ProgressDialog (context);
                if (!TextUtils.isEmpty (message))
                    {
                        waitDialog.setMessage (message);
                    }
                return waitDialog;
            }

        /***
         * 获取一个dialog
         *
         * @param context 上下文
         * @return
         */
        public static Builder getDialog (Context context)
            {
                Builder builder = new Builder (context);
                return builder;
            }

        /***
         * 获取一个选择dialog
         *
         * @param context 上下文
         * @param title 标题
         * @param arrays 选择项数组
         * @param onClickListener 点击事件
         * @return
         */
        public static Builder getSelectDialog (Context context, String title, String[] arrays, DialogInterface.OnClickListener onClickListener)
            {
                Builder builder = getDialog (context);
                builder.setItems (arrays, onClickListener);
                if (!TextUtils.isEmpty (title))
                    {
                        builder.setTitle (title);
                    }
                builder.setPositiveButton ("取消", null);
                return builder;
            }

        /***
         * 获取一个无标题选择dialog
         *
         * @param context 上下文
         * @param arrays 选择项数组
         * @param onClickListener 点击事件
         * @return
         */
        public static Builder getSelectDialog (Context context, String[] arrays, DialogInterface.OnClickListener onClickListener)
            {
                return getSelectDialog (context, "", arrays, onClickListener);
            }

        /***
         * 获取一个初始化位置的选择dialog
         *
         * @param context 上下文
         * @param title 标题
         * @param arrays 选择项数组
         * @param selectIndex 开始位置
         * @param onClickListener 点击事件
         * @return
         */
        public static Builder getSingleChoiceDialog (Context context, String title, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener)
            {
                Builder builder = getDialog (context);
                builder.setSingleChoiceItems (arrays, selectIndex, onClickListener);
                if (!TextUtils.isEmpty (title))
                    {
                        builder.setTitle (title);
                    }
                builder.setNegativeButton ("取消", null);
                return builder;
            }

        /***
         * 获取一个无标题初始化位置的选择dialog
         *
         * @param context 上下文
         * @param arrays 选择项数组
         * @param selectIndex 开始位置
         * @param onClickListener 点击事件
         * @return
         */
        public static Builder getSingleChoiceDialog (Context context, String[] arrays, int selectIndex, DialogInterface.OnClickListener onClickListener)
            {
                return getSingleChoiceDialog (context, "", arrays, selectIndex, onClickListener);
            }
    }
