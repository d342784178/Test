package jdk特性.jdk1_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Desc:
 * 原理: 共享锁
 * 1. 初始化时设定state为信号量个数
 * 2. acquire()和release()分别增减state值
 * 3. acquire()如果state<=0 则自旋阻塞
 * Author: ljdong2
 * Date: 2018/2/3
 */
public class SemaphoreDemo extends Thread {
    //Semaphore 类似一个信号池
    //用途:多个线程 争抢一个有限资源 例子:抢坑位

    private Semaphore position;
    private int       id;

    public SemaphoreDemo(int i, Semaphore s) {
        this.id = i;
        this.position = s;
    }

    public void run() {
        try {
            //有没有空厕所
            if (position.availablePermits() > 0) {
                System.out.println("顾客[" + this.id + "]进入厕所，有空位");
            } else {
                System.out.println("顾客[" + this.id + "]进入厕所，没空位，排队");
            }
            //获取到空厕所了  
            position.acquire();
            System.out.println("顾客[" + this.id + "]获得坑位");
            //使用中...  
            Thread.sleep((int) (Math.random() * 1000));
            System.out.println("顾客[" + this.id + "]使用完毕");
            //厕所使用完之后释放  
            position.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        ExecutorService list     = Executors.newCachedThreadPool();
        Semaphore       position = new Semaphore(2);//只有两个厕所
        //有十个人  
        for (int i = 0; i < 10; i++) {
            list.submit(new SemaphoreDemo(i + 1, position));
        }
        list.shutdown();
        //很重要!! 指导池子中有n个信号 才往下执行 否则阻塞
        position.acquireUninterruptibly(2);
        System.out.println("使用完毕，需要清扫了");
        position.release(2);
    }

}  