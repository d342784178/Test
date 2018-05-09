package 刷题.leetcode;

import java.util.Arrays;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-15
 * Time: 23:10
 */
public class Candy {
//    有N个孩子站在一排。每个孩子被分配一个评分值。 你给这些孩子糖果满足以下要求： 每个孩子必须有至少一个糖果。 评分较高的孩子比他们的邻居获得更多的糖果。 你必须给予的最低糖果是什么?

    public int candy(int[] ratings) {
        //从数组两端开始扫描 由小到大递增 遇到key节点 取大值
        int   sum  = 0;
        int[] ints = new int[ratings.length];
        for (int i = 0; i < ratings.length; i++) {
            ints[i] = 1;
        }
        for (int i = 0; i < ratings.length - 1; i++) {
            int left  = ratings[i];
            int right = ratings[i + 1];
            if (right > left) {
                ints[i + 1] = ints[i] + 1;
            }
        }
        for (int length = ratings.length - 1; length - 1 >= 0; length--) {
            int left  = ratings[length];
            int right = ratings[length - 1];
            if (right > left) {
                if (ints[length - 1] != 1) {
                    ints[length - 1] = Math.max(ints[length] + 1, ints[length - 1]);
                } else {
                    ints[length - 1] = ints[length] + 1;
                }
            }
        }
        System.out.println(Arrays.toString(ints));
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
        }
        return sum;
    }


    private int[] ratings;
    private int[] has;

    //采用动态规划 (容易堆栈溢出)
    public int candy2(int[] ratings) {
        this.ratings = ratings;
        if (ratings.length == 0) {
            return 0;
        }
        has = new int[ratings.length];
        int sum = 0;
        for (int i = 0; i < ratings.length; i++) {
            if (has[i] == 0) {
                adsf(i, 0);
            }
        }
        for (int i = 0; i < ratings.length; i++) {
            if (has[i] == 0) {
                has[i] = 1;
            }
            sum += has[i];
        }
        System.out.println(Arrays.toString(has));
        return sum;
    }


    public void adsf(int index, int left) {
        if (!(index >= 0)) {
            return;
        }
        int current = ratings[index];
        if (index + 1 < ratings.length) {
            int right = ratings[index + 1];
            if (current > right) {
                if (index + 1 == ratings.length - 1) {
                    has[index + 1] = 1;
                } else {
                    adsf(index + 1, current);
                }
                has[index] = has[index + 1] + 1;
            } else if (current < right) {
                if (index == 0 || current < left) {
                    has[index] = 1;
                }
                has[index + 1] = has[index] + 1;
                if (index + 1 < ratings.length - 1) {
                    adsf(index + 1, current);
                }
            }
        }
    }

    public static void main(String args[]) {
        System.out.println(new Candy().candy(new int[]{1, 0, 2}));
    }
}
