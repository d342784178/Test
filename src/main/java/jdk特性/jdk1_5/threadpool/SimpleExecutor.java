package jdk特性.jdk1_5.threadpool;

import java.util.concurrent.Executor;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-08-08
 * Time: 14:13
 */
public class SimpleExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }

    public static void main(String args[]) {
        new SimpleExecutor().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("run");
            }
        });
    }
}
