package 刷题.设计模式.访问者;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 17:26
 */
public class VisitorA implements IVisitor{
    @Override
    public void visit(DataA typeA) {
        System.out.println("VisitorA visit typeA");
    }

    @Override
    public void visit(DataB typeB) {
        System.out.println("VisitorA visit typeB");
    }
}
