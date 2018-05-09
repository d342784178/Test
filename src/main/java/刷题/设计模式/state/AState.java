package 刷题.设计模式.state;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 14:01
 */
public class AState extends AbsState {
    public AState(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void action1() {
        System.out.println("a->b");
        stateManager.setiState(stateManager.getbState());
    }

    @Override
    public void action2() {
        System.out.println("a->c");
        stateManager.setiState(stateManager.getcState());
    }
}
