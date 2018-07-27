package 刷题.设计模式.备忘录;

import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Desc: 装饰器
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 16:29
 */
public class BackupManagerDecorator<T, R> extends IBackup<T, R> {
    private IBackup<T, R> originnator;
    private Deque<Memento<T, R>> mementoList = new LinkedBlockingDeque<>(100);

    public BackupManagerDecorator(IBackup<T, R> originnator) {
        this.originnator = originnator;
    }

    @Override
    protected T saveState() {
        return originnator.saveState();
    }

    @Override
    protected Memento.Func<T, R> backupFunc() {
        return originnator.backupFunc();
    }

    @Deprecated
    @Override
    public Memento<T, R> store() {
        Memento<T, R> store = originnator.store();
        mementoList.offerFirst(store);
        return store;
    }

    @Deprecated
    @Override
    public void backup(Memento<T, R> memento) {
        originnator.backup(memento);
    }


    public void autoStore() {
        store();
    }

    public void backup() {
        Memento<T, R> take = mementoList.pollFirst();
        take.invoke();
    }
}
