package 刷题.设计模式.备忘录;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 16:20
 */
public abstract class IBackup<T, R> {
    abstract protected T saveState();

    abstract protected Memento.Func<T, R> backupFunc();


    public Memento<T, R> store() {
        return new Memento<T, R>(saveState(), (R) this, backupFunc());
    }

    public void backup(Memento<T, R> memento) {
        memento.invoke();

    }

}
