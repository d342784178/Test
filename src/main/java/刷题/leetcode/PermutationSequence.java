package 刷题.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-16
 * Time: 16:25
 */
public class PermutationSequence {
//    The set [1,2,3,…,n] contains a total of n! unique permutations.
//
//    By listing and labeling all of the permutations in order,
//    We get the following sequence (ie, for n = 3):
//
//            "123"
//            "132"
//            "213"
//            "231"
//            "312"
//            "321"
//    Given n and k, return the kth permutation sequence.
//
//    Note: Given n will be between 1 and 9 inclusive.
//    求排列中的第k个排列


    //思路: 因为有顺序 n-1=的顺序排列个数为(n-1)!
    //所以 直接跳到 k-k/(n-1)*n-1 这样 最后直接得出目标排列 而不需要穷举
    public String getPermutation(int n, int k) {
        ArrayList<Integer> ints = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            ints.add(i);
        }
        StringBuffer  sb  = new StringBuffer("");
        List<Integer> sdf = sdf(ints, new ArrayList<>(), k - 1);
        for (Integer integer : sdf) {
            sb.append(String.valueOf(integer));
        }
        return sb.toString();
    }

    public List<Integer> sdf(List<Integer> ints, List<Integer> has, int k) {
        if (ints.size() == 0) {
            return has;
        }

        int i1    = get(ints.size() - 1);
        int start = 0;
        if (ints.size() > 1) {
            start = k / i1;
        }
        Integer            integer   = ints.get(start);
        ArrayList<Integer> arrayList = new ArrayList<>(ints);
        arrayList.remove(start);
        ArrayList<Integer> newHas = new ArrayList<>(has);
        newHas.add(integer);
        return sdf(arrayList, newHas, k - start * i1);
    }

    public int get(int n) {
        if (n - 1 <= 0) {
            return 1;
        }
        return n * get(n - 1);
    }

    public static void main(String args[]) {
        long l = System.currentTimeMillis();
        System.out.println(new PermutationSequence().getPermutation(2, 2));
        System.out.println(System.currentTimeMillis() - l);
    }
}
