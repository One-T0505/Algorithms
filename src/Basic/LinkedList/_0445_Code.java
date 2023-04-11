package Basic.LinkedList;

// leetCode445
// 给你两个非空链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。
// 将这两数相加会返回一个新的链表。你可以假设除了数字 0 之外，这两个数字都不会以零开头。

import utils.SingleNode;
import java.util.Stack;

public class _0445_Code {

    // 方法1：因为顺序和leetCode 第2题相反，所以我们可以先把两个链表反转，再用和第2题一模一样的方法实现，
    public static SingleNode addTwoNumbers1(SingleNode l1, SingleNode l2) {
        SingleNode h1 = reverse(l1);
        SingleNode h2 = reverse(l2);
        SingleNode end = add(h1, h2);
        return reverse(end);
    }


    private static SingleNode reverse(SingleNode head){
        SingleNode pre = null, next;
        while (head != null){
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }


    private static SingleNode add(SingleNode l1, SingleNode l2) {
        int carry = 0; // 记录进位信息
        int sum = 0;
        int len1 = 0, len2 = 0;
        SingleNode h1 = l1, h2 = l2;
        while (h1 != null){
            len1++;
            h1 = h1.next;
        }
        h1 = l1;
        while (h2 != null){
            len2++;
            h2 = h2.next;
        }
        h2 = l2;
        SingleNode shorter = len1 < len2 ? l1 : l2;
        SingleNode longer = shorter == l1 ? l2 : l1;
        SingleNode pre = null;

        while (shorter != null){
            sum = (shorter.val + longer.val + carry) % 10;
            carry = (shorter.val + longer.val + carry) / 10;
            longer.val = sum;
            shorter = shorter.next;
            pre = longer;
            longer = longer.next;
        }
        // 此时出来了之后，shorter==null，longer也可能为空，如果两个链表等长；longer也可能不为空
        // pre肯定不为空，表示longer的前驱
        while (longer != null){
            sum = (longer.val + carry) % 10;
            carry = (longer.val + carry) / 10;
            longer.val = sum;
            pre = longer;
            longer = longer.next;
        }
        if (carry != 0)
            pre.next = new SingleNode(1, null);

        return len1 < len2 ? h2 : h1;
    }
    // =================================================================================================




    // 方法2：不反转链表的情况下，借助栈俩实现
    public static SingleNode addTwoNumbers(SingleNode l1, SingleNode l2) {
        int carry = 0, sum = 0, cur = 0;
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        SingleNode dummy = null;
        while (l1 != null){
            s1.push(l1.val);
            l1 = l1.next;
        }
        while (l2 != null){
            s2.push(l2.val);
            l2 = l2.next;
        }
        while (!s1.isEmpty() || !s2.isEmpty() || carry != 0){
            int a = s1.isEmpty() ? 0 : s1.pop();
            int b = s2.isEmpty() ? 0 : s2.pop();
            sum = a + b + carry;
            cur = sum % 10;
            carry = sum / 10;

            SingleNode node = new SingleNode(cur, null);
            node.next = dummy;
            dummy = node;
        }
        return dummy;
    }
}
