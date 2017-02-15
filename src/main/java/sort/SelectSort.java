package sort;

/**
 * Desc: 选择排序
 * Author: DLJ
 * Date: 2017-02-11
 * Time: 14:35
 */
public class SelectSort implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int minIndex = getMinIndex(array, i);
            int min      = array[minIndex];
            array[minIndex] = array[i];
            array[i] = min;
        }
        return array;
    }

    private int getMinIndex(int[] array, int start) {
        int minIndex = start;
        for (int i = start; i < array.length - 1; i++) {
            int left  = array[i];
            int right = array[i + 1];
            minIndex = left < right ? i : i + 1;
        }
        return minIndex;
    }
}
