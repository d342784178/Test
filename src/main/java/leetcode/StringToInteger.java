package leetcode;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 15:19
 */
public class StringToInteger {
    public int myAtoi(String str) {
        if (str == null) {
            return 0;
        }
        str = str.trim();
        if (str.equals("")) {
            return 0;
        }
        boolean positive = true;
        int     length   = str.length();
        int     sum      = 0;
        //数字部分开始位置
        int startPos = -1;
        //数字部分结束部分(遇到字母就提前结束结束)
        int endPos = -1;
        for (int i = 0; i < length; i++) {
            char c1 = str.charAt(i);
            if (c1 >= '0' && c1 <= '9') {
                endPos = i;
            } else if (c1 == '+' || c1 == '-') {
                startPos = i;
                endPos = i;
            } else {
                endPos = i - 1;
                break;
            }
        }
        //判断正负
        if (startPos >= 0) {
            String substring = str.substring(0, startPos + 1);
            if (substring.length() > 1) {
                return 0;
            } else {
                for (int i = 0; i < substring.length(); i++) {
                    char c = substring.charAt(i);
                    positive = positive & (c == '+');
                }
            }
        }
        for (int i = startPos + 1; i <= endPos; i++) {
            char c1 = str.charAt(i);
            if (positive) {
                sum += Math.pow(10, endPos - i) * (c1 - '0');
            } else {
                sum += -1 * Math.pow(10, endPos - i) * (c1 - '0');
            }
        }
        return sum;
    }

    public static void main(String args[]) {
        System.out.println(new StringToInteger().myAtoi(" b11228552307"));
    }
}
