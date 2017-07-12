package util.Http.okhttp;


import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import util.Http.IHttpCallBack;
import util.Http.IRxHttpUtils;
import util.Http.Pair;
import util.Http.RequestMap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Desc: 使用了rx
 * User: DLJ
 * Date: 2016-05-11
 * Time: 14:07
 */
public class RxOkhttpUtils3_2_0 extends OKhttpHttpUtils3_2_0 implements IRxHttpUtils<Response> {
    private static RxOkhttpUtils3_2_0 instance;

    protected RxOkhttpUtils3_2_0(boolean isDebug) {
        super(isDebug);
    }

    public static RxOkhttpUtils3_2_0 getInstance(boolean isDebug) {
        if (instance == null) {
            instance = new RxOkhttpUtils3_2_0(isDebug);
        }
        return instance;
    }


    @Override
    public Observable<IHttpCallBack.ResponseBean> post(final RequestMap params) {
        Observable<Response> responseObservable = Observable.create(new Observable
                .OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                OkhttpParamBuilder okhttpParamBuilder = new OkhttpParamBuilder(params);
                try {
                    Response response = client.newCall(new Request.Builder().headers(Headers.of
                            (params.getHeaders()))
                            .url(params.getUrl() + getQueryStr(params.getQueryStrs()))
                            .post(okhttpParamBuilder.build())
                            .build())
                            .execute();
                    subscriber.onNext(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }

            }
        });
        return getResponseBeanObserverable(responseObservable, params);
    }

    @Override
    public Observable<IHttpCallBack.ResponseBean> getResponseBeanObserverable
            (Observable<Response> observable, final RequestMap params) {
        return observable.flatMap(new Func1<Response, Observable<IHttpCallBack.ResponseBean>>() {
            @Override
            public Observable<IHttpCallBack.ResponseBean> call(final Response response) {
                return Observable.create(new Observable.OnSubscribe<IHttpCallBack.ResponseBean>() {
                    @Override
                    public void call(Subscriber<? super IHttpCallBack.ResponseBean> subscriber) {
                        String result = null;
                        final IHttpCallBack.ResponseBean responceBean = new IHttpCallBack
                                .ResponseBean();
                        try {
                            result = response.body()
                                    .string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        responceBean.pair = Pair.of("0", result);
                        responceBean.errorCode = response.code();

                        subscriber.onNext(responceBean);
                        subscriber.onCompleted();
                    }
                });
            }
        })
                .doOnNext(new Action1<IHttpCallBack.ResponseBean>() {//打印日志
                    @Override
                    public void call(final IHttpCallBack.ResponseBean responseBean) {//响应码不是200
                        // 则显示toast
                        printLog(params, responseBean.pair.key);
                    }
                });
    }

    @Override
    public Observable<IHttpCallBack.ResponseBean> get(final RequestMap params) {
        Observable<Response> responseObservable = Observable.create(new Observable
                .OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                Request request = new Request.Builder().headers(Headers.of(params.getHeaders()))
                        .url(params.getUrl() + getQueryStr(params.getQueryStrs()))
                        .build();
                try {
                    Response response = client.newCall(request)
                            .execute();
                    subscriber.onNext(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                }
            }
        });

        return getResponseBeanObserverable(responseObservable, params);
    }

    @Override
    public Observable<DownloadProgress> download(final String targetPath, final RequestMap params) {
        return Observable.create(new Observable.OnSubscribe<DownloadProgress>() {
            @Override
            public void call(final Subscriber<? super DownloadProgress> sub) {
                InputStream  input  = null;
                OutputStream output = null;
                Request request = new Request.Builder().headers(Headers.of(params.getHeaders()))
                        .url(params.getUrl() + getQueryStr(params.getQueryStrs()))
                        .build();
                try {

                    Response response = client.newCall(request)
                            .execute();
                    //如果访问成功
                    if (response.isSuccessful()) {
                        input = response.body()
                                .byteStream();
                        final long tlength = response.body()
                                .contentLength();

                        output = new FileOutputStream(targetPath);

                        byte data[] = new byte[1024];
                        final DownloadProgress downloadProgress = new DownloadProgress(tlength,
                                params.getUrl());
                        sub.onNext(downloadProgress);
                        int count;
                        //读取下载进度
                        while ((count = input.read(data)) != -1) {
                            downloadProgress.currentLength += count;
                            output.write(data, 0, count);
                            downloadProgress.currentPrecent = (int) (downloadProgress
                                    .currentLength * 100 / downloadProgress.totalLength);
                            sub.onNext(downloadProgress);
                        }
                        downloadProgress.currentLength = downloadProgress.totalLength;
                        downloadProgress.currentPrecent = 100;
                        sub.onNext(downloadProgress);

                        output.flush();
                        output.close();
                        input.close();
                    } else {
                        sub.onError(new IOException("下载失败 请检查下载链接"));
                    }
                } catch (Exception e) {
                    sub.onError(e);
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                        }
                    }
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                        }
                    }
                    //下载完成
                    sub.onCompleted();
                }

            }
        });
    }

    public static void main(String args[]) {
        RxOkhttpUtils3_2_0 instance = RxOkhttpUtils3_2_0.getInstance(false);
        RequestMap         params   = new RequestMap();
        params.putUrl("http://www.xdowns.com/");
        params.putForm("aaa", "getSolution");
        instance.post(params).subscribe(new Action1<IHttpCallBack.ResponseBean>() {
            @Override
            public void call(IHttpCallBack.ResponseBean responseBean) {
                System.out.println(responseBean.pair.key);
            }
        });
    }


}
