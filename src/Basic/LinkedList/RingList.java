package Basic.LinkedList;

// 常见面试题
// 给定两个可能有环也可能无环的单链表，头节点head1和head2。
// 请实现一个函数，如果两个链表相交，请返回相交的第一个节点。如果不相交，返回null
// 要求: 如果两个链表长度之和为N，时间复杂度请达到O(N)，额外空间复杂度请达到O(1)。

// 这题有点难的地方在于，会脑补很多有环的情况，但是这些情况都不会出现，请牢记这是单链表，只有一个next域，不可能形成这样的环：
//       ___                        \    /
//      /   \                        \  /
//      \   /                         |
// ______\_/______                    |
//                                   / \
//
// 如果两个链表真的相交，那肯定不可能再分开

// 先解决第一步：如果无环，返回null，如果有环返回入环的第一个结点
// 可以用容器，或者不用容器做，容器做起来很容易。
// 容器：用一个hashSet，记录结点，当第一次有结点出现在set中时就是入环的第一个结点
// 不用容器：用快慢指针，如果有环快慢指针必然会相遇，当相遇时让快指针回到头部，此时快慢指针每次都只移动一步，当再次相遇时就是
// 入环的第一个结点。不用管证明，记住这个结论就好

import utils.SingleNode;
import java.util.HashSet;

public class RingList {

    // 找到入环的第一个结点的两种方法

    public static SingleNode getLoopNodeV1(SingleNode head) {
        if (head == null)
            return null;
        HashSet<SingleNode> set = new HashSet<>();
        while (head != null){
            if (!set.contains(head))
                set.add(head);
            else
                return head;
            head = head.next;
        }
        return null;
    }


    // 该局部方法对应了leetCode141 和 142 题，直接通过。整体算法一模一样，只需要判断题目要求的长度为1，
    // 长度为2时，返回什么值，像这样的规定性问题需要辨别一下就行了。
    public static SingleNode getLoopNodeV2(SingleNode head){
        if (head == null || head.next == null || head.next.next == null)
            return null;
        SingleNode slow = head.next, fast = head.next.next;
        while (slow != fast){
            if (fast.next == null || fast.next.next == null)
                return null;
            slow = slow.next;
            fast = fast.next.next;
        }
        fast = head;
        while (slow != fast){
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    // 第一步找到入环结点后，应该处理下一个问题了。下面的方法用于已知两个单链表都无环的情况下，返回第一个相交结点，
    // 若不相交则返回null.
    // 如果两个单链表相交，那么最后一个结点内存地址必然相同，若不相同则不相交。若相交，那么最后一个结点也只是相交的
    // 最后一个结点，我们需要找到相交的第一个结点。这样找：假设链表1长度为50，链表2长度为30，那就让h1先走20步，然
    // 后两个一起走，第一个相同的地方就是第一个相交的地方

    public static SingleNode noLoop(SingleNode h1, SingleNode h2) {
        if (h1 == null || h2 == null)
            return null;
        SingleNode cur1 = h1, cur2 = h2;
        int len = 0;
        while (cur1.next != null){
            len++;
            cur1 = cur1.next;
        }
        while (cur2.next != null){
            len--;
            cur2 = cur2.next;
        }
        // 此时cur1，cur2都来到了最后一个结点
        if (cur1 != cur2)
            return null;
        // 此时len的值表示为：链表1的长度 - 链表2的长度
        cur1 = len > 0 ? h1 : h2;      // cur1指向较长的链表头
        cur2 = cur1 == h1 ? h2 : h1;   // cur2指向较短的链表头
        len = Math.abs(len);
        while (len != 0) {
            cur1 = cur1.next;
            len--;
        }
        // 此时cur1，cur2来到了同一起跑线
        while (cur1 != cur2){
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }

    // 如果一个链表有环，一个链表无环，这样是不可能相交的。所以只需要在讨论下，两个链表都有环的情况下，如何找出
    // 相交的第一个结点  两个链表都有环的情况也要再细分成三种情况：
    // 1.两个链表各自玩各自的，没有任何关系
    //              ___                 ___
    //             /   \               /   \
    //        _____\___/          _____\___/
    //
    // 2.环型区域整个都是公共的   即：在入环前重合
    //                    ___
    //              \    /   \
    //           ____\___\___/
    //
    // 3.环型区域有部分重合  即：在入环后重合
    //
    //                    ___
    //                   /   \
    //              _____\___/
    //                      /
    //                     /
    // 还记得上面写的两个找出第一个入环结点的方法吗？ 如果两个入环结点相同那就是情况2，如果不相同，那就是1和3情况
    // 如果是情况2：那就可以把找到的共同入环结点当作结尾，然后学noLoop方法，让长链表走差值步，就能找到第一个相交结点
    // 如果是情况1和情况3：那就让一个入环结点loop1不停往下一格走，另一个入环结点loop2不动，如果loop1转了一圈回到自己还没碰到loop2，
    // 那就是情况1，否则就是情况3

    // h1是第一个链表头部，loop1是链表1的入环结点；h2是第二个链表头部，loop2是链表2的入环结点
    public static SingleNode bothLoop(SingleNode h1, SingleNode loop1, SingleNode h2, SingleNode loop2) {
        SingleNode cur1 = null, cur2 = null;
        if (loop1 == loop2) { // 情况2
            cur1 = h1;
            cur2 = h2;
            int len = 0;
            while (cur1 != loop1) {
                len++;
                cur1 = cur1.next;
            }
            while (cur2 != loop2) {
                len--;
                cur2 = cur2.next;
            }
            cur1 = len > 0 ? h1 : h2;
            cur2 = cur1 == h1 ? h2 : h1;
            len = Math.abs(len);
            while (len != 0) {
                cur1 = cur1.next;
                len--;
            }
            while (cur1 != cur2) {
                cur1 = cur1.next;
                cur2 = cur2.next;
            }
            return cur1;
        } else { // 情况1或情况3
            cur1 = loop1.next;
            while (cur1 != loop1) {
                if (cur1 == loop2)
                    return loop1;   // 返回loop2也行，loop1离h1近，loop2离h2近
                cur1 = cur1.next;
            }
            return null;
        }
    }

    // 终于来到了主函数
    public static SingleNode getFirstIntersect(SingleNode h1, SingleNode h2) {
        if (h1 == null || h2 == null)
            return null;
        SingleNode loop1 = getLoopNodeV2(h1);
        SingleNode loop2 = getLoopNodeV2(h2);
        if (loop1 == null && loop2 == null)
            return noLoop(h1, h2);
        if (loop1 != null && loop2 != null)
            return bothLoop(h1, loop1, h2, loop2);
        return null;
    }
}
