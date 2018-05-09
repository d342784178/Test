package 刷题.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Desc: 求所有sum为给定数字的数字组合(可重复)
 * Author: DLJ
 * Date: 2017-02-16
 * Time: 13:35
 */
public class CombinationSum {

    public static void main(String args[]) {
        System.out.println(new CombinationSum().combinationSum(new int[]{2, 3, 6, 7, }, 7));
    }

    List<List<Integer>> ans  = new ArrayList<>();
    int[]               cans = {};

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        this.cans = candidates;
        Arrays.sort(cans);
        backTracking(new ArrayList<>(), 0, target);
        return ans;
    }

    //回溯法 类似穷举(有选择性的穷举)
    public void backTracking(List<Integer> cur, int from, int target) {
        if (target == 0) {
            List<Integer> list = new ArrayList<>(cur);
            ans.add(list);
        } else {
            //i=from保证不向前找 这样可以避免重复
            for (int i = from; i < cans.length && cans[i] <= target; i++) {
                cur.add(cans[i]);
                backTracking(cur, i, target - cans[i]);
                cur.remove(new Integer(cans[i]));
            }
        }
    }


}
