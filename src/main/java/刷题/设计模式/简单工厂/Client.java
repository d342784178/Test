package 刷题.设计模式.简单工厂;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 10:55
 */
public class Client {
    public static void main(String args[]) {
        Factory factory = new Factory();
        factory.produce(AProduct.class).echo();
        factory.produce(BProduct.class).echo();
    }
}
