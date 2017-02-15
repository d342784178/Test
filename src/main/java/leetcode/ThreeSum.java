package leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Desc: 找出数组中 长度为3 和为0的所有子序列
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 16:25
 */
public class ThreeSum {
    //TODO 去重 优化
    private List<List<Integer>> all = new ArrayList<>();

    public List<List<Integer>> threeSum(int[] nums) {
        ArrayList<Integer> numsList = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            numsList.add(nums[i]);
        }
        Collections.sort(numsList);
        getSolution(numsList, 3, 0, null);

        return all;
    }


    /**
     * @param nums 数组
     * @param n    n个数和
     * @param sum  目标值
     * @param has  传null
     */
    public void getSolution(List<Integer> nums, int n, int sum, List<Integer> has) {
        if (n == 1) {
            if (nums.contains(sum)) {
                has.add(sum);
                all.add(has);
                System.out.println(has);
            }
            return;
        }
        for (int i = 0; i < nums.size(); i++) {
            Integer            integer   = nums.get(i);
            ArrayList<Integer> arrayList = new ArrayList<>(nums);
            arrayList.remove(integer);
            ArrayList<Integer> newHas = null;
            if (has != null) {
                newHas = new ArrayList<>(has);
            } else {
                newHas = new ArrayList<>();
            }
            newHas.add(integer);
            getSolution(arrayList.subList(i, nums.size() - 1), n - 1, sum - integer, newHas);
        }
    }

    public static void main(String args[]) {
        System.out.println(new ThreeSum().threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
    }
}
