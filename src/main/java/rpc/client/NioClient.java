package rpc.client;

import com.google.common.collect.Maps;
import lombok.Getter;
import rpc.user.Intf;
import rpc.transport.base.Client;
import rpc.transport.base.IClient;
import rpc.transport.base.ReadCallBack;
import rpc.transport.context.Context;
import rpc.transport.dto.ReqEntity;
import rpc.transport.dto.ResEntity;

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

    @Getter
    private String serverHost;
    @Getter
    private int    serverPort;

    private Client           client;
    private ReadCallBackImpl readCallBack = new ReadCallBackImpl();

    public static Intf genClient(String serverHost, int serverPort) throws Exception {
        NioClient nioClient = new NioClient(serverHost, serverPort);
        return ProxyFactory.getRpcClientProxy(nioClient);
    }

    public NioClient(String serverHost, int serverPort) throws Exception {
        client = new Client(serverHost, serverPort, readCallBack);
    }

    @Override
    public Object send(Class cls, Method method, Object[] param) throws Exception {
        //long                start = System.currentTimeMillis();
        CountDownFutureTask task  = new CountDownFutureTask();
        String              reqId = readCallBack.register(task);
        ReqEntity reqEntity = new ReqEntity(reqId, cls.getCanonicalName(),
                method.getName(), param);
        client.write(reqEntity);
        FutureTask<Object> resFuture = new FutureTask<>(task);
        resFuture.run();
        Object o = resFuture.get();
        //System.out.println(System.currentTimeMillis() - start);
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
