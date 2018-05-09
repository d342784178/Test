package 刷题.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Desc: 找出数组中 长度为3 和为0的所有子序列
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 16:25
 */
public class ThreeSum {
    private List<List<Integer>> all = new ArrayList<>();

    public List<List<Integer>> threeSum(int[] num) {
        if (num == null || num.length < 3) {
            return all;
        }

        Arrays.sort(num);

        int len = num.length;
        for (int i = 0; i < len - 2; i++) {
            if (i > 0 && num[i] == num[i - 1]) {
                continue;
            }
            //转换为2sum问题 再用双指针法
            find(num, i + 1, len - 1, num[i]); //寻找两个数与num[i]的和为0
        }

        return all;
    }

    //双指针法
    public void find(int[] num, int begin, int end, int target) {
        int l = begin, r = end;
        while (l < r) {
            if (num[l] + num[r] + target == 0) {
                List<Integer> ans = new ArrayList<Integer>();
                ans.add(target);
                ans.add(num[l]);
                ans.add(num[r]);
                all.add(ans); //放入结果集中

                //去重
                while (l < r && num[l] == num[l + 1]) {
                    l++;
                }
                while (l < r && num[r] == num[r - 1]) {
                    r--;
                }
                l++;
                r--;
            } else if (num[l] + num[r] + target < 0) {
                l++;
            } else {
                r--;
            }
        }
    }

    public static void main(String args[]) {
        System.out.println(new ThreeSum().threeSum(new int[]{9, 14, 0, -8, 10, 0, 2, 9, -8, 13, -3, 1, 10, -13, 4, 3,
                -3, -11, 8, -13, -4, -6, 5, -10, -14, 0, 3, -9, -9, -7, -11, 8, -8, -4, -15, 9, 11, 3, 3, -11, -7, 7,
                5, -12, 1, -14, -1, 13, -9, -8, 7, 2, -6, -11, -1, -5, -4, -13, -7, 2, -13, -2, -5, -6, 9, -12, 10,
                -2, -2, -10, 2, 6, 4, 14, 2, -10, -15, -14, 10, -9, -15, -6, 0, -6, -2, 14, -3, 9, 8, -3, -12, 10, 2,
                -9, 11, -3, -6, -2, 10, 7, 3, -11, -10, -8, -12, -1}));
//        new ThreeSum().twoSum2(Arrays.asList(2, -1, 1), -2);
    }
}
