package 刷题.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Desc: 求所有sum为给定数字的数字组合(不可重复)
 * Author: DLJ
 * Date: 2017-02-16
 * Time: 17:58
 */
public class CombinationSumII {
    public static void main(String args[]) {
        System.out.println(new CombinationSumII().combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));
    }

    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < candidates.length; i++) {
            arrayList.add(candidates[i]);
        }
        backTracking(arrayList, new ArrayList<>(), 0, target);
        return ans;
    }

    //回溯法 类似穷举(有选择性的穷举)
    public void backTracking(List<Integer> nums, List<Integer> cur, int from, int target) {
        if (target == 0) {
            List<Integer> list = new ArrayList<>(cur);
            ans.add(list);
        } else {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = from; i < nums.size() && nums.get(i) <= target; i++) {
                Integer e = nums.get(i);
                //去重 原理:防止重复元素 进入下层递归
                if (arrayList.contains(e)) {
                    continue;
                } else {
                    arrayList.add(e);
                }
                ArrayList<Integer> newCur = new ArrayList<>(cur);
                newCur.add(e);
                ArrayList<Integer> newNums = new ArrayList<>(nums);
                newNums.remove(i);
                backTracking(newNums, newCur, i, target - e);
            }
        }
    }
}
