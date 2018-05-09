package 刷题.设计模式.state;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 14:01
 */
public class Client {
    public static void main(String args[]) {
        StateManager stateManager = new StateManager();
        stateManager.setiState(stateManager.getaState());
        stateManager.action1();
        stateManager.action1();
        stateManager.action2();
    }
}
