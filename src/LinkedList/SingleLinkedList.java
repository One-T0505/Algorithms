package LinkedList;


public class SingleLinkedList {

    // 单链表的反转
    public  static SingleNode singleLinkedListReverse(SingleNode head){
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

    public static void main(String[] args) {
        SingleNode n5 = new SingleNode(4, null);
        SingleNode n4 = new SingleNode(7, n5);
        SingleNode n3 = new SingleNode(12, n4);
        SingleNode n2 = new SingleNode(4, n3);
        SingleNode n1 = new SingleNode(4, n2);
        SingleNode head = n1;
//        SingleNode new_head = singleLinkedListTraverse(head);
//        while (new_head != null){
//            System.out.print(new_head.val + "\t");
//            new_head = new_head.next;
//        }
        SingleNode singleNode = removeTarget(head, 4);
        while (head != null){
            System.out.print(head.val + "\t");
            head =head.next;
        }
    }
}
