package sort;

/**
 * Desc: 快速排序法
 * Author: DLJ
 * Date: 2017-02-11
 * Time: 13:17
 */
public class QuickSort implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        return sort(array, 0, array.length - 1);
    }

    private int[] sort(int[] array, int start, int end) {
        //如果递归的时候只有2个元素 那肯定是已经排好序的 直接跳过
        if (start + 1 >= end && array.length != 2) {
            return array;
        }
        //阀
        Integer key = array[start];
        //低位
        int low = start;
        //高位
        int   high      = end;
        int   length    = end - start + 1;
        int[] tempArray = new int[length];
        System.arraycopy(array, 0, tempArray, 0, length);
        for (int i = start; i < length; i++) {
            Integer t = tempArray[i];
            if (t > key) {
                //大于 高位-1
                array[high--] = t;
            } else if (t < key) {
                //小于 低位+1
                array[low++] = t;
            }
            array[low] = key;
        }
        sort(array, start, low - 1);
        sort(array, high + 1, end);
        return array;
    }
}
