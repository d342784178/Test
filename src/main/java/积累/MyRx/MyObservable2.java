package 积累.MyRx;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Collection;

/**
 * Desc: rxjava callback回调形式
 * 回调方式
 * Author: DLJ
 * Date: 2017-02-07
 * Time: 17:09
 */
public abstract class MyObservable2<T> {
//    介绍:
//    subscribe是入口 会去调用start()方法
//    操作符方法都会返回一个新的 MyObservable对象 并且重写了start()方法来调用自身的start()方法

    public MyObservable2() {

    }

    public static <T> MyObservable2<T> create(MyOnSubscribe<T> onSubscribe) {
        return new MyObservable2<T>() {
            @Override
            void start(MyCallBack<T> callBack) {
                onSubscribe.call(new MySubscribe<T>() {
                    @Override
                    public void onNext(T t) {
                        callBack.onSuccess(t);
                    }

                    @Override
                    public void onComplate() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }
                });
            }
        };
    }

    public static <T> MyObservable2<T> just(T t) {
        return new MyObservable2<T>() {
            @Override
            public void start(MyCallBack<T> callBack) {
                callBack.onSuccess(t);
            }
        };
    }

    public static <T> MyObservable2<T> from(Collection<T> collection) {
        return new MyObservable2<T>() {
            @Override
            void start(MyCallBack<T> callBack) {
                for (T t : collection) {
                    callBack.onSuccess(t);
                }
            }
        };
    }

    public <R> MyObservable2<R> map(MyFunction<T, R> function) {
        MyObservable2<T> source = this;
        return new MyObservable2<R>() {
            @Override
            public void start(MyCallBack<R> callBack) {
                source.start(new MyCallBack<T>() {
                    @Override
                    public void onSuccess(T t) {
                        callBack.onSuccess(function.convert(t));
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }
                });
            }
        };
    }

    public <R> MyObservable2<R> flatmap(MyFunction<T, MyObservable2<R>> function) {
        MyObservable2<T> source = this;
        return new MyObservable2<R>() {
            @Override
            public void start(MyCallBack<R> callBack) {
                source.start(new MyCallBack<T>() {
                    @Override
                    public void onSuccess(T t) {
                        function.convert(t).start(callBack);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }
                });
            }
        };
    }

    public MyObservable2<T> filter(MyFunction<T, Boolean> function) {
        MyObservable2<T> source = this;
        return new MyObservable2<T>() {
            @Override
            public void start(MyCallBack<T> callBack) {
                source.start(new MyCallBack<T>() {
                    @Override
                    public void onSuccess(T t) {
                        Boolean convert = function.convert(t);
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

    public MyObservable2<T> runOnMainThread() {
        MyObservable2<T> source = this;
        return new MyObservable2<T>() {
            @Override
            void start(MyCallBack<T> callBack) {
                source.start(new MyCallBack<T>() {
                    @Override
                    public void onSuccess(T t) {
                        Thread thread = Thread.currentThread();
                        if (thread.getId() != 0) {
                            thread.interrupt();
                            thread.stop();
                        }
                        callBack.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        };
    }

    public MyObservable2<T> runOnNewThread() {
        MyObservable2<T> source = this;
        return new MyObservable2<T>() {
            @Override
            void start(MyCallBack<T> callBack) {
                source.start(new MyCallBack<T>() {
                    @Override
                    public void onSuccess(T t) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(t);
                            }
                        }).start();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
            }
        };
    }

    abstract void start(MyCallBack<T> callBack);

    public void subscribe(MyAction<T> action) {
        start(new MyCallBack<T>() {
            @Override
            public void onSuccess(T t) {
                action.action(t);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }


    public static void main(String args[]) {
        MyObservable2<Integer> source = MyObservable2.just(1);
        MyObservable2<String> map = source
                .runOnNewThread()
                .map(new MyFunction<Integer, String>() {
                    @Override
                    public String convert(Integer integer) {
                        System.out.println(Thread.currentThread());
                        return integer.toString();
                    }
                });
        MyObservable2<Float> flatmap = map
                .runOnMainThread()
                .flatmap(new MyFunction<String, MyObservable2<Float>>() {
                    @Override
                    public MyObservable2<Float> convert(String s) {
                        System.out.println(Thread.currentThread());
                        return MyObservable2.just(Float.valueOf(s));
                    }
                });
        flatmap.subscribe(new MyAction<Float>() {
            @Override
            public void action(Float aFloat) {
                System.out.println(aFloat);
            }
        });
        // 流程:map.start()->flatmap.start()->source.start()->(source)onSuccess()->(map)onSuccess()->(flatmap)onSuccess
        // ()->action();


        MyObservable2.from(Lists.newArrayList("a", "b", "c")).filter(new MyFunction<String, Boolean>() {
            @Override
            public Boolean convert(String s) {
                return !Objects.equal("a", s);
            }
        }).subscribe(System.out::println);

    }

}
