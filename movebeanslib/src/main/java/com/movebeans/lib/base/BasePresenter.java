package com.movebeans.lib.base;

import android.content.Context;

import com.movebeans.lib.common.tool.RxManager;


/**
 * 基础中转模型
 * <p>
 * Created by zhangfei on 2017/3/22.
 */

public abstract class BasePresenter<M, T>
    {

        public BasePresenter ()
            {
                attachM ();
            }

        public Context mContext;
        public M mModel;
        public T mView;
        public RxManager mRxManager = new RxManager ();

        /**
         * 绑定视图
         *
         * @param v
         */
        public void attachV (Context mContext, T v)
            {
                this.mView = v;
                this.mContext = mContext;
            }

        /**
         * 绑定数据请求
         *
         * @param
         */
        public void attachM ()
            {
                this.mModel = getmModel ();
            }


        /**
         * 解除绑定关系
         */
        public void detachVM ()
            {
                unsubscribe ();
                mView = null;
                mModel = null;
            }

        /**
         * 取消网络请求
         */
        public void unsubscribe ()
            {
                mRxManager.clear ();
            }


        /**
         * 创建请求关系
         *
         * @param <M>
         * @return
         */
        public abstract <M> M getmModel ();

    }
