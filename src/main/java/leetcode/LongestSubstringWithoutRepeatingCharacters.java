package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 找到字符串中连续的 不重复的最长子串
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 10:07
 */
public class LongestSubstringWithoutRepeatingCharacters {

    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        int             n   = s.length();
        List<Character> set = new ArrayList<>();
        int             i   = 0, j = 0, maxLength = 1;
        while (i < n & j < n) {
            char c = s.charAt(j);
            if (!set.contains(c)) {
                set.add(c);
                j++;
            } else {
                set.remove(0);
                i++;
            }
            maxLength = Math.max(maxLength, j - i);
        }
        return maxLength;
    }

    public static void main(String args[]) {
        System.out.println(new LongestSubstringWithoutRepeatingCharacters().lengthOfLongestSubstring
                ("au"));
    }
}
