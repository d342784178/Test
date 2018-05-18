package jdk特性.基本使用.nio;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Desc: 同步/异步 阻塞/非阻塞
 * Author: ljdong2
 * Date: 2018-03-23
 * Time: 16:10
 * 同步/异步:关注点在于当前线程是否等待 某操作结束 再继续执行
 * 阻塞/非阻塞:关注点在于 当数据尚未准备就绪时 是否立刻返回 然后以多次尝试获取的方式获取数据
 */
public class Sync {

    /**
     * 同步:当前线程等待操作完成后继续执行
     */
    public void sync() {
        System.out.println("同步执行开始");
        syncCall();
        System.out.println("同步执行结束");
    }

    public void syncCall() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("syncCall");
    }

    /**
     * 异步: 当前线程不等待操作完成
     */
    public void async() {
        System.out.println("异步执行开始");
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                syncCall();
            }
        });
        System.out.println("异步执行结束");

    }

    /**
     * 阻塞: 等待数据就绪
     */
    public void block() {
        System.out.println("阻塞执行开始");
        System.out.println(blockCall());
        System.out.println("阻塞执行结束");

    }

    public String blockCall() {
        try {
            //模拟等待数据就绪
            Thread.sleep(1000);
            return "ok";
        } catch (InterruptedException e) {
            return "fail";
        }
    }

    /**
     * 非阻塞: 不等待数据就绪,获取不到数据则立刻返回
     */
    public void unblock() {
        System.out.println("非阻塞执行开始");
        Future<String> future = unblockCall();
        boolean flag   = true;
        String  result = null;
        //多次尝试获取
        while (flag) {
            try {
                //模拟获取失败立刻返回
                result = future.get(100, TimeUnit.MILLISECONDS);
                flag = false;
            } catch (Exception e) {
                System.out.println("再次获取");
            }
        }
        System.out.println(result);
        System.out.println("非阻塞执行结束");

    }

    public Future<String> unblockCall() {
        return Executors.newSingleThreadExecutor().submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    Thread.sleep(1000);
                    return "ok";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "fail";
                }
            }
        });

    }

    /**
     * 同步阻塞:
     * 同步:当前线程等待执行结束
     * 阻塞:等待数据准备就绪
     */
    public void syncBlock() {
        System.out.println("同步阻塞执行开始");
        //同步:当前线程等待
        System.out.println(blockCall());
        System.out.println("同步阻塞执行结束");

    }

    /**
     * 同步阻塞:
     * 同步:当前线程等待执行结束
     * 阻塞:不等待数据准备就绪 获取不到数据则立刻返回
     */
    public void syncUnblock() {
        System.out.println("同步非阻塞执行开始");
        System.out.println(unblockCall());
        System.out.println("同步非阻塞执行结束");

    }

    /**
     * 异步阻塞:
     * 异步:当前线程不等待执行结束
     * 阻塞:等待数据准备就绪
     */
    public void asyncBlock() {
        System.out.println("异步阻塞执行开始");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(blockCall());

            }
        });
        System.out.println("异步阻塞执行结束");
    }


    /**
     * 异步非阻塞
     */
    public void asyncUnblock() {
        System.out.println("异步非阻塞执行开始");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Future<String> future = unblockCall();
                boolean flag   = true;
                String  result = null;
                //多次尝试获取
                while (flag) {
                    try {
                        //模拟获取失败立刻返回
                        result = future.get(100, TimeUnit.MILLISECONDS);
                        flag = false;
                    } catch (Exception e) {
                        System.out.println("再次获取");
                    }
                }
                System.out.println(result);
            }
        });
        System.out.println("异步非阻塞执行结束");
    }


    public static void main(String args[]) {
        Sync sync = new Sync();
//        sync.sync();
//        sync.async();
//        sync.block();
//        sync.unblock();
//        sync.syncBlock();
//        sync.syncUnblock();
//        sync.asyncBlock();
        sync.asyncUnblock();
    }

}
