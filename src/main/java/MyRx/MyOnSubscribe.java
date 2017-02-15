package MyRx;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-08
 * Time: 11:23
 */
public interface MyOnSubscribe<T> {
    void call(MySubscribe<T> subscribe);
}
