package leetcode.dp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
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


    /**
     * i表示数组长度 lis表示最长非降子序列
     * d(i) = int[i-1]>=int[i-2]? (lis[i-1].get(length-1)==int[i-2])?d(i-1)+1:(考虑子序列长度相等的情况)):d(i-i)
     * @param next 用于新旧lis长度相等时 通过在newlis中加入i+1 来帮助判断
     */
    private List<Integer> getLonggest(int[] array, Integer next) {
        int i1 = array[array.length - 1];
        if (array.length - 1 > 0) {
            int[] newArray = new int[array.length - 1];
            System.arraycopy(array, 0, newArray, 0, array.length - 1);
            int i2 = newArray[newArray.length - 1];
            if (i1 >= i2) {
                List<Integer> lis = getLonggest(newArray, i1);
                if (lis.get(lis.size() - 1) == i2) {//lis[i-1].get(length-1)==int[i-2])
                    lis.add(i1);
                    return Lists.newArrayList(lis);
                } else {
                    //将i1,i2构成newlis 与lis比较长度
                    ArrayList<Integer> newLis = Lists.newArrayList(i2);
                    newLis.add(i1);
                    if (newLis.size() == lis.size()) {//长度相等时 通过next来判断新旧lis的长度
                        return next != null && next >= i1 ? newLis : lis;
                    } else {
                        return Lists.newArrayList(newLis.size() > lis.size() ? newLis : lis);
                    }
                }
            } else {
                return Lists.newArrayList(getLonggest(newArray, i1));
            }
        } else {
            return Lists.newArrayList(array[0]);
        }
    }

    public static void main(String args[]) {
        //1.返回4
        System.out.println(new Lis().getLonggest(new int[]{4}, null));
        //1.5>4 2.返回{4} 3,返回{4,5}
        System.out.println(new Lis().getLonggest(new int[]{4, 5}, null));
        //1.4<5 2.返回{4}
        System.out.println(new Lis().getLonggest(new int[]{5, 4}, null));
        //1.3<5 2.5>4 3.返回{4} 5.返回{4,5}
        System.out.println(new Lis().getLonggest(new int[]{4, 5, 3}, null));
        //1.7>6 2.6>3 3.3<5 4.5>4 5.返回{4} 6.返回{4,5} 7.{3.6}长度等于{4,5} 8.{3,6,7}长度>{4,5} 8.返回{3,6,7}
        System.out.println(new Lis().getLonggest(new int[]{4, 5, 3, 6, 7}, null));




    }
}
