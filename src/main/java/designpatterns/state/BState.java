package designpatterns.state;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 14:01
 */
public class BState extends AbsState {
    public BState(StateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void action1() {
        System.out.println("b->c");
        stateManager.setiState(stateManager.getcState());
    }

    @Override
    public void action2() {
        System.out.println("b->a");
        stateManager.setiState(stateManager.getaState());
    }
}
