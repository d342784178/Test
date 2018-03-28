package 数据结构;

import org.junit.Assert;

/**
 * Desc: 队列
 * Author: ljdong2
 * Date: 2018-03-28
 * Time: 20:01
 */
public class Queue<T> {
    /**
     * 栈顶指针
     */
    private Node<T> firstNode;
    private Node<T> lastNode;

    public T pop() {
        Node<T> node = firstNode;
        firstNode = firstNode.next;
        return node.t;
    }

    public void push(T t) {
        if (firstNode == null) {
            firstNode = new Node<>(t);
            lastNode = firstNode;
        } else {
            lastNode.next = new Node<>(t);
            lastNode = lastNode.next;
        }
    }

    public boolean isEmpty() {
        return firstNode == null;
    }

    public static void main(String args[]) {
        Queue<Integer> stack = new Queue<>();
        stack.push(1);
        Assert.assertTrue(stack.pop() == 1);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        Assert.assertTrue(stack.pop() == 1);
        Assert.assertTrue(stack.pop() == 2);
        Assert.assertTrue(stack.pop() == 3);
        Assert.assertTrue(stack.isEmpty() == true);
    }
}
