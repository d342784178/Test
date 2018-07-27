package 刷题.设计模式.访问者;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Desc: 访问者模式
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 17:25
 */
public class Client {
    public static void main(String args[]) {
        // 适用场景: 对相同data根据不同visitor有不同展示. 适用于data种类不变的情况
        // 优点: 1.便于对visitor的拓展
        // 缺点: 1.data的拓展不变,增加一个data就要改动所有的visitor (不符合开闭原则)
        //      2.对多个data的不同展示,可能排列组合出极多个visitor
        ArrayList<IData> iData = Lists.newArrayList(new DataA(), new DataB());
        iData.forEach(data -> data.accept(new VisitorA()));
    }
}
