package 刷题.设计模式.抽象工厂;

import 刷题.设计模式.简单工厂.IProduct;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 10:55
 */
public class ProductB1 implements IProductB {
    @Override
    public void echo() {
        System.out.println("b1");
    }
}
