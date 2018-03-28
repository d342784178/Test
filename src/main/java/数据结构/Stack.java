package 数据结构;

import org.junit.Assert;

/**
 * Desc: 栈:链表实现
 * Author: ljdong2
 * Date: 2018-03-28
 * Time: 19:44
 */
public class Stack<T> implements StackI<T> {
    /**
     * 栈顶指针
     */
    private Node<T> topNode;

    private int size;

    @Override
    public T pop() {
        Node<T> node = topNode;
        topNode = topNode.next;
        size -= 1;
        return node.t;
    }

    @Override
    public void push(T t) {
        if (topNode == null) {
            topNode = new Node<>(t);
        } else {
            Node<T> tNode = new Node<>(t);
            tNode.next = topNode;
            topNode = tNode;
        }
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return topNode == null;
    }

    @Override
    public T peek() {
        return topNode != null ? topNode.t : null;
    }

    @Override
    public int size() {
        return size;
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
