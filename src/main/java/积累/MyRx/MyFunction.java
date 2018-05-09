package 积累.MyRx;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-07
 * Time: 17:11
 */
public interface MyFunction<T, R> {
    R convert(T t);
}
