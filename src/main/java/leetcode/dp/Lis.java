package leetcode.dp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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

    private static Map<Integer, List<Integer>> map = Maps.newHashMap();

    //i,j表示数组长度
    //d(i) = max{1, d(j)+1},其中j<i,array[j]<=array[i]
    private List<Integer> getLonggest(int[] array, List<Integer> lists) {
        if (lists == null) {
            lists = Lists.newArrayList();
        }
        List<Integer> tempList = Lists.newArrayList();
        tempList.addAll(lists);
        int[] ints = new int[array.length - 1];
        System.arraycopy(array, 0, ints, 0, array.length - 1);
        if (lists.isEmpty() || array[array.length - 1] < lists.get(0)) {
            tempList.add(0, array[array.length - 1]);
        } else {
            tempList.clear();
            tempList.add(0, array[array.length - 1]);
        }

        if (tempList.size() != lists.size() + 1) {
            map.put(lists.size(), lists);
        }
        if (array.length == 1) {
            return lists;
        } else {
            return getLonggest(ints, tempList);
        }
    }


    public static void main(String args[]) {
        new Lis().getLonggest(new int[]{5, 3, 4, 8, 6, 7}, null);
        System.out.println(map);
    }
}
