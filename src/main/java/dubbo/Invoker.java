package dubbo;

import java.lang.reflect.Method;

/**
 * @desc TODO
 * @auth DLJ
 * @createDate 2017/8/15
 */
public interface Invoker {
    Object invoke(Object proxy, Method method, Object[] args);
}
