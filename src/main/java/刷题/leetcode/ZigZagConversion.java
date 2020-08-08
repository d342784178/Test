package 刷题.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: Z 字形变换
 * https://leetcode-cn.com/problems/zigzag-conversion/
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 14:44
 */
public class ZigZagConversion {
    static class Solution {
        public String convert(String s, int numRows) {
            if(numRows==1){
                return s;
            }
            //被取余数
            int                   ss   = (numRows - 1) * 2;
            List<List<Character>> list = new ArrayList<>();
            for (int i = 0; i < numRows; i++) {
                list.add(new ArrayList<>());
            }
            for (int i = 0; i < s.length(); i++) {
                char c  = s.charAt(i);
                int  j  = i % ss;
                int  jj = j % numRows;
                if (j >= numRows) {
                    int i1 = numRows - 1 - (jj + 1);
                    list.get(i1).add(c);
                } else {
                    list.get(jj).add(c);
                }

            }

            System.out.println(list);
            StringBuffer sb = new StringBuffer();
            for (List<Character> characters : list) {
                for (Character character : characters) {
                    sb.append(character);
                }
            }
            return sb.toString();
        }
    }

    public static void main(String args[]) {
        new Solution().convert("LEETCODEISHIRING"
                , 3);
    }
}
