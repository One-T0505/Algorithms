package GreatOffer.TopInterviewQ;


// 给定单链表的头节点 head ，将所有索引为奇数的节点和索引为偶数的节点分别组合在一起，然后返回重新排序的列表。
// 第一个节点的索引被认为是奇数， 第二个节点的索引为偶数，以此类推。
// 请注意，偶数组和奇数组内部的相对顺序应该与输入时保持一致。
// 你必须在 O(1) 的额外空间复杂度和 O(n) 的时间复杂度下解决这个问题。

import utils.SingleNode;

public class _0328_OddEvenLinkedList {

    public SingleNode oddEvenList(SingleNode head) {
        // 如果头结点为空 或者 只有一个结点  或者 只有两个结点  都直接返回头
        if (head == null || head.next == null || head.next.next == null)
            return head;
        // 执行到这里说明，链表至少有三个结点
        SingleNode oddHead = head;  // 奇数区的头
        SingleNode oddTail = head;  // 奇数区的尾
        SingleNode evenHead = head.next;  // 偶数区的头
        SingleNode evenTail = head.next;  // 偶数区的尾
        SingleNode odd = head.next.next;
        SingleNode even = odd.next;

        while (odd != null && even != null) {
            oddTail.next = odd;
            oddTail = odd;
            odd = even.next;
            evenTail.next = even;
            evenTail = even;
            even = odd == null ? even : odd.next;
        }
        if (odd != null) {  // even == null
            oddTail.next = odd;
            oddTail = odd;
            evenTail.next = null;
        }
        oddTail.next = evenHead;
        return oddHead;
    }
}
