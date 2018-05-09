package 刷题.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-17
 * Time: 13:47
 */
public class MultiplyStrings {
//    Given two non-negative integers num1 and num2 represented as strings, return the product of num1 and num2.
//
//    Note:
//
//    The length of both num1 and num2 is < 110.
//    Both num1 and num2 contains only digits 0-9.
//    Both num1 and num2 does not contain any leading zero.
//    You must not use any built-in BigInteger library or convert the inputs to integer directly.
    //求积

    public String multiply(String num1, String num2) {
        char[]             num1Chars = num1.toCharArray();
        char[]             num2Chars = num2.toCharArray();
        ArrayList<Integer> objects   = new ArrayList<>();
        char[]             temp;
        if (num1Chars.length > num2Chars.length) {
            temp = num2Chars;
            num2Chars = num1Chars;
            num1Chars = temp;
        }

        for (int i = num2Chars.length - 1; i >= 0; i--) {
            int a = num2Chars[i] - '0';
            for (int j = num1Chars.length - 1; j >= 0; j--) {
                int b     = num1Chars[j] - '0';
                int multi = a * b;

                int index = (num1Chars.length - 1 - j) + (num2Chars.length - 1 - i);
                setListIndex(objects, index, multi % 10);
                if (multi >= 10) {
                    setListIndex(objects, index + 1, multi / 10);
                }
            }
        }
        boolean      isZero = true;
        StringBuffer sb     = new StringBuffer("");
        for (int i = objects.size() - 1; i >= 0; i--) {
            sb.append(objects.get(i));
            if (objects.get(i) != 0) {
                isZero = false;
            }
        }
        if (sb.length() == 0 || isZero) {
            return "0";
        } else {
            return sb.toString();
        }
    }

    //判断index位置时候有值 有值则叠加 (如果>10 则存到index+1上)
    private void setListIndex(List<Integer> array, int index, int target) {
        if (index + 1 <= array.size()) {
            int element = array.get(index) + target;
            array.set(index, element % 10);
            if (element >= 10) {
                setListIndex(array, index + 1, element / 10);
            }
        } else {
            array.add(index, target);
        }
    }

    public static void main(String args[]) {
        System.out.println(new MultiplyStrings().multiply("1123124124213123", "21341523"));
    }

}
