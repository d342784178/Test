package rpc.transport.base;

import java.lang.reflect.Method;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-09-07
 * Time: 15:45
 */
public interface IClient {

    Object send(Class cls, Method method, Object[] param) throws Exception;

}
