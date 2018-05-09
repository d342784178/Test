package 积累.MyRx;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-08
 * Time: 11:34
 */
public interface MySubscribe<T> {
    void onNext(T t);
    void onComplate();
    void onError(Throwable e);
}
