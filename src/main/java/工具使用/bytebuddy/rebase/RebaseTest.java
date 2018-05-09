//package agenttest.rebase;
//
//import net.bytebuddy.ByteBuddy;
//import net.bytebuddy.agent.ByteBuddyAgent;
//import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
//
//import java.util.Arrays;
//
///**
// * Desc:
// * Author: ljdong2
// * Date: 2018-01-28
// * Time: 16:21
// */
//public class RebaseTest {
//
//    public static void main(String args[]) throws Exception {
//        Foo fBefore = new Foo();
//        System.out.println(fBefore.m());
//        ByteBuddyAgent.install();
//        Foo foo = new Foo();
//        Foo foo2 = new ByteBuddy()
//                .rebase(Foo.class)
//                .make()
//                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
//                .getLoaded().newInstance();
//        System.out.println(foo.m());
//        Foo foo1 = new Foo();
//        System.out.println(foo1.m());
//        System.out.println(Arrays.toString(foo.getClass().getDeclaredMethods()));
//        System.out.println(Arrays.toString(foo1.getClass().getDeclaredMethods()));
//        System.out.println(Arrays.toString(foo2.getClass().getDeclaredMethods()));
//    }
//}
