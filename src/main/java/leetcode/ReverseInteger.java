package leetcode;

/**
 * Desc: Reverse Integer
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 14:49
 */
public class ReverseInteger {
    public int reverse(int x) {
        int ans = 0;
        while (x != 0) {
            int temp = ans * 10 + x % 10;
            if (temp / 10 != ans) {
                return 0;
            }
            ans = temp;
            x /= 10;
        }
        return ans;
    }

    public static void main(String args[]) {
        System.out.println(new ReverseInteger().reverse(-123));
    }
}
