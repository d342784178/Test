package 刷题.leetcode;

import java.util.*;

public class Subsequence2 {
    public int numberOfArithmeticSlices(int[] A) {
        //key:差值 value:计数器
        Map<Long, Integer>[] pairCounts = new Map[A.length];
        for (int i = 0; i < A.length; i++) {
            pairCounts[i] = new HashMap<>();
        }
        Map<Long, Integer>[] seqCounts = new Map[A.length];
        for (int i = 0; i < A.length; i++) {
            seqCounts[i] = new HashMap<>();
        }
        int numbers = 0;
        //保存2个数的差值
        for (int i = 0; i < A.length - 2; i++) {
            for (int j = i + 1; j < A.length - 1; j++) {
                long    diff  = (long) A[j] - A[i];
                Integer count = pairCounts[j].getOrDefault(diff, 0);
                pairCounts[j].put(diff, ++count);
            }
        }

        for (int i = 1; i < A.length - 1; i++) {
            for (int j = i + 1; j < A.length; j++) {
                long    diff    = (long) A[j] - A[i];
                Integer pCountI = pairCounts[i].getOrDefault(diff, 0);
                Integer sCountI = seqCounts[i].getOrDefault(diff, 0);
                Integer sCountJ = seqCounts[j].getOrDefault(diff, 0);
                if (pCountI + sCountI + sCountJ > 0) {
                    seqCounts[j].put(diff, pCountI + sCountI + sCountJ);
                    numbers += pCountI + sCountI;
                }
            }
        }
        return numbers;
    }



    public static void main(String args[]) {

    }
}