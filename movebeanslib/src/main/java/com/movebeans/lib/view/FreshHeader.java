package com.movebeans.lib.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by younminx on 2017/3/3.
 * 自定义下拉刷新头部
 */

public class FreshHeader extends FrameLayout implements PtrUIHandler
    {
        TextView processView;
        TextView statusView;

        public FreshHeader(Context context)
            {
                super(context, null);
                init();
            }

        public FreshHeader(Context context, AttributeSet attr)
            {
                super(context, attr);
                init();
            }

        /**
         * 头部布局，可以根据需求自定义
         */
        private void init()
            {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                processView = new TextView(getContext());
                processView.setPadding(0, 25, 0, 25);
                processView.setGravity(Gravity.CENTER);
                processView.setTextColor(Color.BLACK);
                processView.setBackgroundColor(Color.WHITE);
                processView.setText("下拉进度");
                statusView = new TextView(getContext());
                statusView.setPadding(0, 25, 0, 25);
                statusView.setGravity(Gravity.CENTER);
                statusView.setTextColor(Color.BLACK);
                statusView.setBackgroundColor(Color.WHITE);
                statusView.setText("下拉状态");
                linearLayout.addView(processView);
                linearLayout.addView(statusView);
                addView(linearLayout);
            }

        @Override
        public void onUIReset(PtrFrameLayout frame)
            {
            }

        @Override
        public void onUIRefreshPrepare(PtrFrameLayout frame)
            {
                statusView.setText("准备刷新");
            }

        @Override
        public void onUIRefreshBegin(PtrFrameLayout frame)
            {
                statusView.setText("正在刷新...");
            }

        @Override
        public void onUIRefreshComplete(PtrFrameLayout frame)
            {
                statusView.setText("刷新结束");
            }

        @Override
        public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
                                       PtrIndicator ptrIndicator)
            {
                processView.setText("下拉进度" + ptrIndicator.getCurrentPercent() * 100 + "%");
            }
    }
