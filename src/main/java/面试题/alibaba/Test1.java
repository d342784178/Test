package 面试题.alibaba;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Desc:篮子里有100个苹果，将这些苹果进行编号，从0开始到99，
 * 现在将这些苹果分成4份，第一份20个，第二份30个，第三份38个，最后一份11个，
 * 要求是苹果的编号不能连续。
 * Author: DLJ
 * Date: 2017-08-10
 * Time: 19:18
 */
public class Test1 {
    public static void main(String args[]) {
        new Test1();
    }

    public Test1() {
        init();
    }

    public void init() {
//        int aSize = 20;
//        int bSize = 30;
//        int cSize = 38;
//        int dSize = 11;
        //思路:
        //1. 篮子a,b,c 减去最少的篮子d的数量11 变为 9,19,27
        //2. 9+19>27 说明可以实现每个篮子苹果编号不连续
        //3. 对a 9,b 19,c 27分配苹果,a b c都剩余11个空位 轮流分配苹果
        ArrayList<String> objects = Lists.newArrayList();
        for (int i = 0; i < 27; i++) {
            objects.add("c");
            if (i < 9) {
                objects.add("a");
            } else {
                objects.add("b");
            }
        }
        objects.add("b");
        int size = objects.size();
        for (int i = 0; i < (100 - size) / 4; i++) {
            objects.add("a");
            objects.add("b");
            objects.add("d");
            objects.add("c");
        }
        System.out.println(objects.size());
        System.out.println(objects);
        java.util.List<Object> listA = Lists.newArrayList();
        java.util.List<Object> listB = Lists.newArrayList();
        java.util.List<Object> listC = Lists.newArrayList();
        java.util.List<Object> listD = Lists.newArrayList();
        for (int i = 0; i < objects.size(); i++) {
            switch (objects.get(i)) {
                case "a":
                    listA.add(i);
                    break;
                case "b":
                    listB.add(i);
                    break;
                case "c":
                    listC.add(i);
                    break;
                case "d":
                    listD.add(i);
                    break;
                default: break;
            }
        }
        System.out.println("");
        System.out.println(listA);
        System.out.println(listA.size());
        System.out.println(listB);
        System.out.println(listB.size());
        System.out.println(listC);
        System.out.println(listC.size());
        System.out.println(listD);
        System.out.println(listD.size());
    }
}
