package redis;

import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Random;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-09-23
 * Time: 20:53
 */
public class RedisTest {
    public static void main(String args[]) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Jedis     jedis     = new Jedis("127.0.0.1", 6379);
                RedisLock redisLock = new RedisLock(jedis);
                redisLock.lock();
                try {
                    long millis = (long) (new Random().nextFloat() * 10000+5000);
                    System.out.println(millis + " " + new Date());
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                redisLock.unlock();
            }
        }).start();
    }
}
