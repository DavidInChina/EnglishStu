package com.movebeans.lib.net;

import com.movebeans.lib.base.JsonEntity;
import com.movebeans.lib.common.tool.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observable.Transformer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hpw on 16/11/2.
 */

public class APiUtil
    {

        /**
         * 统一线程处理
         *
         * @param <T>
         * @return
         */
        public static <T> Transformer<T, T> rxSchedulerHelper ()
            {    //compose简化线程
                return new Transformer<T, T> ()
                    {
                        @Override
                        public Observable<T> call (Observable<T> tObservable)
                            {
                                return tObservable.observeOn (AndroidSchedulers.mainThread ()).subscribeOn (Schedulers.io ());
                            }
                    };
            }

        /**
         * 统一返回数据请求结果处理
         *
         * @param <T>
         * @return
         */
        public static <T> Transformer<JsonEntity<T>, T> handleResult ()
            {   //compose判断结果
                return new Transformer<JsonEntity<T>, T> ()
                    {
                        @Override
                        public Observable<T> call (Observable<JsonEntity<T>> jsonEntityObservable)
                            {
                                return jsonEntityObservable.map (new HttpResultFunc<T> ());
                            }
                    };
            }

        /**
         * 统一返回下载结果处理
         *
         * @return
         * @Param filePath 文件路径
         */
        public static Transformer<ResponseBody, String> handleDownLoadResult (final String filePath)
            {   //compose判断结果

                return new Transformer<ResponseBody, String> ()
                    {

                        @Override
                        public Observable<String> call (Observable<ResponseBody> responseBodyObservable)
                            {
                                Observable<String> observable = responseBodyObservable.subscribeOn (Schedulers.io ()).unsubscribeOn (Schedulers.io ()).map (new Func1<ResponseBody, InputStream> ()
                                    {
                                        @Override
                                        public InputStream call (ResponseBody responseBody)
                                            {
                                                return responseBody.byteStream ();
                                            }
                                    }).observeOn (Schedulers.computation ()).doOnNext (new Action1<InputStream> ()
                                    {
                                        @Override
                                        public void call (InputStream inputStream)
                                            {
                                                File file = new File (filePath);
                                                if (!file.exists ())
                                                    try
                                                        {
                                                            file.createNewFile ();
                                                        } catch (IOException e)
                                                        {
                                                            e.printStackTrace ();
                                                        }
                                                FileUtil.write2Disk (inputStream, file);
                                            }
                                    }).map (new Func1<InputStream, String> ()
                                    {
                                        @Override
                                        public String call (InputStream inputStream)
                                            {
                                                return filePath;
                                            }
                                    }).observeOn (AndroidSchedulers.mainThread ());
                                return observable;
                            }
                    };
            }
    }
