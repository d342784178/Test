package leetcode;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Desc: 去数组的有序组合
 * Author: DLJ
 * Date: 2017/2/13
 */
public class Subsets {

    public static void main(String args[]) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ssSSS");
        System.out.println(sdf.format(new Date()));
//        1, 2, 3, 4, 5, 6, 7, 8, 10, 0
        Subsets subsets = new Subsets();
        System.out.println(subsets.subsets(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 0}).size());
        System.out.println(sdf.format(new Date()));
    }

    //        算法说明：当n大于2时，n个数的全组合一共有(2^n)-1种。
    //        当对n个元素进行全组合的时候，可以用一个n位的二进制数表示取法。
    //        1表示在该位取，0表示不取。例如，对ABC三个元素进行全组合，  100表示取A，010表示取B，001表示取C，101表示取AC  110表示取AB，011表示取BC，111表示取ABC
    //                注意到表示取法的二进制数其实就是从1到7的十进制数
    //        推广到对n个元素进行全排列，取法就是从1到2^n-1的所有二进制形式
    public List<List<Integer>> subsets(int[] ints) {
        int                 nCnt    = ints.length;//3
        int                 nBit    = 1 << nCnt;//8
        List<List<Integer>> objects = new ArrayList<>();
        for (int i = 1; i <= nBit; i++) {//1~8
            ArrayList<Integer> aa = new ArrayList<>();
            for (int j = 0; j < nCnt; j++) {//通过位运算  取出二进制位对应的下标
                //i==8时
                //001&111 010&111 001&111
                if ((1 << j & i) != 0) {
                    aa.add(ints[j]);
                }
            }
            objects.add(aa);
        }
        return objects;
    }


    private Map<Integer, Map<Integer, List<Integer>>> allMap = new HashMap<>();

    private int maxValue;
    private int add;

    //思路:列出全排列(无序) 利用hashcode去重
    //效率低!! TODO 无需去重 直接获得有序的
    public List<List<Integer>> subsets2(int[] nums) {
        for (int num : nums) {
            if (maxValue < num) {
                maxValue = num;
            }
        }
        int minValue = maxValue;
        for (int num : nums) {
            if (minValue > num) {
                minValue = num;
            }
        }
        maxValue = maxValue - minValue;
        add = 0 - minValue;

        List<Integer> integers = new ArrayList<>();
        allMap.put(0, new HashMap<>(0));
        for (int i = 0; i < nums.length; i++) {
            integers.add(nums[i]);
            int n = nums.length - i;
            allMap.put(n, new HashMap<>(512));
        }
        for (int i = 0; i < nums.length / 2 + 1; i++) {
            int n = nums.length - i;
            getA(Collections.emptyList(), integers, n);
        }
        List<List<Integer>> objects1 = new ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> entry : allMap.entrySet()) {
            Map<Integer, List<Integer>> value = entry.getValue();
            for (Map.Entry<Integer, List<Integer>> listEntry : value.entrySet()) {
                objects1.add(listEntry.getValue());
            }
        }
        return objects1;
    }


    public void getA(List<Integer> has, List<Integer> nums, int n) {
        if (n <= 0) {
            Map<Integer, List<Integer>> integerListMap = allMap.get(nums.size());
            integerListMap.put(gethash(nums), nums);

            Map<Integer, List<Integer>> map = allMap.get(has.size());
            map.put(gethash(has), has);
        } else {
            for (Integer integer : nums) {
                ArrayList<Integer> integers = new ArrayList<>(nums);
                integers.remove(integer);
                ArrayList<Integer> newHas = new ArrayList<>(has);
                newHas.add(integer);
                getA(newHas, integers, n - 1);
            }
        }
    }

    public int gethash(List<Integer> array) {
        int[] ints = new int[maxValue + 1];
        for (Integer integer : array) {
            ints[integer + add] = 1;
        }
        return Arrays.hashCode(ints);
    }

}