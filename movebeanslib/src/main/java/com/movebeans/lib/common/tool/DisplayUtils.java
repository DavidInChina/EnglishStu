/*
 * Copyright (C) 2016 venshine.cn@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.movebeans.lib.common.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

/**
 * 屏幕显示相关信息
 *
 * @author venshine
 */
public class DisplayUtils
    {


    /**
     * Get screen width, in pixels
     *
     * @param context the context
     * @return screen width
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * Get screen height, in pixels
     *
     * @param context the context
     * @return screen height
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static Bitmap zoom(Bitmap bitmap, int targetWidth, int targetHeight) {
        float scaleVal = getScaleVal(bitmap, targetWidth, targetHeight);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleVal, scaleVal);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    private static float getScaleVal(Bitmap bitmap, float targetWidth, float targetHeight) {
        if (bitmap == null) {
            return 0;
        }
        try {
            int srcWidth = bitmap.getWidth();
            int srcHeight = bitmap.getHeight();
            float scaleVal = Math.max(targetWidth / srcWidth, targetHeight / srcHeight);
            return scaleVal;
        } catch (Exception e) {
            return 0;
        }

    }

}
