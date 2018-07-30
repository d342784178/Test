package 刷题.设计模式.解释器;

import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-30
 * Time: 10:39
 */
public class VarExpression implements IExpression {
    private String variable;

    public VarExpression(String variable) {
        this.variable = variable;
    }

    @Override
    public int interpreter(Map<String, Integer> context) {
        return context.get(variable);
    }

}
