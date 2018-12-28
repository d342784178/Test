package 刷题.数据结构;

import org.junit.Assert;

/**
 * Desc: 两个栈实现队列
 * 1. in/out两个栈
 * 2. in入栈 out出栈
 * 3. out全部出栈后 再把in栈内的导过来
 * 缺点: out一直不出栈的话, in越来越长
 * Author: ljdong2
 * Date: 2018-03-28
 * Time: 20:08
 * https://img-blog.csdn.net/20150902105019731
 */
public class QueueByStack implements StackI<Integer> {
    private Stack<Integer> in  = new Stack<>();
    private Stack<Integer> out = new Stack<>();

    @Override
    public Integer pop() {
        if (out.isEmpty()) {//out是否已全部出栈
            int size = in.size();
            if (size == 1) {
                return in.pop();
            } else {
                Integer t = null;
                //in全部出栈 out入栈
                while ((t = in.peek()) != null && in.size() > 1) {
                    in.pop();
                    out.push(t);
                }
                return in.pop();
            }
        } else {
            return out.pop();
        }
    }

    @Override
    public void push(Integer integer) {
        in.push(integer);
    }

    @Override
    public boolean isEmpty() {
        return in.isEmpty()&&out.isEmpty();
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
        QueueByStack stack = new QueueByStack();
        stack.push(1);
        Assert.assertTrue(stack.pop() == 1);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        Assert.assertTrue(stack.pop() == 1);
        stack.push(4);
        Assert.assertTrue(stack.pop() == 2);
        Assert.assertTrue(stack.pop() == 3);
        Assert.assertTrue(stack.pop() == 4);
        Assert.assertTrue(stack.isEmpty() == true);
    }
}
