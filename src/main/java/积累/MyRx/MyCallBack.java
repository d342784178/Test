package 积累.MyRx;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-08
 * Time: 10:49
 */
public interface MyCallBack<T> {
    void onSuccess(T t);

    void onError(Throwable e);

}
