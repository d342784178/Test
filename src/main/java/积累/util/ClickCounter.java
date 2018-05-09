package 积累.util;

import java.util.Arrays;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-16
 * Time: 15:37
 */
public class ClickCounter {

    private int    internal = 1000 * 5;//时间
    private int    length   = 10;//点击次数
    private long[] hits     = new long[length];

    public ClickCounter(int internal, int length) {
        this.internal = internal + 1;
        this.length = length;
    }

    public boolean click() {
        long[] tempHits = new long[length];
        System.arraycopy(hits, 0, tempHits, 1, length - 1);
        hits = tempHits;
        hits[0] = System.currentTimeMillis();
        long l = hits[0] - hits[length - 1];
        System.out.println(Arrays.toString(hits) + l);
        if (l >= 0 && l < internal) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String args[]) {
        ClickCounter clickCounter = new ClickCounter(1000 * 5, 5);
        System.out.println(clickCounter.click());
        System.out.println(clickCounter.click());
        System.out.println(clickCounter.click());
        System.out.println(clickCounter.click());
        System.out.println(clickCounter.click());
        System.out.println(clickCounter.click());
    }
}
