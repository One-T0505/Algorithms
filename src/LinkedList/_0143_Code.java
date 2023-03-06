package LinkedList;

// 给定一个单链表 L 的头节点 head ，单链表 L 表示为： L0 → L1 → … → Ln - 1 → Ln
// 请将其重新排列后变为：L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …

public class _0143_Code {

    // 思路：先找到中间，然后将后半段逆序，再重新构造链接顺序
    public static void reorderList(SingleNode head) {
        if (head == null || head.next == null || head.next.next == null)
            return;
        SingleNode mid = midOrLatterMid(head);
        SingleNode end = reverse(mid);
        SingleNode next1 = null, next2 = null;
        while (head.next != mid){
            next1 = head.next;
            next2 = end.next;
            head.next = end;
            end.next = next1;
            head = next1;
            end = next2;
        }
        // 上面的终止情况是 head.next != mid  如果链表的结点是偶数个，那么从while出来后就完全连好了
        // 下面的if就是单独处理奇数个情况的时候，因为奇数个的时候从while出来还没连好
        if (end != mid){
            head.next = end;
        }
    }


    private static SingleNode midOrLatterMid(SingleNode head){
        if (head == null || head.next == null)
            return head;
        SingleNode slow = head.next;
        SingleNode fast = head.next;

        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
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
}
