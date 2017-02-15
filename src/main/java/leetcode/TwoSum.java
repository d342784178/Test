package leetcode;

import java.util.Arrays;

public class TwoSum {
    public static void main(String args[]) {
        System.out.println(Arrays.toString(new TwoSum().twoSum(new int[]{2, 7, 11, 15}, 9)));
    }

    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length - 1; i++) {
            int num = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                int num1 = nums[j];
                if (num + num1 == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0};
    }
}