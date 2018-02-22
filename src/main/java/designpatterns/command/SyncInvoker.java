package designpatterns.command;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:20
 */
public class SyncInvoker implements IInvoker {

    @Override
    public void invoke(ICommand command) {
        command.execute();
    }

}
