package 刷题.设计模式.备忘录;

import java.util.Random;

/**
 * Desc: 发起人
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 15:51
 */
public class Originnator extends IBackup<Integer, Originnator> {
    private int state = 0;

    public int changeState() {
        state = new Random().nextInt(1000);
        return state;
    }


    @Override
    protected Integer saveState() {
        return state;
    }

    @Override
    protected Memento.Func<Integer, Originnator> backupFunc() {
        return new Memento.Func<Integer, Originnator>() {
            @Override
            public void call(Integer state, Originnator origin) {
                origin.state = state;
            }
        };
    }


    public int getState() {
        return state;
    }
}
