package logic.兔子爬楼梯;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc: 兔子爬楼梯问题
 * 兔子一下可以跳1格,2格,3格 请问10格楼梯有几种跳法
 * Author: DLJ
 * Date: 2017-02-11
 * Time: 15:05
 */
public class Rabbit2 {

    //    使用dp方式求解
    private List<List<Integer>> all = new ArrayList<>();

    private int getNum(int target, int[] steps) {
        getNum(target, steps, 0, new ArrayList<>());
        return all.size();
    }

    private void getNum(int target, int[] steps, int index, List<Integer> stepLists) {
        int step = steps[index];
        if (index == steps.length - 1) {
            if (target % step == 0) {
                stepLists.add(target / step);
                all.add(stepLists);
                System.out.println(stepLists);
            }
            return;
        }

        int t = target / step;
        int i = 0;
        while (i <= t) {
            ArrayList<Integer> objects = new ArrayList<>(stepLists);
            objects.add(i);
            getNum(target - i * step, steps, index + 1, objects);
            i++;
        }
    }

    public static void main(String args[]) {
        System.out.println(new Rabbit2().getNum(11, new int[]{3, 2, 1}));
    }
}
