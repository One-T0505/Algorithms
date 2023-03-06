package LinkedList;

// leetCode160
//  给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。
//  如果两个链表不存在相交节点，返回 null 。

// listA 中节点数目为 m   listB 中节点数目为 n
// 1 <= m, n <= 3 * 10^4
// 1 <= Node.val <= 10^5

public class _0160_Code {

    public static SingleNode getIntersectionNode(SingleNode headA, SingleNode headB) {
        int lenA = 0, lenB = 0;
        SingleNode A = headA;
        SingleNode B = headB;
        // 先统计两个链表的长度
        while(A != null){
            lenA++;
            A = A.next;
        }

        while(B != null){
            lenB++;
            B = B.next;
        }
        // 做差值
        int diff = Math.abs(lenA - lenB);

        // 让A最终指向较短的链表
        A = lenA > lenB ? headB : headA;
        // 让B最终指向较长的链表
        B = A == headA ? headB : headA;
        // 先让较长的链表和较短的链表处于同一起跑线上
        while(diff-- > 0){
            B = B.next;
        }
        while(A != B){
            A = A.next;
            B = B.next;
        }
        return A;
    }
}
