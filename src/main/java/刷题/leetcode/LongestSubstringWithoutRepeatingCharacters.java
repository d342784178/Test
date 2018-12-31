package 刷题.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * Desc: 找到字符串中连续的 不重复的最长子串
 * https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/solution/
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 10:07
 */
public class LongestSubstringWithoutRepeatingCharacters {

    /**
     * 暴力法:
     * 1. 找出所有子串
     * 2. 找到无重复的所有子串
     * 3. 得到最大长度
     * 缺点:
     * 1. 子串间存在包含关系,如果从索引 i 到 j-1 之间已经被检查为没有重复字符。
     * 我们只需要检查s[j] 是否存在s[i,j-1]即可,可以利用hashmap降低时间复杂度
     * 2. 只需要返回最大长度即可,直接进行数值比较即可
     * <pr>
     * 滑动窗口法:
     * 1. i,j两个指针从下标0开始移动
     * 2. 若s[j]不存在s[i,j-1]中,则添加到list中,j++.
     * 否则remove(charAt(i)),i++
     * 注意: 假设s[i,j-1]=abf s[j]=b 则先remove(charAt(i)=a),再i++,remove(charAt(i)=b)
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        }
        //长度
        int            n   = s.length();
        //理由hash表查找o(1)的特性
        Set<Character> set = new HashSet<>();
        //两个指针
        int i         = 0, j = 0;
        int maxLength = 1;
        while (i < n & j < n) {
            // 取每个字符
            char c = s.charAt(j);
            if (!set.contains(c)) {
                set.add(c);
                j++;
            } else {
                set.remove(s.charAt(i++));
            }
            //比较已知最大长度 和现有长度
            maxLength = Math.max(maxLength, j - i);
            //System.out.println(set);
        }
        return maxLength;
    }

    public static void main(String args[]) {
        System.out.println(new LongestSubstringWithoutRepeatingCharacters().lengthOfLongestSubstring
                ("pwwkew"));
    }
}
