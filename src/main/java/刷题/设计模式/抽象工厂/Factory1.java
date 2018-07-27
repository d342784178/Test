package 刷题.设计模式.抽象工厂;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 10:54
 */
public class Factory1 implements IFactory {

    @Override
    public IProductA produceA() {
        return new ProductA1();
    }

    @Override
    public IProductB produceB() {
        return new ProductB1();
    }
}
