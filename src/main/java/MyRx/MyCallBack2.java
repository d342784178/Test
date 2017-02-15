package MyRx;

import java.util.concurrent.FutureTask;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-08
 * Time: 10:49
 */
public interface MyCallBack2<T> {
    void onSuccess(FutureTask<T> t) throws Exception;

    void onError(Throwable e);

}
