package 刷题.leetcode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                //i==8时 001&111 010&111 001&111
                //i==7时 001&110 010&110 100&110
                if ((1 << j & i) != 0) {
                    aa.add(ints[j]);
                }
            }
            objects.add(aa);
        }
        return objects;
    }


    //=========回溯法
    public List<List<Integer>> subsets2(int[] nums) {
        ArrayList<Integer> objects = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            objects.add(nums[i]);
        }
        subsets(objects, new ArrayList<>());
        return all;
    }

    List<List<Integer>> all = new ArrayList<>();

    public void subsets(List<Integer> nums, List<Integer> has) {
        if (nums.size() == 0) {
            all.add(has);
            return;
        }
        Integer integer = nums.get(0);
        nums.remove(integer);

        ArrayList<Integer> has1 = new ArrayList<>(has);
        ArrayList<Integer> has2 = new ArrayList<>(has);
        has2.add(integer);
        subsets(new ArrayList<>(nums), has2);
        subsets(new ArrayList<>(nums), has1);
    }
}