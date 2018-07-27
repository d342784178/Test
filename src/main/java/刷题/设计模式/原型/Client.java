package 刷题.设计模式.原型;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 11:08
 */
public class Client {
    public static void main(String args[])throws Exception {
        //注意浅拷贝问题
        A a = new A();
        a.setA("a");
        System.out.println(a);
        A clone = (A) a.clone();
        clone.setA("b");
        System.out.println(a);
        System.out.println(clone);
    }
}
