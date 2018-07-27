package 刷题.设计模式.抽象工厂;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 10:58
 */
public interface IFactory {
    IProductA produceA();

    IProductB produceB();
}
