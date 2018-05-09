package 刷题.leetcode.dp;

/**
 * Desc: 平面上有N*M个格子，每个格子中放着一定数量的苹果。你从左上角的格子开始， 每一步只能向下走或是向右走，每次走到一个格子上就把格子里的苹果收集起来， 这样下去，你最多能收集到多少个苹果。
 * Author: DLJ
 * Date: 2017-02-13
 * Time: 17:23
 */
public class Apple {

    public int getMaxSum(int[][] gezi, int x, int y) {
        int i   = 0;
        int i1  = 0;
        int sum = 0;
        if (x + 1 < gezi[0].length) {
            i = getMaxSum(gezi, x + 1, y);
        }
        if (y + 1 < gezi.length) {
            i1 = getMaxSum(gezi, x, y + 1);
        }

        if (x + 1 >= gezi[0].length && y + 1 >= gezi.length) {
            return gezi[x][y];
        } else {
            sum = Math.max(i, i1) + gezi[x][y];
            return sum;
        }
    }

    public static void main(String args[]) {
        int[][] ints = new int[][]{{1, 8, 3, 4}, {3, 4, 5, 6}, {5, 6, 7, 8}, {6, 7, 8, 9}};
        for (int[] anInt : ints) {
            for (int i : anInt) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        System.out.println(new Apple().getMaxSum(ints, 0, 0));
    }
}
