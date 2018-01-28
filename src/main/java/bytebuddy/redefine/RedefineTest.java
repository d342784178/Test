package bytebuddy.redefine;

import bytebuddy.redefine.Bar;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-01-28
 * Time: 16:21
 */
public class RedefineTest {

    public static void main(String args[]) {
        Foo fBefore = new Foo();
        System.out.println(fBefore.m());
        ByteBuddyAgent.install();
        Foo foo = new Foo();
        new ByteBuddy()
                .redefine(Bar.class)
                .name(Foo.class.getName())
                .make()
                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(foo.m());
        Foo foo1 = new Foo();
        System.out.println(foo1.m());
    }
}
