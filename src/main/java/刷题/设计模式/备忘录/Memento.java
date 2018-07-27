package 刷题.设计模式.备忘录;

/**
 * Desc: 备忘录
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 15:51
 */
public class Memento<T, R> {
    private T          state;
    private R          origin;
    private Func<T, R> func;

    public Memento(T state, R origin, Func<T, R> func) {
        this.origin = origin;
        this.state = state;
        this.func = func;
    }

    public interface Func<T, R> {
        void call(T state, R origin);
    }

    public void invoke() {
        func.call(state, origin);
    }
}
