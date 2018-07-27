package 刷题.设计模式.访问者;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 17:20
 */
public class DataB implements IData {

    @Override
    public void accept(IVisitor visiter) {
        visiter.visit(this);
    }
}
