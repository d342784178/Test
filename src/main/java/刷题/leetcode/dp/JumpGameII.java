package 刷题.leetcode.dp;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-17
 * Time: 16:29
 */
public class JumpGameII {
    //    Given an array of non-negative integers, you are initially positioned at the first index of the array.
//    给你一组自然数数组,你是最初定位在第一个数组下标的索引。
//    Each element in the array represents your maximum jump length at that position.
//    每个数组元素代表你在当前位置可以dump的最大值
//    Your goal is to reach the last index in the minimum number of jumps.
//    你的目标是达到最后一个下标的最小跳数。
//    For example:
//    Given array A = [2,3,1,1,4]
//
//    The minimum number of jumps to reach the last index is 2. (Jump 1 step from index 0 to 1, then 3 steps to the
// last index.)
//
//    Note:
//    You can assume that you can always reach the last index.
    public int jump(int[] nums) {
        return jumpStep(nums, 0, 0);
    }

    public int jumpStep(int[] nums, int index, int step) {
        if (index + 1 > nums.length) {
            return Integer.MAX_VALUE;
        } else if (index + 1 == nums.length) {
            return step;
        } else {
            if (nums[index] != 0) {
                return Math.min(jumpStep(nums, index + 1, step + 1), jumpStep(nums, index + nums[index], step + 1));
            } else {
                return jumpStep(nums, index + 1, step + 1);
            }
        }
    }

    public int jump2(int[] nums) {
        int maxReach = nums[0];
        int edge     = 0;
        int minstep  = 0;
        for (int i = 1; i < nums.length; i++) {
            if (i > edge) {
                minstep += 1;
                edge = maxReach;
                if (edge > nums.length - 1) {
                    return minstep;
                }
            }
            maxReach = Math.max(maxReach, nums[i] + i);
            if (maxReach == i) {
                return -1;
            }
        }
        return minstep;
    }

    public static void main(String args[]) {
        System.out.println(new JumpGameII().jump(new int[]{3,2,1}));
    }
}
