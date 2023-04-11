package GreatOffer.TopInterviewQ;

/**
 * ymy
 * 2023/3/15 - 10 : 31
 **/

import utils.SingleNode;
import java.util.PriorityQueue;

// 给你一个链表数组，每个链表都已经按升序排列。
// 请你将所有链表合并到一个升序链表中，返回合并后的链表。

public class _0023_MergeSortedList {


    public SingleNode mergeKLists(SingleNode[] lists) {
        if(lists == null || lists.length == 0)
            return null;
        int N = lists.length;
        // 小根堆
        PriorityQueue<SingleNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (SingleNode s : lists) {
            if (s != null)
                heap.add(s);
        }
        SingleNode head = null;
        SingleNode tail = null;
        while (!heap.isEmpty()){
            SingleNode poll = heap.poll();
            if (head == null){
                head = poll;
                tail = poll;
            } else {
                tail.next = poll;
                tail = poll;
            }
            if (poll.next != null)
                heap.add(poll.next);
        }
        return head;
    }

}
