package leetcode;

/**
 * Desc: 求幂
 * Author: DLJ
 * Date: 2017-03-02
 * Time: 15:34
 */
public class Pow {
    public double myPow(double x, int n) {
        double ans = 1;
        for (long i = Math.abs((long) n); i > 0; i = i >> 1, x *= x) {
            if ((i & 1) == 1) {
                ans *= x;
                System.out.println(x);
            }
        }
        return n > 0 ? ans : 1 / ans;
    }

    public static void main(String args[]) {
        double left = 2;
        int right = 11;
        double v = new Pow().myPow(left, right);
        System.out.println(v);
        System.out.println(Math.pow(left, right) == v);
    }
}
