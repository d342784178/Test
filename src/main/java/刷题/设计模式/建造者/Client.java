package 刷题.设计模式.建造者;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 11:16
 */
public class Client {
    public static void main(String args[]) {
        //适用于实例化过程较为复杂 且需要所有参数就绪才能实例化的场景
        A build = new A.Builder().setA("a").setB("b").setC("c").build();
    }
}
