package 工具使用.bytebuddy.agent;


import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * Desc: -javaagent:/Users/mic/IdeaProjects/CtrTimeOut/agentTest/target/agentTest-jar-with-dependencies.jar
 * <plugin>
 * <artifactId>maven-assembly-plugin</artifactId>
 * <version>2.5.2</version>
 * <configuration>
 * <archive>
 * <manifestEntries>
 * <Premain-Class>
 * com.iflytek.ossp.agenttest.AgentTest
 * </Premain-Class>
 * </manifestEntries>
 * </archive>
 * <descriptorRefs>
 * <descriptorRef>jar-with-dependencies</descriptorRef>
 * </descriptorRefs>
 * </configuration>
 * </plugin>
 *
 * Author: ljdong2
 * Date: 2018-01-16
 * Time: 15:44
 */
public class AgentTest {
    /**
     * 该方法在main方法之前运行，与main方法运行在同一个JVM中
     * 并被同一个System ClassLoader装载
     * 被统一的安全策略(security policy)和上下文(context)管理
     * @param agentOps
     * @param inst
     * @author SHANHY
     * @create 2016年3月30日
     */
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法执行========");
        //inst.addTransformer(new ClassFileTransformerTest());


        AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription,
                                                    ClassLoader classLoader, JavaModule module) {
                return builder
                        .method(ElementMatchers.<MethodDescription>any()) // 拦截任意方法
                        .intercept(MethodDelegation.to(TimeInterceptor.class)); // 委托
            }
        };

        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                         boolean loaded, DynamicType dynamicType) {
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module,
                                  boolean loaded) {
            }

            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded,
                                Throwable throwable) {
            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
            }

        };

        new AgentBuilder
                .Default()
                .type(ElementMatchers.nameStartsWith("com.iflytek.ossp.ctrtimeout")) // 指定需要拦截的类
                .transform(transformer)
                .with(listener)
                .installOn(inst);
    }


    /**
     * 如果不存在 premain(String agentOps, Instrumentation inst)
     * 则会执行 premain(String agentOps)
     * @param agentOps
     * @author SHANHY
     * @create 2016年3月30日
     */
    public static void premain(String agentOps) {
        System.out.println("=========premain方法执行2========");
        System.out.println(agentOps);
    }


    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("=========agentmain方法执行========");
        System.out.println(agentOps);
        System.out.println(inst);
    }

    public static void agentmain(String agentArgs) {
        System.out.println("=========agentmain方法执行========");
    }

    //public static void main(String args[]) {
    //    Class<?> dynamicType = new ByteBuddy()
    //            .subclass(Object.class)
    //            .method(ElementMatchers.named("toString"))
    //            .intercept(FixedValue.value("Hello World!"))
    //            .make()
    //            .load(AgentTest.class.getClassLoader(),
    //                    ClassLoadingStrategy.Default.WRAPPER)
    //            .getLoaded();
    //    System.out.println(dynamicType);
    //}
}
