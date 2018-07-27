package 刷题.设计模式.简单工厂;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 10:55
 */
public class AProduct implements IProduct {
    @Override
    public void echo() {
        System.out.println("a");
    }
}
