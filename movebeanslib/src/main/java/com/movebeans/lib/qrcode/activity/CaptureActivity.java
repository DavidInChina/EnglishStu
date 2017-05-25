package com.movebeans.lib.qrcode.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.movebeans.lib.R;
import com.movebeans.lib.view.dialog.ToastUtil;


/**
 * Initial the camera
 * 默认的二维码扫描Activity
 */
public class CaptureActivity
        extends AppCompatActivity
    {


        @Override
        protected void onCreate (Bundle savedInstanceState)
            {

                super.onCreate (savedInstanceState);
                setContentView (R.layout.camera);
                if (ContextCompat.checkSelfPermission (this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.CAMERA}, 0);
                        CaptureFragment captureFragment = new CaptureFragment ();
                        captureFragment.setAnalyzeCallback (analyzeCallback);
                        getSupportFragmentManager ().beginTransaction ().replace (R.id.fl_zxing_container, captureFragment).commit ();
                    } else
                    {
                        CaptureFragment captureFragment = new CaptureFragment ();
                        captureFragment.setAnalyzeCallback (analyzeCallback);
                        getSupportFragmentManager ().beginTransaction ().replace (R.id.fl_zxing_container, captureFragment).commit ();
                    }
            }

        @Override
        public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
            {
                super.onRequestPermissionsResult (requestCode, permissions, grantResults);
                if (requestCode == 0)
                    {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                            {
                                ToastUtil.show (this, "没有相机权限");
                                finish ();
                            } else
                            {
                                CaptureFragment captureFragment = new CaptureFragment ();
                                captureFragment.setAnalyzeCallback (analyzeCallback);
                                getSupportFragmentManager ().beginTransaction ().replace (R.id.fl_zxing_container, captureFragment).commit ();
                            }
                    }
            }

        /**
         * 二维码解析回调函数
         */
        CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback ()
            {
                @Override
                public void onAnalyzeSuccess (Bitmap mBitmap, String result)
                    {
                        Intent resultIntent = new Intent ();
                        Bundle bundle = new Bundle ();
                        bundle.putInt (CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                        bundle.putString (CodeUtils.RESULT_STRING, result);
                        resultIntent.putExtras (bundle);
                        CaptureActivity.this.setResult (RESULT_OK, resultIntent);
                        CaptureActivity.this.finish ();
                    }

                @Override
                public void onAnalyzeFailed ()
                    {
                        Intent resultIntent = new Intent ();
                        Bundle bundle = new Bundle ();
                        bundle.putInt (CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                        bundle.putString (CodeUtils.RESULT_STRING, "");
                        resultIntent.putExtras (bundle);
                        CaptureActivity.this.setResult (RESULT_OK, resultIntent);
                        CaptureActivity.this.finish ();
                    }
            };
    }