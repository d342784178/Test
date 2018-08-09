package jdk特性.jdk1_5.threadpool;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-08-08
 * Time: 14:15
 */
public class SimpleExecutorService implements ExecutorService {
    private static Logger log = LoggerFactory.getLogger(SimpleExecutorService.class);

    private BlockingQueue<FutureTask> queue   = new ArrayBlockingQueue<FutureTask>(1000);
    private List<Worker>              workers = Lists.newArrayList();

    private volatile     int state          = STATE_NEW;
    private static final int STATE_NEW      = 0;
    private static final int STATE_SHUTDOWN = -1;
    private RejectedExecutionHandler rejectedExecutionHandler;


    public SimpleExecutorService(int workerNum, RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        for (int i = 0; i < workerNum; i++) {
            workers.add(new Worker());
        }
    }

    /**
     * 标记停止 不接收新任务
     */
    @Override
    public void shutdown() {
        //标记shutdown
        UNSAFE.compareAndSwapInt(this, stateOffset, STATE_NEW, STATE_SHUTDOWN);
    }

    /**
     * 停止当前任务,返回等待执行任务列表
     */
    @Override
    public List<Runnable> shutdownNow() {
        shutdown();
        //停止当前执行任务
        workers.forEach(worker -> {
            worker.shutdown();
            worker.interrupt();
        });
        //返回剩余任务
        return new ArrayList<>(queue);
    }

    @Override
    public boolean isShutdown() {
        return state == STATE_SHUTDOWN;
    }

    /**
     * isShutdown()&&当前所有任务已结束
     */
    @Override
    public boolean isTerminated() {
        return isShutdown() && workers.stream().allMatch(Worker::isComlated);
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        if (!isShutdown()) {
            throw new RuntimeException("does not shutdowned");
        }
        if (workers.stream().allMatch(Worker::isComlated)) {
            return true;
        } else {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Worker worker : workers) {
                        worker.shutdown();
                        try {
                            worker.awaitTerminated();
                        } catch (InterruptedException e) {
                            log.warn("", e);
                            return;
                        }
                    }
                    countDownLatch.countDown();
                }
            }).start();
            return countDownLatch.await(timeout, unit);
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (isShutdown()) {
            throw new RuntimeException("is shutdowned");
        }
        FutureTask<T> futureTask = new FutureTask<>(task);
        try {
            if (!queue.offer(futureTask, 200, TimeUnit.MILLISECONDS)) {
                rejectedExecutionHandler.rejectedExecution(futureTask, this);
            }
            return futureTask;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                task.run();
                return result;
            }
        });
    }

    @Override
    public Future<?> submit(Runnable task) {
        return submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                task.run();
                return null;
            }
        });
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        if (isShutdown()) {
            throw new RuntimeException("is shutdowned");
        }
        List<Future<T>> collect = tasks.stream().map(this::submit).collect(Collectors.toList());
        return getAll(collect);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
            InterruptedException {
        if (isShutdown()) {
            throw new RuntimeException("is shutdowned");
        }
        List<Future<T>> collect = tasks.stream().map(this::submit).collect(Collectors.toList());
        try {
            return new FutureTask<>(() -> getAll(collect)).get(timeout, unit);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            log.warn("", e);
            return collect;
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        if (isShutdown()) {
            throw new RuntimeException("is shutdowned");
        }
        List<Future<T>> collect = tasks.stream().map(this::submit).collect(Collectors.toList());
        return getAny(collect);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
            InterruptedException, ExecutionException, TimeoutException {
        if (isShutdown()) {
            throw new RuntimeException("is shutdowned");
        }
        List<Future<T>> collect = tasks.stream().map(this::submit).collect(Collectors.toList());
        return new FutureTask<>(() -> getAny(collect)).get(timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        submit(command);
    }

    private static <T> T getAny(List<Future<T>> futures) throws InterruptedException {
        T result = null;
        for (Future<T> tFuture : futures) {
            try {
                result = tFuture.get();
            } catch (ExecutionException e) {
                log.warn("", e);
            }
        }
        return result;
    }

    private static <T> List<Future<T>> getAll(List<Future<T>> futures) throws InterruptedException {
        for (Future<T> tFuture : futures) {
            try {
                tFuture.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return futures;
    }

    private class Worker implements Runnable {
        private       boolean        shutdown;
        private       boolean        complate;
        private       Thread         thread;
        private final CountDownLatch complateCountDown;

        public Worker() {
            thread = new Thread(this);
            thread.start();
            complateCountDown = new CountDownLatch(1);
        }

        @Override
        public void run() {
            while (!shutdown) {
                try {
                    FutureTask futureTask = queue.poll(500, TimeUnit.MILLISECONDS);
                    if (futureTask != null) {
                        futureTask.run();
                    }
                } catch (InterruptedException e) {
                    log.warn("", e);
                }
            }
            complate = true;
            complateCountDown.countDown();
        }

        public void shutdown() {
            shutdown = true;
        }

        public void awaitTerminated() throws InterruptedException {
            if (!shutdown) {
                throw new RuntimeException("does not shutdown");
            }
            complateCountDown.await();
        }

        public boolean awaitTerminated(long time, TimeUnit unit) throws InterruptedException {
            if (!shutdown) {
                throw new RuntimeException("does not shutdown");
            }
            return complateCountDown.await(time, unit);
        }

        public void interrupt() {
            thread.interrupt();
        }

        public boolean isComlated() {
            return complate;
        }
    }

    private static final sun.misc.Unsafe UNSAFE;
    private static final long            stateOffset;

    static {
        try {
            UNSAFE = getUnsafe();
            Class<?> k = SimpleExecutorService.class;
            stateOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("state"));
        } catch (Exception e) {
            throw new Error(e);
        }

    }

    private static Unsafe getUnsafe() throws Exception {
        Class<?> unsafeClass = Unsafe.class;
        for (Field f : unsafeClass.getDeclaredFields()) {
            if ("theUnsafe".equals(f.getName())) {
                f.setAccessible(true);
                return (Unsafe) f.get(null);
            }
        }
        throw new IllegalAccessException("no declared field: theUnsafe");
    }

    public interface RejectedExecutionHandler {

        /**
         * Method that may be invoked by a {@link ThreadPoolExecutor} when
         * {@link ThreadPoolExecutor#execute execute} cannot accept a
         * task.  This may occur when no more threads or queue slots are
         * available because their bounds would be exceeded, or upon
         * shutdown of the Executor.
         * <p>In the absence of other alternatives, the method may throw
         * an unchecked {@link RejectedExecutionException}, which will be
         * propagated to the caller of {@code execute}.
         * @param r        the runnable task requested to be executed
         * @param executor the executor attempting to execute this task
         * @exception RejectedExecutionException if there is no remedy
         */
        void rejectedExecution(Runnable r, ExecutorService executor);
    }

    public static class AbortPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ExecutorService e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }


}
