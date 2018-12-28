package 刷题.数据结构;

import org.junit.Assert;

/**
 * Desc: 两个队列实现栈
 * 1. in/out队列
 * 2. in入队 out出队
 * 3. 当out>1时 出队加入in 直到out=1出队
 * 4. 当in>1时 出队加入out 知道in=1出队 in/out切换
 * Author: ljdong2
 * Date: 2018-03-28
 * Time: 20:08
 * https://img-blog.csdn.net/20150902105044136
 */
public class StackByQueue implements StackI<Integer> {
    private Queue<Integer> in  = new Queue<>();
    private Queue<Integer> out = new Queue<>();

    @Override
    public Integer pop() {
        if (out.size() >= 1) {//out长度>1
            Integer t = null;
            //入队in
            while (out.peek() != null && out.size() > 1) {
                t = out.pop();
                in.push(t);
            }
            //out==1时 出队
            return out.pop();
        } else if (in.size() >= 1) {//out==0 in>=1
            Integer t = null;
            //入队out
            while (in.peek() != null && in.size() > 1) {
                t = in.pop();
                out.push(t);
            }
            //in==1时出队
            Integer        pop  = in.pop();
            Queue<Integer> temp = out;
            //in/out互换!! 使得此时如果有push进来的 直接到out 因为此时out==1 所以可以直接pop出去 提高效率
            out = in;
            in = temp;
            return pop;
        } else {
            return null;
        }
    }

    @Override
    public void push(Integer integer) {
        //push进out
        out.push(integer);
    }

    @Override
    public boolean isEmpty() {
        return in.isEmpty() && out.isEmpty();
    }

    @Override
    public Integer peek() {
        Integer pop = pop();
        push(pop);
        return pop;
    }

    @Override
    public int size() {
        return in.size() + out.size();
    }

    public static void main(String args[]) {
        StackByQueue stack = new StackByQueue();
        stack.push(1);
        Assert.assertTrue(stack.pop() == 1);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        Assert.assertTrue(stack.pop() == 3);
        stack.push(4);
        stack.push(5);
        Assert.assertTrue(stack.pop() == 5);
        stack.push(6);
        Assert.assertTrue(stack.pop() == 6);
        Assert.assertTrue(stack.pop() == 4);
        Assert.assertTrue(stack.pop() == 2);
        Assert.assertTrue(stack.pop() == 1);
        Assert.assertTrue(stack.isEmpty() == true);
    }
}
