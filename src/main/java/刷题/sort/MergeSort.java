package 刷题.sort;

import java.util.Arrays;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-08-29
 * Time: 15:54
 */
public class MergeSort implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        return sort(array, 0, array.length - 1);
    }

    private int[] sort(int[] array, int start, int end) {
        if (end == start) {
            return array;
        }
        //取中点
        int mid = (end - start) / 2 + start;
        //二分排序
        sort(array, start, mid);
        sort(array, mid + 1, end);
        //自身排序

        merge(array, start, mid, mid + 1, end);
        return array;
    }

    private void merge(int[] array, int ls, int le, int rs, int re) {
        int[] tempArray = new int[array.length];
        //将要做排序的部分 copy至tempArray
        System.arraycopy(array, 0, tempArray, 0, array.length);
        int index = ls;
        int i     = ls, j = rs;
        while (i <= le || j <= re) {
            if (i > le) {
                array[index++] = tempArray[j];
                j++;
            } else if (j > re) {
                array[index++] = tempArray[i];
                i++;
            } else {
                int l = tempArray[i];
                int r = tempArray[j];
                if (l < r) {
                    array[index++] = l;
                    i++;
                } else {
                    array[index++] = r;
                    j++;
                }
            }
        }

    }
}
