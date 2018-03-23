package nio;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Desc: 同步/异步 阻塞/非阻塞
 * Author: ljdong2
 * Date: 2018-03-23
 * Time: 16:10
 * 同步/异步:区别在于是否会通过事件机制通知操作完成(简单来说就是是否使用回调的方式)
 * 阻塞/非阻塞:区别在于是否会等待操作完成(简单来说就是是否会立刻返回,拿到的是future还是真实结果)
 */
public class Sync {

    @Test
    public void sync() {
        System.out.println("同步执行开始");
        syncCall();
        System.out.println("同步执行结束");
    }

    public void syncCall() {
        System.out.println("syncCall");
    }

    @Test
    public void async() {
        System.out.println("异步执行开始");
        asyncCall(new Runnable() {
            @Override
            public void run() {
                System.out.println("同步执行结束");
            }
        });

    }

    public void asyncCall(Runnable runnable) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("asyncCall");
                runnable.run();
            }
        });
    }

    @Test
    public void block() {
        System.out.println("阻塞执行开始");
        blockCall();
        System.out.println("阻塞执行结束");

    }

    public void blockCall() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unblock() {
        System.out.println("非阻塞执行开始");
        unblockCall();
        System.out.println("非阻塞执行结束");

    }

    public void unblockCall() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void syncBlock() {
        System.out.println("同步阻塞执行开始");
        syncBlockCall();
        System.out.println("同步阻塞执行结束");

    }

    public void syncBlockCall() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void syncUnblock() {
        System.out.println("同步非阻塞执行开始");
        syncUnblockCall();
        System.out.println("同步非阻塞执行结束");

    }

    public void syncUnblockCall() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void asyncBlock() {
        System.out.println("异步阻塞执行开始");
        asyncBlockCall(new Runnable() {
            @Override
            public void run() {
                System.out.println("同步非阻塞执行结束");
            }
        });


    }

    public void asyncBlockCall(Runnable runnable) {
        Future<?> submit = Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runnable.run();
            }
        });
        try {
            submit.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void asyncUnblock() {
        System.out.println("异步非阻塞执行开始");
        asyncBlockCall(new Runnable() {
            @Override
            public void run() {
                System.out.println("同步非阻塞执行结束");
            }
        });


    }

    public void asyncUnblockCall(Runnable runnable) {
        Future<?> submit = Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runnable.run();
            }
        });

    }

}
