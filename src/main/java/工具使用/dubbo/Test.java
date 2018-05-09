package 工具使用.dubbo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @desc TODO
 * @auth DLJ
 * @createDate 2017/8/15
 */
public class Test {
    public static void main(String[] args) {
        TestServiceImpl testService = new TestServiceImpl();
        Invoker invoker = new Invoker() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                try {
                    return method.invoke(testService, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        TestService o = (TestService) Proxy.newProxyInstance(Test.class.getClassLoader(), new Class[]{TestService
                                                                                                              .class}, new
                InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return invoker.invoke(proxy, method, args);
                    }
                });
        o.add();
    }
}
