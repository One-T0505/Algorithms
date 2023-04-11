package Basic.LinkedList;

// leetCode0002  链表的两数相加问题
// 给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
// 请你将两个数相加，并以相同形式返回一个表示和的链表。
// 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。

// 每个链表中的节点数在范围 [1, 100] 内
// 0 <= Node.val <= 9
// 题目数据保证列表表示的数字不含前导零

// 进阶问题参考leetCode445题

import utils.SingleNode;

public class _0002_Code {

    // 要考虑进位生成新结点的情况
    public static SingleNode addTwoNumbers(SingleNode l1, SingleNode l2) {
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
}
