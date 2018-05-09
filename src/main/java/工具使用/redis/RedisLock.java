package 工具使用.redis;

import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-09-23
 * Time: 20:56
 */
public class RedisLock {

    Jedis jedis;
    private String value;
    private final static String key = "key";

    public RedisLock(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean tryLock() {
        value = UUID.randomUUID().toString();
        Long setnx = jedis.setnx(key, value);
        if (setnx == 1) {//已经获取到锁 线程安全 所以无需加事务
            //TODO 如果此时宕机 则死锁  可以使用lua脚本保证原子性
            jedis.expire(key, 10);
        }
        return setnx == 1;
    }

    public void lock() {
        boolean b = tryLock();
        while (!b) {
            b = tryLock();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean tryLock(long time) {
        long    end = System.currentTimeMillis() + time;
        boolean b   = false;
        while (System.currentTimeMillis() < end) {
            b = tryLock();
            if (!b) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return b;
    }

    public void unlock() {
        String oldValue = jedis.get(key);
        if (value.equals(oldValue)) {//如果相同 则锁还在
            jedis.del(key);
        }
    }
}
