package 工具使用.bytebuddy.agent;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class TimeInterceptor {
    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @AllArguments Object[] objects,
                                   @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        System.out.println(Arrays.toString(objects));
        try {
            // 原有函数执行
            return callable.call() + "intercept";
        } finally {
            System.out.println(method + ": took " + (System.currentTimeMillis() - start) + "ms");
        }
    }
}