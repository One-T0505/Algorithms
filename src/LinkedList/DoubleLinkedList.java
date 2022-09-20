package LinkedList;

public class DoubleLinkedList {

    // 双链表反转
    public static DoubleNode doubleLinkedListReverse(DoubleNode head){
        DoubleNode pre = null, next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            head.pre = next;
            pre = head;
            head = next;
        }
        return pre;
    }
}
