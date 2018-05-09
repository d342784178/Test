package 刷题.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 找到的最长回型子串 如:abcba aba abcddcba
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 11:39
 */
public class LongestPalindromicSubstring2 {
    public String longestPalindrome(String s) {
        if (s == null || s.equals("")) {
            return "";
        }
        //遍历
        List<String> lists = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            lists.add(find(s, i));
        }

        String ss = "";
        for (String list : lists) {
            if (ss.length() < list.length()) {
                ss = list;
            }
        }
        return ss;
    }

    //使用dp求解
    //从中间想两边检测是否为回型
    public String find(String s, int index) {
        if (index + 1 >= s.length()) {
            return String.valueOf(s.charAt(index));
        }
        char c1 = s.charAt(index + 1);
        char c  = s.charAt(index);
        //判断是否可能为偶数型回型自趺床
        if (c != c1) {
            return leftFind(s, index - 1, index + 1);
        } else {
            //可能即是偶数也是奇数
            String s1 = leftFind(s, index - 1, index + 1);
            String s2 = leftFind(s, index - 1, index + 2);
            return s2.length() > s1.length() ? s2 : s1;
        }
    }

    public String leftFind(String s, int left, int right) {
        if (left < 0 || right > s.length() - 1) {
            String substring = s.substring(left + 1, right);
            return substring;
        }

        char leftChar  = s.charAt(left);
        char rightChar = s.charAt(right);
        if (leftChar == rightChar) {
            return leftFind(s, left - 1, right + 1);
        } else {
            return s.substring(left + 1, right);
        }
    }


    public static void main(String args[]) {
        System.out.println(new LongestPalindromicSubstring2().longestPalindrome
                ("abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababa"));
    }
}
