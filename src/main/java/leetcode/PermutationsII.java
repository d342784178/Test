package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-17
 * Time: 09:05
 */
public class PermutationsII {
    List<List<Integer>> all = new ArrayList<>();

    public List<List<Integer>> permuteUnique(int[] nums) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            arrayList.add(nums[i]);
        }
        backTracking(arrayList, new ArrayList<>());
        return all;
    }

    //回溯法 类似穷举(有选择性的穷举)
    public void backTracking(List<Integer> nums, List<Integer> cur) {
        if (nums.size() == 0) {
            List<Integer> list = new ArrayList<>(cur);
            all.add(list);
        } else {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0; i < nums.size(); i++) {
                Integer e = nums.get(i);
                //去重 原理:防止重复元素 进入下层递归
                if (arrayList.contains(e)) {
                    continue;
                } else {
                    arrayList.add(e);
                }
                ArrayList<Integer> newCur = new ArrayList<>(cur);
                newCur.add(e);
                ArrayList<Integer> newCans = new ArrayList<>(nums);
                newCans.remove(i);
                backTracking(newCans, newCur);
            }
        }
    }

    public static void main(String args[]) {
        System.out.println(new PermutationsII().permuteUnique(new int[]{1, 1, 2}));
    }
}
