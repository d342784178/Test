package 刷题.leetcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-14
 * Time: 10:04
 */
public class AddTwoNumbers {
    public static class Solution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode first = new ListNode(0);
            ListNode tmp   = first;
            int      carry = 0;
            while (true) {
                int left  = l1 == null ? 0 : l1.val;
                int right = l2 == null ? 0 : l2.val;
                tmp.val = (left + right + carry) % 10;
                carry = (left + right + carry) / 10;
                //指向下一个
                l1 = (l1 == null ? null : l1.next);
                l2 = (l2 == null ? null : l2.next);
                //两种情况要判断下一个:
                //1. carry>0
                //2. l1或l2不为null
                if (carry > 0 || (l1 != null || l2 != null)) {
                    tmp.next = new ListNode(0);
                    tmp = tmp.next;
                } else {
                    break;
                }
            }
            return first;
        }
    }

    //==================================//==================================//==================================//==================================
    private static class ListNode {
        int      val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public static int[] stringToIntegerArray(String input) {
        input = input.trim();
        input = input.substring(1, input.length() - 1);
        if (input.length() == 0) {
            return new int[0];
        }

        String[] parts  = input.split(",");
        int[]    output = new int[parts.length];
        for (int index = 0; index < parts.length; index++) {
            String part = parts[index].trim();
            output[index] = Integer.parseInt(part);
        }
        return output;
    }

    public static ListNode stringToListNode(String input) {
        // Generate array from the input
        int[] nodeValues = stringToIntegerArray(input);

        // Now convert that list into linked list
        ListNode dummyRoot = new ListNode(0);
        ListNode ptr       = dummyRoot;
        for (int item : nodeValues) {
            ptr.next = new ListNode(item);
            ptr = ptr.next;
        }
        return dummyRoot.next;
    }

    public static String listNodeToString(ListNode node) {
        if (node == null) {
            return "[]";
        }

        String result = "";
        while (node != null) {
            result += Integer.toString(node.val) + ", ";
            node = node.next;
        }
        return "[" + result.substring(0, result.length() - 2) + "]";
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String         line;
        while ((line = in.readLine()) != null) {
            ListNode l1 = stringToListNode(line);
            line = in.readLine();
            ListNode l2 = stringToListNode(line);

            ListNode ret = new Solution().addTwoNumbers(l1, l2);

            String out = listNodeToString(ret);

            System.out.print(out);
        }
    }

}
