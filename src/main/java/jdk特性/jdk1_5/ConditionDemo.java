package jdk特性.jdk1_5;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @desc TODO
 * @auth DLJ
 * @createDate 2017/8/4
 */
public class ConditionDemo {
    public static void main(String[] args) {
        Factory factory = new Factory();
        new Worker(false, factory).start();
//        new Worker(false, factory).start();

        new Worker(true, factory).start();
        new Worker(true, factory).start();
        new Worker(true, factory).start();

    }

    private static class Worker implements Runnable {
        private static int a = 0;

        private boolean consume;

        private Factory factory;

        public Worker(boolean consume, Factory factory) {
            this.consume = consume;
            this.factory = factory;
        }

        public void start() {
            new Thread(this, consume ? "消费者" + a++ : "生产者" + a++).start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(new Random().nextInt(10) * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (consume) {
                    factory.consume();
                } else {
                    factory.produce();
                }
            }
        }
    }

    private static class Factory {
        ReentrantLock lock = new ReentrantLock();

        Condition full = lock.newCondition();

        Condition empty = lock.newCondition();

        private int count;

        private final int total = 10;

        public void produce() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                while (count + 1 > total) {
                    System.out.println(threadName + "等待中,库存已满," + count);
                    full.await();
                    System.out.println(threadName + "等待结束,库存已消耗," + count);
                    Thread.sleep(100);
                }
                count += 1;

                System.out.println(threadName + "库存增加,剩余" + count);
                empty.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void consume() {
            try {
                String threadName = Thread.currentThread().getName();
                lock.lock();
                while (count - 1 < 0) {
                    //非公平锁时使用if判断会有问题. 当empty.signal()时,线程1从等待队列进入同步队列
                    //此时新的消费者线程2加入直接获取到锁,消耗商品后锁释放,之前的线程1被唤醒,此时商品已经被消耗,数量已经不对了
                    //改成while 循环判断可以排除非公平锁问题
                    System.out.println(threadName + "等待中,库存不足," + count);
                    empty.await();
                    System.out.println(threadName + "等待结束,库存已增加," + count);
                    Thread.sleep(100);
                }
                count -= 1;
                System.out.println(Thread.currentThread().getName() + "库存减少,剩余" + count);
                full.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

}
