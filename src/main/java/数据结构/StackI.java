package 数据结构;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-03-28
 * Time: 20:09
 */
public interface StackI<T> {
    T pop();

    void push(T t);

    boolean isEmpty();

    T peek();
    int size();
}
