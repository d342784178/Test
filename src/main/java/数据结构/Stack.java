package 数据结构;

import lombok.Data;

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
            tNode.next=lastNode;
            lastNode=tNode;
        }
    }

    public boolean isEmpty() {
        return lastNode == null;
    }

    @Data
    public static class Node<T> {
        T       t;
        Node<T> next;

        public Node(T t) {
            this.t = t;
        }
    }

    public static void main(String args[]) {
        Stack<Integer> stack = new Stack<>();
        System.out.println(stack.isEmpty());
        stack.push(1);
        System.out.println(stack.pop());
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.isEmpty());
    }
}
