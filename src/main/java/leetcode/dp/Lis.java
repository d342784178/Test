package leetcode.dp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desc: dp求最长非降子序列
 * Author: DLJ
 * Date: 2017-02-13
 * Time: 16:54
 */
public class Lis {
    //思路: map中存放所有子序列 key为下标
    //遍历数组 再遍历map中的子序列 如果能构成非降序列 则添加到map中存放的序列中

    private Map<Integer, List<Integer>> map = new HashMap<>();

    //i,j表示数组长度
    //d(i) = max{1, d(j)+1},其中j<i,array[j]<=array[i]
    private List<Integer> getLonggest(int[] array) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            arrayList.add(array[i]);
        }
        sdf(arrayList, 0);
        List<Integer> longgest = null;
        for (Map.Entry<Integer, List<Integer>> integerListEntry : map.entrySet()) {
            List<Integer> value = integerListEntry.getValue();
            System.out.println(value);
            if (longgest == null) {
                longgest = value;
            } else {
                if (value.size() > longgest.size()) {
                    longgest = value;
                }
            }
        }
        return longgest;
    }

    private void sdf(List<Integer> array, int index) {
        if (index > array.size() - 1) {
            return;
        }
        Integer integer = array.get(index);
        map.put(index, new ArrayList<>());
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            List<Integer> value = entry.getValue();
            if (value.size() == 0 || integer > value.get(value.size() - 1)) {
                value.add(integer);
            }
        }
        sdf(array, index + 1);

    }

    public static void main(String args[]) {
        System.out.println(new Lis().getLonggest(new int[]{5, 3, 4, 8, 6, 7}));
    }
}
