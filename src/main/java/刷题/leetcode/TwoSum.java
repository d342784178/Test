package 刷题.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public static void main(String args[]) {
        System.out.println(Arrays.toString(new TwoSum().twoSum3(new int[]{2, 7, 11, 15}, 9)));
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

    //hash法:借助hashmap 时间复杂度为o(1)的优势
    //实现时间复杂度o(n)
    public int[] twoSum2(int[] nums, int target) {
//        第一次遍历数组先将所有元素和它的下标作为key-value对存入Hashmap中，第二次遍历数组时根据目标和与当前元素之差，在Hashmap
// 中找相应的差值。如果存在该差值，说明存在两个数之和是目标和。此时记录下当前数组元素下标并拿出Hashmap中数组元素下标即可。Hashmap获取元素的时间复杂度是O(1)，所以总的时间复杂度仍不超过O(n)。
        //        key:差值 value:下标
        Map<Integer, Integer> map = new HashMap<>();
        int[]                 res = new int[2];
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            map.put(target - num, i);
        }
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            //如果map存在差值为num的
            if (map.containsKey(num) && map.get(num) != i) {
                res[0] = map.get(num);
                res[1] = i;
                break;
            }
        }
        return res;
    }

    //双指针法 时间复杂度o(nlogn)
    private int[] twoSum3(int[] nums, int target) {
        // 首先将原数组复制一遍，对新数组进行排序。排序后将双指针指向头部与尾部元素，进行迭代。如果双指针指向元素之和大于目标和，则将尾部指针向前移一位，反之则将头部指针向后移一位，直到双指针指向元素之和等于目标和，记录这两个元素的值，然后再遍历一遍旧数组，找出这两个元素的下标。
        int   left  = 0;
        int   right = nums.length - 1;
        int[] ints  = new int[2];
        while (left < right) {
            if (nums[left] + nums[right] == target) {
                ArrayList<Integer> e = new ArrayList<>();
                ints[0] = left;
                ints[1] = right;
                break;
            } else if (nums[left] + nums[right] > target) {
                right--;
            } else {
                left++;
            }
        }
        return ints;
    }
}