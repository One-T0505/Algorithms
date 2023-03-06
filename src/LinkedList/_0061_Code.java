package LinkedList;

// leetCode61
// 给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。
// 如果给的是：1->2->3->4->5  k==1   那就变成了  5->1->2->3->4
// 如果k==2  那就变成了  4->5->1->2->3

// 链表中节点的数目在范围 [0, 500] 内
// -100 <= Node.val <= 100
// 0 <= k <= 2 * 10^9

public class _0061_Code {

    // 要注意：题目说 0 <= k <= 2*10^9
    // 如果我们正常做，肯定会超时；我们发现，如果链表长度为n，并且k==n，那么旋转之后的链表和原始的一模一样
    // 所以，一开始我们就需要对k % n  使得其值变小。又因为，链表中节点的数目在范围 [0, 500] 内，
    // 所以，取模完之后的k就在[0,500] 这样就小很多了
    public static SingleNode rotateRight(SingleNode head, int k) {
        if (head == null || head.next == null || k == 0)
            return head;
        // 执行到这里说明，链表至少有两个结点，并且 k >= 1
        // 如果链表长度为11，k==4，其实最终的旋转结果就是：链表的最后4个保持不变，然后取链接前面的7个
        // 所以我们要做的就是将链表区分成了两部分：一部分是最后的k个结点；另一部分是开头的len-k个结点
        SingleNode cur = head;
        int len = 0;
        while (cur.next != null){
            len++;
            cur = cur.next;
        }
        k %= (++len);
        if (k == 0)
            return head;
        SingleNode latter = cur; // 让latter指向链表的最后一个结点
        // 从while出来后的len还不是真实的长度，还需要+1才是链表的长度
        cur = head;
        while (len-- != k + 1){
            cur = cur.next;
        }
        // 此时cur指向了第一部分的最后一个，因为要让其next指空  但是此时cur.next就是最终链表的新头部，所以要提前保存
        SingleNode newHead = cur.next;
        cur.next = null;
        latter.next = head;
        return newHead;
    }
}
