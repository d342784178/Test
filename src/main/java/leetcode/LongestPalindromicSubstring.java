package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * //TODO 从中间开始找
 * Desc: 找到的最长回型子串 如:abcba aba abcddcba
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 11:39
 */
public class LongestPalindromicSubstring {
    //效率低
    //先获取所有首尾相同字符的子序列 再判断是不是回型的
    public String longestPalindrome(String s) {
        if (s == null || s.equals("")) {
            return "";
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j <= i; j++) {
                if (s.charAt(i) == s.charAt(j) && j >= 0 && j <= i) {
                    String substring = s.substring(j, i + 1);
                    if (judge(substring)) {
                        list.add(substring);
                    }
                }
            }
        }
        String ss = "";
        for (String s1 : list) {
            if (judge(s1) && ss.length() < s1.length()) {
                ss = s1;
            }
        }
        return ss;
    }

    public boolean judge(String s) {
        boolean ok = true;
        for (int i = 0; i < s.length() / 2; i++) {
            if (s.charAt(i) != s.charAt(s.length() - i - 1)) {
                ok = false;
                break;
            }
        }
        return ok;
    }

    public static void main(String args[]) {
        System.out.println(new LongestPalindromicSubstring().longestPalindrome
                ("abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababa"));
    }
}
