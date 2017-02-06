package future;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Desc:
 * Author: DLJ
 * Date: 2016-12-04
 * Time: 15:59
 */
public class FutureTest {
    @Test
    public void futureTest() throws ExecutionException, InterruptedException {
        Callable<Integer> integerCallable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(5000);
                return 1;
            }
        };
        FutureTask<Integer> futureTask = new FutureTask<>(integerCallable);
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }
}
