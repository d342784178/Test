package leetcode.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Desc: 有面值为1元、2元和3元的硬币若干枚，如何用最少的硬币凑够11元？
 * Author: DLJ
 * Date: 2017-02-13
 * Time: 15:42
 */
public class Coin {
    //使用dp求解 1a+2b+3c=10 求解min(a+b+c)

    // i:钱数 vj:硬币面额
    //d(i)=min{ d(i-vj)+1 }
    public int getMinCoin(int num, List<Integer> array) {
        if (array == null) {
            array = new ArrayList<>();
        }
        int coinNum3 = 0;
        int coinNum2 = 0;
        int coinNum1 = 0;
        if (num >= 3) {
            ArrayList<Integer> integers = new ArrayList<>(array);
            integers.add(3);
            coinNum3 = getMinCoin(num - 3, integers);
        }
        if (num >= 2) {
            ArrayList<Integer> integers = new ArrayList<>(array);
            integers.add(2);
            coinNum2 = getMinCoin(num - 2, integers);
        }
        if (num >= 1) {
            ArrayList<Integer> integers = new ArrayList<>(array);
            integers.add(1);
            coinNum1 = getMinCoin(num - 1, integers);
        }
        if (num < 1) {
            System.out.println(array);
            return array.size();
        }
        return getMin(Arrays.asList(coinNum1, coinNum2, coinNum3));
    }

    /**
     * 求大于0 的最小值
     * @param array
     * @return
     */
    public int getMin(List<Integer> array) {
        int min = 0;
        for (int i = 0; i < array.size() - 1; i++) {
            Integer i1 = array.get(i);
            Integer i2 = array.get(i + 1);
            if (i1 > 0 && i2 > 0) {
                min = Math.min(i1, i2);
            } else if (i1 > 0) {
                min = i1;
            } else if (i2 > 0) {
                min = i2;
            }
        }
        return min;
    }

    public static void main(String args[]) {
        System.out.println(new Coin().getMinCoin(13, null));
    }
}
