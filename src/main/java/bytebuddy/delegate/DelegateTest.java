package bytebuddy.delegate;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-01-28
 * Time: 16:50
 */
public class DelegateTest {
    public static void main(String args[]) throws Exception {
        Source hello = new ByteBuddy()
                .subclass(Source.class)
                .method(named("hello")).intercept(MethodDelegation.to(InterceptorDelegate.class))
                //.method(named("hello")).intercept(MethodDelegation.to(InterceptorDelegate.class))
                .make()
                .load(DelegateTest.class.getClassLoader())
                .getLoaded()
                .newInstance();
        String helloWorld = hello
                .hello("World");
        System.out.println(helloWorld);
    }
}
