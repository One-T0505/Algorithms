package LinkedList;

// leetCode82
// 给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，只留下不同的数字。返回已排序的链表 。

// 链表中节点数目在范围 [0, 300] 内

public class _0082_Code {

    // 我们要充分利用排好序这一特点
    public SingleNode deleteDuplicates(SingleNode head) {
        if (head == null || head.next == null)
            return head;
        // 执行到这里说明链表至少有两个结点了
        // 在head之前再新建一个结点
        // 这个dummy就像一个起点，后面连着的都是正经要去判断的，因为是排好序的，所以重复值的元素都是连在一起的。
        SingleNode dummy = new SingleNode(0, head);
        SingleNode cur = dummy;
        // cur一开始指向dummy，是置身事外的一个点，所以必须保证其后面有两个点，才有必要去重，如果只有一个点，根本不用去重，
        // 因为cur一开始指向的dummy在题目给的链表中根本不存在。
        // 当算法执行了一会后，cur可能指向的是真实链表中的元素，但是它的地位依然是完全不用考虑在去重范围内的，我们要考虑的
        // 永远是cur后面的元素；cur此时指向的元素，既然被保留下来了，那它势必和之前被删除的一批元素不同值，并且和cur的下一个
        // 元素不同值，所以此时的cur是要被保留的元素，这也就是为什么cur来到了这里。

        while (cur.next != null && cur.next.next != null){
            // 如果后面的两个元素相等，就意味着后面又迎来了一批相同值的元素，但数量还未知
            // 我们需要先把这个重复值用x记录下来，只要后面的值是x，那就一直删除，直到碰到不是x的结点，才说明删除干净了
            if (cur.next.val == cur.next.next.val){
                int x = cur.next.val;
                while (cur.next != null && cur.next.val == x)
                    cur.next = cur.next.next;
            } else // 如果 cur 下一个  下两个     默认是cur != 下一个的值   要不然cur也不会在这个地方
            // 然后 下一个 != 下两个  所以下一个是可以被保留的点
                cur = cur.next;
        }
        return dummy.next;
    }
}
