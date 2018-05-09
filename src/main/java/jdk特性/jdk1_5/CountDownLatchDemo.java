package jdk特性.jdk1_5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Desc:
 * 原理: 共享锁
 * 1.实例化时设定state个数
 * 2.countdown()减少state个数
 * 3.await() 如果state!=0 则自旋阻塞
 * 4.只能使用一次 因为state只减不增
 *
 * Author: ljdong2
 * Date: 2018/2/3
 */
public class CountDownLatchDemo {
    //CountDownLatch 线程安全的计数器
    //用途:类似notifyAll 用与阻塞/唤醒所有线程

    public static void main(String[] args) throws InterruptedException {
        // 比赛开始的计数器
        final CountDownLatch begin = new CountDownLatch(3);
        // 比赛结束的计数器 (总共10名选手 所有计10个数)
        final CountDownLatch end = new CountDownLatch(10);
        // 十名选手  
        final ExecutorService exec = Executors.newFixedThreadPool(10);
        for (int index = 0; index < 10; index++) {
            final int NO = index + 1;
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        //阻塞等待比赛开始
                        begin.await();
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("No." + NO + " arrived");
                    } catch (InterruptedException e) {
                    } finally {
                        //一名选手到达 比赛结束计数器-1
                        end.countDown();
                    }
                }
            };
            exec.submit(run);
        }
        //开始倒数计时
        System.out.println("3");
        Thread.sleep(1000);
        begin.countDown();
        System.out.println("2");
        Thread.sleep(1000);
        begin.countDown();
        System.out.println("1");
        Thread.sleep(1000);
        begin.countDown();
        System.out.println("Game Start");

        //阻塞 知道比赛结束计数器为0
        end.await();
        System.out.println("Game Over");

        exec.shutdown();
    }
}  
