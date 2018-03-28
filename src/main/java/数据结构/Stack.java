package 数据结构;

import org.junit.Assert;

/**
 * Desc: 栈:链表实现
 * Author: ljdong2
 * Date: 2018-03-28
 * Time: 19:44
 */
public class Stack<T> {
    /**
     * 栈顶指针
     */
    private Node<T> lastNode;

    public T pop() {
        Node<T> node = lastNode;
        lastNode = lastNode.next;
        return node.t;
    }

    public void push(T t) {
        if (lastNode == null) {
            lastNode = new Node<>(t);
        } else {
            Node<T> tNode = new Node<>(t);
            tNode.next = lastNode;
            lastNode = tNode;
        }
    }

    public boolean isEmpty() {
        return lastNode == null;
    }


    public static void main(String args[]) {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        Assert.assertTrue(stack.pop() == 1);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        Assert.assertTrue(stack.pop() == 3);
        Assert.assertTrue(stack.pop() == 2);
        Assert.assertTrue(stack.pop() == 1);
        Assert.assertTrue(stack.isEmpty() == true);
    }
}
