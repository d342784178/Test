package designpatterns.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:38
 */
public class AsyncInvoker implements IInvoker {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    public void invoke(ICommand command) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                command.execute();
            }
        });
    }
}
