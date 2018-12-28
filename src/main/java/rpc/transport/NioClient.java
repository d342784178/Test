package rpc.transport;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
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
    public Object send(Class cls, Method method, Object[] param) {
        try {
            return new FutureTask<Object>(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    CountDownFutureTask countDownFutureTask = new CountDownFutureTask();
                    String              reqId               = readCallBack.register(countDownFutureTask);
                    ReqEntity reqEntity = new ReqEntity(reqId, cls.getCanonicalName(),
                            method.getName(), param);
                    ByteBuf buffer = new UnpooledByteBufAllocator(false).buffer();
                    reqEntity.outPut(buffer);
                    client.write(buffer.array());
                    FutureTask<Object> resFuture = new FutureTask<>(countDownFutureTask);
                    resFuture.run();
                    return resFuture.get();
                }
            }).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public class ReadCallBackImpl implements Client.ReadCallBack {
        private Map<String, CountDownFutureTask> map = Maps.newConcurrentMap();

        public String register(CountDownFutureTask countDownFutureTask) {
            String key = UUID.randomUUID().toString();
            map.put(key, countDownFutureTask);
            return key;
        }

        @Override
        public void onRead(ByteBuf readBuf) {
            //1.判断数据是否完整
            // 协议头完整
            if (readBuf.readableBytes() > 12) {
                int reqIdLength   = readBuf.readInt();
                int resTypeLength = readBuf.readInt();
                int resLength     = readBuf.readInt();
                //协议体完整
                if (readBuf.readableBytes() > (reqIdLength + resTypeLength + resLength)) {
                    ResEntity resEntity = ResEntity.read(readBuf, reqIdLength, resTypeLength, resLength);
                    //2.countDownlatch.countDown()
                    if (map.containsKey(resEntity.getReqId())) {
                        CountDownFutureTask countDownFutureTask = map.get(resEntity.getReqId());
                        try {
                            countDownFutureTask.setResult(JSON.parseObject(resEntity.getRes(),
                                    Class.forName(resEntity.getResType())));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        countDownFutureTask.complate();
                    }
                }
            }

        }
    }
}
