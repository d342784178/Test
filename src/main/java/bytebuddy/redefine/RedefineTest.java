package bytebuddy.redefine;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-01-28
 * Time: 16:21
 */
public class RedefineTest {

    public static void main(String args[]) throws IllegalAccessException, InstantiationException {
        Foo fBefore = new Foo();
        System.out.println(fBefore.m());
        ByteBuddyAgent.install();
        Foo foo = new Foo();
//        new ByteBuddy()
//                .redefine(Bar.class)
//                .name(Foo.class.getName())
//                .make()
//                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        foo = new ByteBuddy()
                .subclass(Foo.class)
                .method(ElementMatchers.named("m")).intercept(MethodDelegation.to(Foo2.class))
                .make()
                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded().newInstance();
        System.out.println(foo.m());
        Foo foo1 = new Foo();
        System.out.println(foo1.m());

        MemoryDatabase loggingDatabase = new ByteBuddy()
                .subclass(MemoryDatabase.class)
                .method(named("load")).intercept(MethodDelegation.to(LoggerInterceptor.class))
                .make()
                .load(RedefineTest.class.getClassLoader())
                .getLoaded()
                .newInstance();
        System.out.println(loggingDatabase.load("!23123"));;
    }



}
