package 刷题.logic.兔子爬楼梯;

/**
 * Desc: 兔子爬楼梯问题
 * 兔子一下可以跳1格,2格,3格 请问10格楼梯有几种跳法
 * Author: DLJ
 * Date: 2017-02-11
 * Time: 15:05
 */
public class Rabbit {
    //1a+2b+3c=10;
    private int getA(int n, String s) {
        System.out.println(s + "," + n / 1);
        return 1;
    }

    private int getB(int n, String s) {
        int i   = n / 2;
        int t   = 0;
        int sum = 0;
        while (t <= i) {
            sum += getA(n - t * 2, s + "," + String.valueOf(t));
            t++;
        }
        return sum;
    }

    private int getNum(int n) {
        int i   = n / 3;
        int t   = 0;
        int sum = 0;
        while (t <= i) {
            sum += getB(n - t * 3, String.valueOf(t));
            t++;
        }
        return sum;
    }

    public static void main(String args[]) {
        System.out.println(new Rabbit().getNum(10));
    }
}
