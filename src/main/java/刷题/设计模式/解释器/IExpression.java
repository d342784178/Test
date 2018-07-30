package 刷题.设计模式.解释器;

import java.util.Map;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-30
 * Time: 10:29
 */
public interface IExpression {
    int interpreter(Map<String, Integer> context);
}
