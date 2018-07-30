package 刷题.设计模式.解释器;

import com.google.common.collect.Maps;

import java.util.HashMap;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-30
 * Time: 10:29
 */
public class Client {
    public static void main(String args[]) {
        HashMap<String, Integer> context = Maps.newHashMap();
        context.put("a", 5);
        context.put("b", 6);
        AddExpression addExpression = new AddExpression(new VarExpression("a"), new VarExpression("b"));
        System.out.println(addExpression);
        System.out.println(addExpression
                .interpreter(context));
    }
}
