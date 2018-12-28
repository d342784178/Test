package rpc.transport;

import java.lang.reflect.Method;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 15:45
 */
public interface IClient {
    void connect(String host, int port);

    Object send(Class cls, Method method, Object[] param);

}
