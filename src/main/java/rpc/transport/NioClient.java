package rpc.transport;

import com.google.common.collect.Maps;
import rpc.Intf;
import rpc.ProxyFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 15:48
 */
public class NioClient implements IClient {

    public static final Charset UTF8 = Charset.forName("utf-8");

    private Client           client;
    private ReadCallBackImpl readCallBack = new ReadCallBackImpl();


    public static void main(String args[]) throws Exception {
        NioClient nioClient = new NioClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                nioClient.connect("127.0.0.1", 9800);
            }
        }).start();
        Intf           intfRpcClient = ProxyFactory.getRpcClientProxy(nioClient);
        BufferedReader br            = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter your value:");
            br.readLine();
            for (int i = 0; i < 10000; i++) {
                String result = intfRpcClient.invoke("a", "b");
            }
            //System.out.println(result);
        }

    }

    @Override
    public void connect(String host, int port) {
        client = new Client(host, port, readCallBack);
        try {
            client.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object send(Class cls, Method method, Object[] param) throws Exception {
        long                start = System.currentTimeMillis();
        CountDownFutureTask task  = new CountDownFutureTask();
        String              reqId = readCallBack.register(task);
        ReqEntity reqEntity = new ReqEntity(reqId, cls.getCanonicalName(),
                method.getName(), param);
        client.write(reqEntity);
        FutureTask<Object> resFuture = new FutureTask<>(task);
        resFuture.run();
        Object o = resFuture.get();
        System.out.println(System.currentTimeMillis() - start);
        return o;
    }

    public class CountDownFutureTask implements Callable<Object> {
        private CountDownLatch countDownLatch = new CountDownLatch(1);
        private Object         result;

        public CountDownFutureTask setResult(Object result) {
            this.result = result;
            return this;
        }

        public void complate() {
            countDownLatch.countDown();
        }

        @Override
        public Object call() throws Exception {
            countDownLatch.await();
            return result;
        }
    }

    public class ReadCallBackImpl implements ReadCallBack<ResEntity> {
        private Map<String, CountDownFutureTask> map = Maps.newConcurrentMap();

        public String register(CountDownFutureTask countDownFutureTask) {
            String key = UUID.randomUUID().toString();
            map.put(key, countDownFutureTask);
            return key;
        }

        @Override
        public void onRead(Context context, List<ResEntity> resEntityList) {
            for (ResEntity resEntity : resEntityList) {
                if (map.containsKey(resEntity.getReqId())) {
                    CountDownFutureTask countDownFutureTask = map.get(resEntity.getReqId());
                    countDownFutureTask.setResult(resEntity.getRes());
                    countDownFutureTask.complate();
                }
            }
        }
    }
}
