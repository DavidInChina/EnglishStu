package com.movebeans.lib.common.http.response;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.movebeans.lib.R;

import rx.Subscriber;

/**
 * Created by younminx on 2017/2/20.
 * 请求响应处理Subscriber
 */

public class BaseSubscriber<T>
        extends Subscriber<T>
    {
        ProgressDialog progressDialog;

        public BaseSubscriber (Context context)
            {
                progressDialog = new ProgressDialog (context);
                progressDialog.setTitle (context.getString (R.string.loading));
                progressDialog.setOnCancelListener (new DialogInterface.OnCancelListener ()
                    {
                        @Override
                        public void onCancel (DialogInterface dialogInterface)
                            {
                                BaseSubscriber.this.unsubscribe ();
                            }
                    });
            }

        @Override
        public void onCompleted ()
            {
                if (null != progressDialog && progressDialog.isShowing ())
                    {
                        progressDialog.dismiss ();
                    }
            }

        @Override
        public void onError (Throwable e)
            {
                if (null != progressDialog && progressDialog.isShowing ())
                    {
                        progressDialog.dismiss ();
                    }
            }

        @Override
        public void onNext (T t)
            {

            }

        @Override
        public void onStart ()
            {
                super.onStart ();
                progressDialog.show ();
            }
    }
