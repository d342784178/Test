package alibaba;

/**
 * Desc: 阿里-新零售2面
 * 自定义一个链表数据结构（可以是单链表或双向链表），
 * 然后初始化一个链表数据，
 * 并对该链表实现两两翻转（是翻转整个节点，而不是仅交换节点的值），然后输出翻转之后的结果。
 * 比如构造的链表是：1->2->3->4->5，翻转之后，输出：2->1->4->3->5.
 * Author: ljdong2
 * Date: 2018-03-25
 * Time: 20:08
 */
public class Test3 {


    public static class Node {
        public int  value;
        public Node next;
        public Node pre;

        public static Node valuesOf(int[] values) {
            if (values.length <= 0) {
                return null;
            }
            Node first = new Node();
            first.value = values[0];
            Node node = first;
            for (int i = 1; i < values.length; i++) {
                Node right = new Node();
                right.value = values[i];
                node.next = right;
                right.pre = node;

                node = node.next;
            }
            return first;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static class NodeComposite extends Node {
        public Node one;

        public NodeComposite(Node one) {
            this.one = one;
            Node two = this.one.next;
            if (one.pre != null) {
                one.pre.next = this;
                this.pre = one.pre;
            }
            if (two.next != null) {
                two.next.pre = this;
                this.next = two.next;
                two.next = null;
            }
        }

        public void swap() {
            Node right = this.one.next;
            right.next = this.one;
            this.one.pre = right;
            this.one.next = null;
            this.one = right;
        }

        public static NodeComposite warp(Node node) {
            return new NodeComposite(node);
        }

        @Override
        public String toString() {
            return one.toString() + one.next.toString();
        }

    }


    public static void main(String[] args) {
        int[] values = new int[]{1, 2, 3, 4, 5};
        //普通链表
        Node node     = Node.valuesOf(values);
        Node warpNode = null;
        //转化为NodeComposite链表
        int index = 0;
        while (node != null && node.next != null) {
            node = NodeComposite.warp(node);
            if (index == 0) {
                warpNode = node;
            }
            node = node.next;
            index++;
        }
        //翻转
        while (warpNode != null) {
            if (warpNode instanceof NodeComposite) {
                ((NodeComposite) warpNode).swap();
            }
            System.out.println(warpNode);
            warpNode = warpNode.next;
        }
    }
}
