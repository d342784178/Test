package leetcode;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-17
 * Time: 17:41
 */
public class JumpGame {
    //    Given an array of non-negative integers, you are initially positioned at the first index of the array.
//
//    Each element in the array represents your maximum jump length at that position.
//
//            Determine if you are able to reach the last index.
//
//    For example:
//    nums = [2,3,1,1,4], return true.
//
//    nums = [3,2,1,0,4], return false.
    public boolean canJump(int[] nums) {
        if (nums.length == 0) {
            return false;
        }
        int step = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (step > 0) {
                step--;
                //最精妙的地方
                //通过记录step表示当前节点 还能往前走几步
                step = Math.max(step, nums[i]);
                if (i + step == nums.length - 1) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return true;
    }


    public static void main(String args[]) {
        System.out.println(new JumpGame().canJump(new int[]{3,2,1,0,4}));
    }
}
