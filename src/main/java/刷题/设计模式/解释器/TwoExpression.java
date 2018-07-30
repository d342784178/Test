package 刷题.设计模式.解释器;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-30
 * Time: 10:34
 */
public abstract class TwoExpression implements IExpression {
    protected IExpression left;
    protected IExpression right;

    public TwoExpression(IExpression left, IExpression right) {
        this.left = left;
        this.right = right;
    }

}
