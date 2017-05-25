package com.movebeans.lib.common.tool;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by zhangfei on 2017/1/12.
 */

public class InputMethodUtils
    {

    /**
     * Toggle Soft Input
     *
     * @param context the context
     */
    public static void toggle(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Show Soft Input
     *
     * @param view the view
     * @return boolean
     */
    public static boolean show(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Show Soft Input
     *
     * @param activity the activity
     * @return boolean
     */
    public static boolean show(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            return imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        }
        return false;
    }

    /**
     * Hide Soft Input
     *
     * @param view the view
     * @return boolean
     */
    public static boolean hide(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hide Soft Input
     *
     * @param activity the activity
     * @return boolean
     */
    public static boolean hide(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
        return false;
    }

    /**
     * Judge whether input method is active
     *
     * @param context the context
     * @return boolean
     */
    public static boolean isActive(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }
}
