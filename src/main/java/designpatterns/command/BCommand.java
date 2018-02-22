package designpatterns.command;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:40
 */
public class BCommand extends AbsCommand {
    public BCommand(IReceiver iReceiver) {
        super(iReceiver);
    }

    @Override
    public void execute() {
        iReceiver.action("b");
    }

    @Override
    public void redo() {
        iReceiver.redo("b");
    }
}
