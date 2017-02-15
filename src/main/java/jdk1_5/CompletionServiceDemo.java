package jdk1_5;

import java.util.concurrent.*;

/**
 * Desc:  CompletionService
 * author: DLJ
 * Date: 2017/2/7
 */
public class CompletionServiceDemo implements Callable<String> {
    //CompletionService 类似 ThreadPool+BlockingQueue+FutureTask
    //用途:生产者消费者模型(如果没有消费者 当到达指定容量不会继续生产) 并且可以获取到生产的结果

    private int id;

    public CompletionServiceDemo(int i) {
        this.id = i;
    }

    public static void main(String[] args) throws Exception {
        ExecutorService           service    = Executors.newCachedThreadPool();
        CompletionService<String> completion = new ExecutorCompletionService<String>(service);
        for (int i = 0; i < 10; i++) {
            completion.submit(new CompletionServiceDemo(i));
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(completion.take().get());
        }
        service.shutdown();
    }

    public String call() throws Exception {
        Integer time = (int) (Math.random() * 1000);
        try {
            System.out.println(this.id + " start");
            Thread.sleep(time);
            System.out.println(this.id + " end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.id + ":" + time;
    }

}  
