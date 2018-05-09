package 刷题.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 回溯法
 * Desc: 全排列问题
 * Author: DLJ
 * Date: 2017-02-16
 * Time: 15:21
 */
public class Combinations {
//    Given two integers n and k, return all possible combinations of k numbers out of 1 ... n.
//
//                                                                                                   For example,
//    If n = 4 and k = 2, a solution is:
//
//            [
//            [2,4],
//            [3,4],
//            [2,3],
//            [1,2],
//            [1,3],
//            [1,4],
//            ]


    private List<List<Integer>> all = new ArrayList<>();

    public List<List<Integer>> combine(int n, int k) {
        ArrayList<Integer> ints = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            ints.add(i);
        }
        asdf(ints, k, new ArrayList<>(), 0);
        return all;
    }

    public void asdf(ArrayList<Integer> ints, int k, ArrayList<Integer> has, int index) {
        if (k == 0) {
            all.add(has);
            return;
        }
        for (int i = index; i < ints.size(); i++) {
            ArrayList<Integer> arrayList = new ArrayList<>(ints);
            arrayList.remove(i);

            ArrayList<Integer> has1 = new ArrayList<>(has);
            has1.add(ints.get(i));
            asdf(ints, k - 1, has1, i + 1);
        }
    }

    public static void main(String args[]) {
        long x = System.currentTimeMillis();
        System.out.println(new Combinations().combine(20, 16));
        System.out.println(System.currentTimeMillis() - x);
    }

}
