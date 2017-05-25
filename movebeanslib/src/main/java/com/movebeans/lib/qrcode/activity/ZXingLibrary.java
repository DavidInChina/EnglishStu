package com.movebeans.lib.qrcode.activity;

import android.content.Context;
import android.util.DisplayMetrics;

import com.movebeans.lib.qrcode.DisplayUtil;


/**
 * Created by aaron on 16/9/7.
 */

public class ZXingLibrary {

    public static void initDisplayOpinion(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(context, dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(context, dm.heightPixels);
    }
}
