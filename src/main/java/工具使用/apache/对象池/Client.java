package 工具使用.apache.对象池;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Desc: 对象池
 * Author: ljdong2
 * Date: 2018-07-30
 * Time: 11:00
 */
public class Client {
    public static void main(String args[]) throws Exception {
        //工厂
        LpPoolFactory factory = new LpPoolFactory();
        //资源池配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(2);
        //创建资源池
        GenericObjectPool<User> objectPool = new GenericObjectPool<User>(factory, poolConfig);

        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            //获取资源对象
            User user = objectPool.borrowObject();
            //将获取的资源对象，返还给资源池
            objectPool.returnObject(user);
            //输出资源对象
            System.out.println(user);
            System.out.println();
        }
    }
}
