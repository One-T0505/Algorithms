package LinkedList;


public class SingleLinkedList {

    // 单链表的反转
    public static SingleNode singleLinkedListReverse(SingleNode head){
        SingleNode next = null, pre = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }


    // 单链表反转对数器
    public static SingleNode reverse(SingleNode head){
        SingleNode pre = null, next = null;
        while (head != null){
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }


    // 单链表反转的升级版，对应leetCode92题
    // 给你单链表的头指针 head 和两个整数 left 和 right ，其中 1 <= left <= right <= n 。请你反转从位
    // 置 left 到位置 right 的链表节点，返回反转后的链表。 并且题目还说 链表结点数量 >=1  就说明给的head不可能为空
    // 省去了判空
    // 比如：1->2->4->3->5  left==2  right==4   那么应该返回 1->3->4->2->5
    public static SingleNode reverseBetween(SingleNode head, int left, int right) {
        if (left == right || head.next == null)
            return head;
        // left < right && 链表至少有两个结点
        SingleNode cur = head;
        // 执行完下面的while后，pre就指向了目标区间的前驱了，即不用参与逆转的结点，
        // 而且有个边界情况，如果left==1，那么头也要参与逆转，所以此时的前驱就是null
        // 让post指向逆转区间的后继
        SingleNode pre = null, post = cur.next;
        while (right != 1){
            if (left > 1){
                pre = cur;
                left--;
            }
            cur = post;
            post = post.next;
            right--;

        }
        // 此时pre指向了目标区间的前驱  并且post指向了逆转区间的后继
        // start记录逆转区间开始的第一个结点  因为它逆转后是尾结点，逆转后，还需要链接后续的结点
        SingleNode start = pre == null ? head : pre.next;
        // 开始逆转
        cur = start;
        SingleNode p = null, n;
        while (cur != post){
            n = cur.next;
            cur.next = p;
            p = cur;
            cur = n;
        }
        // 此时p指向了逆转后的新头部
        if (pre != null)
            pre.next = p;
        start.next = post;
        return pre == null ? p : head;
    }



    // 将单链表中结点值为 target的结点全部删除，剩下的结点相对顺序不变
    public static SingleNode removeTarget(SingleNode head, int target){
        // 如果头结点就是 target，那头结点就会被删除，就会产生新的头结点
        // 所以首先要找到第一个不是target的结点作为新的头结点
        while (head != null){
            if (head.val != target)
                break;
            head = head.next;
        }
        // 跳出while循环后，head就来到了第一个不等于target的结点
        // 如果开头真的有若干val==target的结点的话，我们并没有真正删除它们，因为没必要真的删除。
        // 我们将第一个不是target结点a作为头结点返回，那么a之前的结点并没有引用能找到他们，java会自动回收掉前面那部分。
        SingleNode pre = head, cur = head;
        while (cur != null) {
            if (cur.val == target)
                pre.next = cur.next;
            else
                pre = cur;
            cur = cur.next;
        }
        return head;
    }
    // 进阶版：给定一个排好序的链表，删除所有重复值的元素。可以参考leetCode82 题。


    public static void main(String[] args) {
        SingleNode n5 = new SingleNode(4, null);
        SingleNode n4 = new SingleNode(7, n5);
        SingleNode n3 = new SingleNode(12, n4);
        SingleNode n2 = new SingleNode(4, n3);
        SingleNode n1 = new SingleNode(4, n2);
        SingleNode head = n1;
        SingleNode singleNode = removeTarget(head, 4);
        while (head != null){
            System.out.print(head.val + "\t");
            head =head.next;
        }
    }
}
