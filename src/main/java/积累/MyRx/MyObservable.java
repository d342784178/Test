package 积累.MyRx;

/**
 * Desc: rxjava 直接返回形式
 * 无回调方式
 * Author: DLJ
 * Date: 2017-02-07
 * Time: 17:09
 */
public abstract class MyObservable<T> {
//    介绍:
//    subscribe是入口 会去调用start()方法
//    操作符方法都会返回一个新的 MyObservable对象 并且重写了start()方法来调用自身的start()方法

    public MyObservable() {

    }

    public static <T> MyObservable<T> just(T t) {
        return new MyObservable<T>() {
            @Override
            public MyObserver<T> start() {
                return new MyObserver<T>() {
                    @Override
                    public T call() {
                        return t;
                    }
                };
            }
        };
    }

    public <R> MyObservable<R> map(MyFunction<T, R> function) {
        MyObservable<T> source = this;
        return new MyObservable<R>() {
            @Override
            public MyObserver<R> start() {
                return new MyObserver<R>() {
                    @Override
                    public R call() {
                        return function.convert(source.start().call());
                    }
                };
            }
        };
    }

    public <R> MyObservable<R> flatmap(MyFunction<T, MyObservable<R>> function) {
        MyObservable<T> source = this;
        return new MyObservable<R>() {
            @Override
            public MyObserver<R> start() {
                return function.convert(source.start().call()).start();
            }
        };
    }

    abstract public MyObserver<T> start();

    public void subscribe(MyAction<T> action) {
        action.action(start().call());
    }

    public static void main(String args[]) {
        MyObservable<Integer> source = MyObservable.just(1);
        MyObservable<String> map = source
                .map(new MyFunction<Integer, String>() {
                    @Override
                    public String convert(Integer integer) {
                        return integer.toString();
                    }
                });
        MyObservable<Float> flatmap = map
                .flatmap(new MyFunction<String, MyObservable<Float>>() {
                    @Override
                    public MyObservable<Float> convert(String s) {
                        return MyObservable.just(Float.valueOf(s));
                    }
                });
        flatmap.subscribe(new MyAction<Float>() {
            @Override
            public void action(Float aFloat) {
                System.out.println(aFloat);

            }
        });


    }

}
