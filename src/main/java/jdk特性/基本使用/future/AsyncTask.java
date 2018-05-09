package jdk特性.基本使用.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Desc:  仿造安卓的AsyncTask
 * Author: DLJ
 * Date: 2016-12-04
 * Time: 16:06
 */
public abstract class AsyncTask<T, R, Q> {
    private FutureTask<R> futureTask;
    private Worker<R, Q>  worker;

    public AsyncTask() {
        worker = new Worker<R, Q>() {
            @Override
            public R call() throws Exception {
                return doInBackground(getParam());
            }
        };
        futureTask = new FutureTask<>(worker);
    }

    public void execute(T t) throws Exception {
        if (!futureTask.isDone()) {
            worker.setParam(onPreview(t));
            new Thread(futureTask).start();
            onPost(futureTask.get());
        } else {
            throw new RuntimeException("只能执行一次");
        }
    }

    public void reset() {
        worker = new Worker<R, Q>() {
            @Override
            public R call() throws Exception {
                return doInBackground(getParam());
            }
        };
        futureTask = new FutureTask<>(worker);
    }

    abstract public Q onPreview(T t);

    abstract public R doInBackground(Q param);

    abstract public void onPost(R result);

    private abstract class Worker<R, Q> implements Callable<R> {
        Q param;

        public Q getParam() {
            return param;
        }

        public void setParam(Q param) {
            this.param = param;
        }
    }

    public static void main(String args[]) {
        try {
            AsyncTask<String, String, String> asyncTask = new AsyncTask<String, String, String>() {
                @Override
                public String onPreview(String s) {
                    System.out.println("onPreview");
                    System.out.println(Thread.currentThread());
                    return "onPreview";
                }

                @Override
                public String doInBackground(String param) {
                    System.out.println("doInBackground");
                    System.out.println(Thread.currentThread());
                    return "doInBackground";
                }

                @Override
                public void onPost(String result) {
                    System.out.println("onPost");
                    System.out.println(Thread.currentThread());
                }

            };
            asyncTask.execute("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
