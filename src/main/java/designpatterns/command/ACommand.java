package designpatterns.command;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:40
 */
public class ACommand extends AbsCommand {
    public ACommand(IReceiver iReceiver) {
        super(iReceiver);
    }

    @Override
    public void execute() {
        iReceiver.action("A");
    }

    @Override
    public void redo() {
        iReceiver.redo("A");
    }
}
