package rpc.client;

import rpc.user.Intf;
import rpc.transport.base.IClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-27
 * Time: 11:27
 */
public class ProxyFactory {

    public static Intf getRpcClientProxy(IClient rpcClient) {
        return (Intf) Proxy.newProxyInstance(ProxyFactory.class
                .getClassLoader(), new Class[]{Intf.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return rpcClient.send(method.getDeclaringClass(), method, args);
            }
        });
    }
}
