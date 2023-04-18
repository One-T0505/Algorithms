package Basic.Queue;


import utils.DoubleNode;

// Java在底层已经用双向链表实现好了双端队列，这里我们自己再复现一下
// 用该双向链表可以实现栈和队列
public class DoubleEndsLinkedList {

    public DoubleNode head;
    public DoubleNode tail;

    // 从头部入队
    public void addToHead(int val){
        DoubleNode node = new DoubleNode(val, null, null);
        if (head == null){
            head = node;
            tail = node;
        }else {
            node.next = head;
            head.pre = node;
            head = node;
        }
    }

    // 从尾部入队
    public void addToTail(int val){
        DoubleNode node = new DoubleNode(val, null, null);
        // 说明是第一个加入的结点
        if (tail == null){
            head = node;
            tail = node;
        }else {
            node.pre = tail;
            tail.next = node;
            tail = node;
        }
    }

    public Integer pollFromHead(){
        if (head == null)
            return null;
        DoubleNode cur = head;
        if (head == tail){
            head = null;
            tail = null;
        }else {
            head = head.next;
            cur.next = null;
            head.pre = null;
        }
        return cur.val;
    }

    // 从尾部出队列
    public Integer pollFromTail(){
        if (head == null)
            return null;
        DoubleNode cur = tail;
        if (head == tail){
            head = null;
            tail = null;
        }else {
            tail = tail.pre;
            tail.next = null;
            cur.pre = null;
        }
        return cur.val;
    }
}
