package 刷题.logic.兔子爬楼梯;

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

//    d(1)=d(0)+x*steps[0]?
//    d(2)=d(1)+x*steps[1]?
//    d(3)=d(2)+x*steps[2]?
//
//    d(i)=d(i-1)+x*steps[i-1]?   total-d(i-1)>=x*steps[i-1]
//
//    d(i)=j i:用i种跳法 j:能爬到的层数
//
//    d(1)表明 用 1 1种跳法 能爬到的层数
//    d(2)表示 用1 2 2种跳法 能爬到的层数

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
        System.out.println(new Rabbit2().getNum(16, new int[]{4, 3, 2, 1}));
    }
}
