package util.Http.okhttp;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import util.GsonTools;
import util.Http.IHttpCallBack;
import util.Http.IRxConvertHttpUtils;
import util.Http.RequestMap;

/**
 * Desc:自动数据解析
 * User: DLJ
 * Date: 2016-05-16
 * Time: 17:53
 */
public class RxConvertOKhttpUtil3_2_0 extends RxOkhttpUtils3_2_0 implements
        IRxConvertHttpUtils<Response> {
    private static RxConvertOKhttpUtil3_2_0 instance;

    protected RxConvertOKhttpUtil3_2_0(boolean isDebug) {
        super(isDebug);
    }

    public static RxConvertOKhttpUtil3_2_0 getInstance(boolean isDebug) {
        if (instance == null) {
            instance = new RxConvertOKhttpUtil3_2_0(isDebug);
        }
        return instance;
    }

    @Override
    public <T> Observable<T> post(final RequestMap params, final Class<T> clazz) {
        return jsonToBean(post(params), clazz);
    }

    public <T> Observable<T> jsonToBean(Observable<IHttpCallBack.ResponseBean> observable, final
    Class<T> clazz) {
        return observable.flatMap(new Func1<IHttpCallBack.ResponseBean, Observable<T>>() {
            @Override
            public Observable<T> call(final IHttpCallBack.ResponseBean responseBean) {
                return Observable.create(new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            T t = GsonTools.jsonToBean(responseBean.pair.key, clazz);
                            subscriber.onNext(t);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(new Throwable(responseBean.pair.key));
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public <T> Observable<T> get(RequestMap params, final Class<T> clazz) {
        return jsonToBean(get(params), clazz);
    }
}
