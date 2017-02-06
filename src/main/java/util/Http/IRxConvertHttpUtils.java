package util.Http;

import rx.Observable;

/**
 * Desc: 带数据解析
 * User: DLJ
 * Date: 2016-05-16
 * Time: 17:51
 */
public interface IRxConvertHttpUtils<E> extends IRxHttpUtils<E> {
    <T> Observable<T> post(final RequestMap params, Class<T> clazz);

    <T> Observable<T> get(final RequestMap params, Class<T> clazz);
}
