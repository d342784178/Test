package 工具使用.bytebuddy.delegate;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * InterceptorDelegate.java
 * 优先方法名匹配,没有则取唯一方法,否则报错
 **/
public class InterceptorDelegate {
    @RuntimeType
    public static Object hello(@AllArguments Object[] objs, @SuperCall Callable<?> zuper)
            throws Exception {
        System.out.println("InterceptorDelegate before");
        System.out.println(Arrays.toString(objs));
        try {
            //return zuper.call();
            return "hello1 " + objs[0];
        } finally {
            System.out.println("InterceptorDelegate after");
        }
    }
}