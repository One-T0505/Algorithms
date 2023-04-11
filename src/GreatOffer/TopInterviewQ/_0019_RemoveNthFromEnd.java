package GreatOffer.TopInterviewQ;


// 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。

public class _0019_RemoveNthFromEnd {


    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || n < 1)
            return head;
        ListNode cur = head;
        int len = 0;
        while (cur.next != null) {
            len++;
            cur = cur.next;
        }
        // cur此时在最后一个结点上
        len++;
        if (n > len)
            return head;
        // 如果要删除的结点刚好是头部
        if (n == len) {
            cur = head.next;
            head = null;
            return cur;
        }
        // 删除其他结点，我们要先找到倒数第n+1的结点
        cur = head;
        int d = 1;
        while (d < len - n) {
            cur = cur.next;
            d++;
        }
        // 此时cur已经来到了要删除的结点的前一个结点
        cur.next = cur.next.next;
        return head;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(3);
        ListNode n1 = new ListNode(5);
        ListNode n2 = new ListNode(8);
        ListNode n3 = new ListNode(2);
        head.next = n1;
        n1.next = n2;
        n2.next = n3;
        ListNode root = removeNthFromEnd(head, 4);
        while (root != null) {
            System.out.print(root.val + " ");
            root = root.next;
        }
        System.out.println();
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

}
