package 刷题.设计模式.state;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 14:01
 */
public class CState extends AbsState {
    public CState(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void action1() {
        System.out.println("c-a");
        stateManager.setiState(stateManager.getaState());
    }

    @Override
    public void action2() {
        System.out.println("c->b");
        stateManager.setiState(stateManager.getbState());
    }
}
