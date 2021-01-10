package jdk特性.jdk1_5;

/**
 * Desc: 自动装箱&拆箱
 * Author: ljdong2
 * Date: 2021-01-10
 * Time: 16:14
 */
public class AutoBoxDemo {
    public static void main(String args[]) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long    g = 3L;
        System.out.println(c == d);
        System.out.println(e == f);
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b));
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));

        if(true){
            System.out.println(1);
        }else{
            System.out.println(2);
        }
    }
}
