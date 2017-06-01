package bdkj.com.englishstu.common.tool;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by younminx on 2017/2/14.
 * 手机像素密度工具类
 */

public class DensityUtil
    {
        /**
         * 手机像素密度信息
         *
         * @param context 运行上下文
         * @return 设备屏幕像素信息
         */
        public static String getDisplayMetrics (Context context)
            {
                String str = "";
                DisplayMetrics dm = new DisplayMetrics ();
                dm = context.getApplicationContext ().getResources ().getDisplayMetrics ();
                int screenWidth = dm.widthPixels;
                int screenHeight = dm.heightPixels;
                float density = dm.density;
                float xdpi = dm.xdpi;
                float ydpi = dm.ydpi;
                str += "屏幕宽度的像素数量:" + String.valueOf (screenWidth) + "pixels\n";
                str += "屏幕高度度的像素数量:" + String.valueOf (screenHeight) + "pixels\n";
                str += "获取当前设备的基准比例:" + String.valueOf (density) + "\n";
                str += "得到物理屏幕上 X 轴方向每英寸的像素:" + String.valueOf (xdpi) + "pixels per inch\n";
                str += "得到物理屏幕上 Y 轴方向每英寸的像素:" + String.valueOf (ydpi) + "pixels per inch\n";
                return str;
            }

        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         *
         * @param context 运行上下文
         * @param dpValue dp
         * @return 像素数
         */
        public static int dip2px (Context context, float dpValue)
            {
                final float scale = context.getResources ().getDisplayMetrics ().density;
                return (int) (dpValue * scale + 0.5f);
            }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         *
         * @param context 运行上下文
         * @param pxValue 像素数
         * @return dp
         */
        public static int px2dip (Context context, float pxValue)
            {
                final float scale = context.getResources ().getDisplayMetrics ().density;
                return (int) (pxValue / scale + 0.5f);
            }

        /**
         * 设置某个View的margin
         *
         * @param view   需要设置的view
         * @param isDp   需要设置的数值是否为DP
         * @param left   左边距
         * @param right  右边距
         * @param top    上边距
         * @param bottom 下边距
         * @return
         */
        public static ViewGroup.LayoutParams setViewMargin (View view, boolean isDp, int left, int right, int top, int bottom)
            {
                if (view == null)
                    {
                        return null;
                    }

                int leftPx = left;
                int rightPx = right;
                int topPx = top;
                int bottomPx = bottom;
                ViewGroup.LayoutParams params = view.getLayoutParams ();
                ViewGroup.MarginLayoutParams marginParams = null;
                //获取view的margin设置参数
                if (params instanceof ViewGroup.MarginLayoutParams)
                    {
                        marginParams = (ViewGroup.MarginLayoutParams) params;
                    } else
                    {
                        //不存在时创建一个新的参数
                        marginParams = new ViewGroup.MarginLayoutParams (params);
                    }

                //根据DP与PX转换计算值
                if (isDp)
                    {
                        leftPx = dip2px (view.getContext (), left);
                        rightPx = dip2px (view.getContext (), right);
                        topPx = dip2px (view.getContext (), top);
                        bottomPx = dip2px (view.getContext (), bottom);
                    }
                //设置margin
                marginParams.setMargins (leftPx, topPx, rightPx, bottomPx);
                view.setLayoutParams (marginParams);
                view.requestLayout ();
                return marginParams;
            }
    }
