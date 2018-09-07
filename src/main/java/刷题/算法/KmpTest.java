package 刷题.算法;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Desc: kmp算法
 * Author: ljdong2
 * Date: 2018-09-06
 * Time: 17:28
 */
public class KmpTest {


    @Test
    public void testKmp() throws Exception {
        Assert.assertEquals(true, kmp("BBC ABCDAB ABCDABCDABDE", "ABCDABD"));
    }

    @Test
    public void testGetNextArray() throws Exception {
        Assert.assertTrue(Arrays.equals(new int[]{0, 0, 1, 2, 3, 4, 5, 0}, getNextArray("abababac")));
        Assert.assertTrue(Arrays.equals(new int[]{0, 0, 0, 0, 1, 2, 0}, getNextArray("ABCDABD")));
    }

    /**
     * @link https://www.bilibili.com/video/av3246487/?from=search&seid=17173603269940723925
     * @link http://www.ruanyifeng.com/blog/2013/05/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm.html
     */
    public boolean kmp(String src, String pattern) {
        int[] next = getNextArray(pattern);
        //j指向pattern
        int j = 0;
        //i指向src 一直往前走
        for (int i = 0; i < src.length() && j < pattern.length(); ) {
            char l = src.charAt(i);
            char r = pattern.charAt(j);
            if (l != r) {//不相等
                if (j > 0) {//j>0 回溯到next[j-1]. next[j-1] 表示j-1位于src前缀相同个数 所以这里不用+1
                    j = next[j - 1];
                } else {//=0 无法回溯
                    i++;
                }
            } else {//相等 指针向前走
                j++;
                i++;
            }
        }
        return j == pattern.length();
    }

    /**
     * 从0开始 网上有些示例从-1开始 没有什么影响
     * @link https://blog.csdn.net/buppt/article/details/78531384
     */
    public int[] getNextArray(String pattern) {
        // next[i]: 表示pattern[i]与pattern前缀有next[i]+1个连续相同字符
        // 注: 为何是next[i]+1而不是next[i],是为了方便计算
        // 如: ababa next[0]=-1,next[1]=-1,next[2]=0,next[3]=1,next[4]=2
        int next[] = new int[pattern.length()];
        int length = pattern.length();
        //j指针
        int j = 0;
        //i指针 一直往前走
        for (int i = 1; i < length; ) {
            char ii = pattern.charAt(i);
            char jj = pattern.charAt(j);
            if (ii == jj) {//i,j指向值相等 则next[i]=next[i-1] + 1
                next[i] = next[i - 1] + 1;
                //j,i指针都+1
                i++;
                j++;
            } else {//i,j指向值不等
                if (j > 1 && next[j - 1] > 0) {//如果当前j>1,且next[j-1]>=0 (表明与pattern前缀有相同部分)
                    // j指向next[j-1]+1
                    // 因为当next[j-1]>=0时 表明j指针前面部分字符串与pattern前缀有 next[j+1]+1个相同
                    // 所以将pattern[next[j-1]+1] 与pattern[i]比较是否相同
                    j = next[j - 1];
                } else {//否则表示与pattern前缀没有相同部分
                    next[i] = next[j];//=-1
                    i++;
                }
            }
        }
        return next;
    }
}
