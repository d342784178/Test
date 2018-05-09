package 刷题.设计模式.command;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:40
 */
public class CCommand extends AbsCommand{
    public CCommand(IReceiver iReceiver) {
        super(iReceiver);
    }

    @Override
    public void execute() {
        iReceiver.action("C");
    }

    @Override
    public void redo() {
        iReceiver.redo("C");
    }
}
