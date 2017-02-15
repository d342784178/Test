package jdk1_5;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Desc: BlockingQueue例子
 * Author: DLJ
 * Date: 2017/2/7
 */
public class BlockingQueueDemo extends Thread {
    /*
    * 简介:
    * BlockingQueue是个有序队列 可以限定队列长度
    * 当超过长度时put take会阻塞
    *
    * 用途:生产者消费者模型(如果没有消费者 当到达指定容量不会继续生产)
    */

    public static BlockingQueue<String> queue = new LinkedBlockingQueue<String>(3);
    private int index;

    public BlockingQueueDemo(int i) {
        this.index = i;
    }

    public void run() {
        try {
            queue.put(String.valueOf(this.index));
            System.out.println("{" + this.index + "} in queue!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            service.submit(new BlockingQueueDemo(i));
        }

        Thread thread = new Thread() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep((int) (Math.random() * 1000));
                        if (BlockingQueueDemo.queue.isEmpty()) {
                            break;
                        }
                        String str = BlockingQueueDemo.queue.take();
                        System.out.println(str + " has take!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        service.submit(thread);
        service.shutdown();
    }

}  

