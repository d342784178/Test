package jdk1_5;

import org.junit.Test;

import java.util.concurrent.*;

public class FutureTaskDemo {
    /**
     * @param args
     * @exception InterruptedException
     * @exception ExecutionException
     * @exception InterruptedException
     * @exception ExecutionException
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        final ExecutorService exe = Executors.newFixedThreadPool(3);
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws InterruptedException {
                return "Thread is finished";
            }
        };
        Future<String> task = exe.submit(call);
        String         obj  = task.get();
        System.out.println(obj + "进程结束");
        System.out.println("总进程结束");
        exe.shutdown();
    }

    @Test
    public void futureTest() throws ExecutionException, InterruptedException {
        Callable<Integer> integerCallable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(5000);
                return 1;
            }
        };
        FutureTask<Integer> futureTask = new FutureTask<>(integerCallable);
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }

}
