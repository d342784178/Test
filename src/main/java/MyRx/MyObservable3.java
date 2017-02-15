package MyRx;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Desc: rxjava 支持线程切换的直接调用形式
 * 无回调方式
 * Author: DLJ
 * Date: 2017-02-07
 * Time: 17:09
 */
public abstract class MyObservable3<T> {
//    介绍:
//    subscribe是入口 会去调用start()方法
//    操作符方法都会返回一个新的 MyObservable对象 并且重写了start()方法来调用自身的start()方法

    private boolean runOnNewThread;

    public MyObservable3() {

    }

    public static <T> MyObservable3<T> just(T t) {
        return new MyObservable3<T>() {
            @Override
            public FutureTask<T> start() {
                return new FutureTask<T>(new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return t;
                    }
                });
            }
        };
    }

    public <R> MyObservable3<R> map(MyFunction<T, R> function) {
        MyObservable3<T> source = this;
        return new MyObservable3<R>() {
            @Override
            public FutureTask<R> start() {
                return new FutureTask<R>(new Callable<R>() {
                    @Override
                    public R call() throws Exception {
                        return function.convert(Worker.run(runOnNewThread, source.start()));
                    }
                });
            }
        };
    }

    public <R> MyObservable3<R> flatmap(MyFunction<T, MyObservable3<R>> function) {
        MyObservable3<T> source = this;
        return new MyObservable3<R>() {
            @Override
            public FutureTask<R> start() throws Exception {
                return new FutureTask<R>(new Callable<R>() {
                    @Override
                    public R call() throws Exception {
                        return function.convert(Worker.run(runOnNewThread, source.start())).start().get();
                    }
                });
            }
        };
    }

    abstract public FutureTask<T> start() throws Exception;

    public void subscribe(MyAction<T> action) {
        try {
            action.action(Worker.run(runOnNewThread, start()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyObservable3<T> setRunOnNewThread(boolean runOnNewThread) {
        this.runOnNewThread = runOnNewThread;
        return this;
    }

    public static void main(String args[]) {
        MyObservable3<Integer> source = MyObservable3.just(1);
        source.map(new MyFunction<Integer, String>() {
            @Override
            public String convert(Integer integer) {
                System.out.println(Thread.currentThread());
                return integer.toString();
            }
        })
                .setRunOnNewThread(true)
                .flatmap(new MyFunction<String, MyObservable3<Double>>() {
                    @Override
                    public MyObservable3<Double> convert(String s) {
                        System.out.println(Thread.currentThread());
                        return MyObservable3.just(Double.valueOf(s));
                    }
                })
                .setRunOnNewThread(true)
                .subscribe(s -> System.out.println(s));
    }

    public static class Worker {
        public static <T> T run(boolean runOnNewThread, FutureTask<T> futureTask) {
            if (runOnNewThread) {
                new Thread(futureTask).start();
            } else {
                futureTask.run();
            }
            try {
                T t = futureTask.get();
                return t;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
