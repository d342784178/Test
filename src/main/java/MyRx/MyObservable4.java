package MyRx;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Desc: rxjava 支持线程切换的回调形式
 * 无回调方式
 * Author: DLJ
 * Date: 2017-02-07
 * Time: 17:09
 */
public abstract class MyObservable4<T> {
//    介绍:
//    subscribe是入口 会去调用start()方法
//    操作符方法都会返回一个新的 MyObservable对象 并且重写了start()方法来调用自身的start()方法

    private boolean runOnNewThread;

    public MyObservable4() {

    }

    public static <T> MyObservable4<T> just(T t) {
        return new MyObservable4<T>() {
            @Override
            public void start(MyCallBack2<T> callback) throws Exception {
                callback.onSuccess(new FutureTask<T>(new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return t;
                    }
                }));
            }
        };
    }

    public <R> MyObservable4<R> map(MyFunction<T, R> function) {
        MyObservable4<T> source = this;
        return new MyObservable4<R>() {
            @Override
            public void start(MyCallBack2<R> callback) throws Exception {
                source.start(new MyCallBack2<T>() {
                    @Override
                    public void onSuccess(FutureTask<T> t) throws Exception {
                        callback.onSuccess(new FutureTask<R>(new Callable<R>() {
                            @Override
                            public R call() throws Exception {
                                return function.convert(MyObservable3.Worker.run(runOnNewThread, t));
                            }
                        }));
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
            }
        };
    }

    /**
     * 不支持切换线程
     * @param function
     * @param <R>
     * @return
     */
    public <R> MyObservable4<R> flatmap(MyFunction<T, MyObservable4<R>> function) {
        MyObservable4<T> source = this;
        return new MyObservable4<R>() {
            @Override
            public void start(MyCallBack2<R> callback) throws Exception {
                source.start(new MyCallBack2<T>() {
                    @Override
                    public void onSuccess(FutureTask<T> t) throws Exception {
                        function.convert(MyObservable3.Worker.run(runOnNewThread, t)).start(callback);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }
                });
            }
        };
    }

    /**
     * 不支持线程切换
     * @param function
     * @return
     */
    public MyObservable4<T> filter(MyFunction<T, Boolean> function) {
        MyObservable4<T> source = this;
        return new MyObservable4<T>() {
            @Override
            public void start(MyCallBack2<T> callBack) throws Exception {
                source.start(new MyCallBack2<T>() {
                    @Override
                    public void onSuccess(FutureTask<T> t) throws Exception {
                        Boolean convert = function.convert(MyObservable3.Worker.run(runOnNewThread, t));
                        if (convert) {
                            callBack.onSuccess(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }
                });
            }
        };
    }

    abstract public void start(MyCallBack2<T> callback) throws Exception;

    public void subscribe(MyAction<T> action) {
        try {
            start(new MyCallBack2<T>() {
                @Override
                public void onSuccess(FutureTask<T> t) {
                    action.action(MyObservable3.Worker.run(runOnNewThread, t));
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyObservable4<T> setRunOnNewThread(boolean runOnNewThread) {
        this.runOnNewThread = runOnNewThread;
        return this;
    }

    public static void main(String args[]) {
        MyObservable4<Integer> source = MyObservable4.just(1);
        source.map(new MyFunction<Integer, String>() {
            @Override
            public String convert(Integer integer) {
                System.out.println(Thread.currentThread());
                return integer.toString();
            }
        })
//                .setRunOnNewThread(true)
                .map(new MyFunction<String, String>() {
                    @Override
                    public String convert(String s) {
                        System.out.println(Thread.currentThread());
                        return s;
                    }
                })
                .setRunOnNewThread(true)
                .filter(new MyFunction<String, Boolean>() {
                    @Override
                    public Boolean convert(String s) {
                        System.out.println(Thread.currentThread());
                        return true;
                    }
                })
//                .setRunOnNewThread(true)
                .subscribe(s -> System.out.println(s));
    }
}
