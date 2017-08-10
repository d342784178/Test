package jdk1_5;

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
        Good good = new Good();
        new AA(false, good).start();
//        new Thread(new AA(false, good)).start();

//        new Thread(new AA(true, good)).start();
        new AA(true, good).start();
        new AA(true, good).start();
        new AA(true, good).start();

    }

    private static class AA implements Runnable {
        private static int a = 0;

        private boolean consume;

        private Good good;

        public AA(boolean consume, Good good) {
            this.consume = consume;
            this.good = good;
        }

        public void start() {
            new Thread(this, consume ? "消费者" + a++ : "生产者" + a++).start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(new Random().nextInt(10) * 100 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (consume) {
                    good.consume();
                } else {
                    good.produce();
                }
            }
        }
    }

    private static class Good {
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
                    //非公平锁时使用if判断会有问题. 当empty.signal()时,准备唤醒线程,此时新的消费者线程加入直接获取到锁,消耗了商品,线程结束,之前的等待线程被唤醒,此时商品数量已经不对了
                    //改成while 循环判断可以排除非公平锁问题
                    System.out.println(threadName + "等待中,库存不足," + count);
                    empty.await();
                    System.out.println(threadName + "等待结束,库存已增加," + count);
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
