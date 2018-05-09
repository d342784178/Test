package 刷题.leetcode;

import java.util.LinkedList;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-15
 * Time: 18:02
 */
public class RemoveNthNodeFromEndofList {
    public static void main(String args[]) {
        ListNode head  = new ListNode(1);
        ListNode head2 = new ListNode(2);
        head.next = head2;
        new RemoveNthNodeFromEndofList().removeNthFromEnd(head, 2);
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode             temp    = head;
        LinkedList<ListNode> objects = new LinkedList<>();
        objects.add(temp);
        while (temp.next != null) {
            temp = temp.next;
            objects.add(temp);
        }
        if (objects.size()  == n) {
            return head.next;
        }
        ListNode pre     = objects.get(objects.size() - n - 1);
        ListNode current = objects.remove(objects.size() - n);
        pre.next = current.next;
        return head;
    }

    private static class ListNode {
        int      val;
        ListNode next;

        public ListNode(int x) {
            val = x;
        }
    }
}
