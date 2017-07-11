package sort;


/**
 * Desc: 冒泡排序
 * Author: DLJ
 * Date: 2017-02-11
 * Time: 12:55
 */
public class BubbleSort implements SortStrategy {
    @Override
    public int[] sort(int[] array) {
        int jjj = 0;
        for (int j = 0; j < array.length - jjj; ) {
            for (int i = 0; i < array.length - 1; i++) {
                int     temp;
                Integer left  = array[i];
                Integer right = array[i + 1];
                if (left > right) {
                    temp = left;
                    array[i] = right;
                    array[i + 1] = temp;
                }
            }
            jjj++;
        }
        return array;
    }
}
