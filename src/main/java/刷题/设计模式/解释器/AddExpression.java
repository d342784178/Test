package 刷题.设计模式.解释器;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-30
 * Time: 10:35
 */
public class AddExpression extends TwoExpression {
    public AddExpression(IExpression left, IExpression right) {
        super(left, right);
    }

    @Override
    public int interpreter(Map<String, Integer> context) {
        return left.interpreter(context) + right.interpreter(context);
    }
}
