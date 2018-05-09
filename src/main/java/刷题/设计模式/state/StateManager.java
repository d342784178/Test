package 刷题.设计模式.state;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 14:02
 */
public class StateManager {

    private IState iState;
    AState aState = new AState(this);
    BState bState = new BState(this);
    CState cState = new CState(this);

    public StateManager setiState(IState iState) {
        this.iState = iState;
        return this;
    }

    public void action1() {
        iState.action1();
    }

    public void action2() {
        iState.action2();
    }

    public AState getaState() {
        return aState;
    }

    public BState getbState() {
        return bState;
    }

    public CState getcState() {
        return cState;
    }
}
