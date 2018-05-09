package 刷题.设计模式.state;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 14:05
 */
public abstract class AbsState implements IState {
    StateManager stateManager;

    public AbsState(StateManager stateManager) {
        this.stateManager = stateManager;
    }
}
