package 工具使用.bytebuddy.agent;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.ProtectionDomain;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-01-22
 * Time: 15:41
 */
public class ClassFileTransformerTest implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] bytes = null;
        if ("org/slf4j/impl/StaticLoggerBinder".equals(className)) {
            System.out.println("替换StaticLoggerBinder");
            try {
                URL resource = AgentTest.class.getClassLoader().getResource("StaticLoggerBinder.class");
                bytes = IOUtils.toByteArray(resource);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
