package 刷题.设计模式.command;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:22
 */
public abstract class AbsCommand implements ICommand {
    IReceiver iReceiver;

    public AbsCommand(IReceiver iReceiver) {
        this.iReceiver = iReceiver;
    }

}
