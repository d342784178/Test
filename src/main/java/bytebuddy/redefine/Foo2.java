package bytebuddy.redefine;

import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class Foo2 {
    static String m(@SuperCall Callable<String> callable) throws Exception{
//        String call = callable.call();
        return "foo2";
    }
}