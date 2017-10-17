package classloader;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-08-06
 * Time: 14:40
 */
public class Test {
    public static void main(String args[]) {
        ClassLoader classLoader = Test.class.getClassLoader();
        System.out.println(classLoader);
    }
}
