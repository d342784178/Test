package 刷题.设计模式.抽象工厂;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 10:55
 */
public class Client {

    public static void main(String args[]) {
        //适合横向拓展
        Factory1 factory1 = new Factory1();
        factory1.produceA().echo();
        factory1.produceB().echo();

        Factory2 factory2 = new Factory2();
        factory2.produceA().echo();
        factory2.produceB().echo();
    }
}
