package 刷题.sort;

import java.util.Arrays;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-11
 * Time: 13:16
 */
public class TestSort {

    public static void main(String args[]) {
        SortStrategy sortStrategy = new MergeSort();
        int[]        array        = new int[]{14, 12, 56, 1, 3, 81, 19, 2};
        System.out.println("原始:" + Arrays.toString(array));
        System.out.println("结果:" + Arrays.toString(sortStrategy.sort(array)));
    }
}
