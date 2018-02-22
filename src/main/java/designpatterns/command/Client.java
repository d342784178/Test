package designpatterns.command;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:24
 */
public class Client {
    public static void main(String args[]) {
        IInvoker syncInvoker = new AsyncInvoker();
        IReceiver receiver = new IReceiver() {
            @Override
            public void action(String s) {
                System.out.println("exec: " + s);
            }

            @Override
            public void redo(String s) {
                System.out.println("redo: " + s);
            }


        };
        syncInvoker.invoke(new ACommand(receiver));
        syncInvoker.invoke(new AbsCommand(receiver) {

            private BCommand bCommand = new BCommand(iReceiver);
            private ACommand aCommand = new ACommand(iReceiver);

            @Override
            public void execute() {
                aCommand.execute();
                bCommand.execute();
            }

            @Override
            public void redo() {
                aCommand.redo();
                bCommand.redo();
            }
        });
        syncInvoker.invoke(new ACommand(receiver));

    }
}
