package LinkedList;

// 给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题
// （即，只能进行节点交换）。

// 链表中节点的数目在范围 [0, 100] 内

public class _0024_Code {

    // 思路：先把链表分成奇数链和偶数链，默认链表头结点下标为1
    // 然后将这两个链交叉相连即可。
    // 这个方法是我自己想的，方法2和方法3是官方提供的
    public static SingleNode swapPairs(SingleNode head) {
        if (head == null || head.next == null)
            return head;
        // 分链
        SingleNode oddHead = head, evenHead = head.next;
        SingleNode odd = oddHead, even = evenHead;
        while (even != null){
            odd.next = even.next;
            odd = even;
            even = even.next;
        }
        // 交叉相连
        odd = oddHead;
        even = evenHead;
        SingleNode oddNext = null;
        SingleNode evenNext = null;
        while (even.next != null){
            oddNext = odd.next;
            evenNext = even.next;
            even.next = odd;
            odd.next = evenNext;
            odd = oddNext;
            even = evenNext;
        }
        even.next = odd;
        return evenHead;
    }



    // 方法2  递归
    public static SingleNode swapPairs2(SingleNode head) {
        if (head == null || head.next == null)
            return head;
        SingleNode newHead = head.next;
        head.next = swapPairs2(newHead.next);
        newHead.next = head;
        return newHead;
    }


    // 方法3：循环遍历  思路类似方法1，但是处理起来比方法1更简洁
    public static SingleNode swapPairs3(SingleNode head) {
        if (head == null || head.next == null)
            return head;
        SingleNode dummy = new SingleNode(0, head);
        SingleNode temp = dummy;
        while (temp.next != null && temp.next.next != null){
            SingleNode n1 = temp.next;
            SingleNode n2 = temp.next.next;
            temp.next = n2;
            n1.next = n2.next;
            n2.next = n1;
            temp = n1;
        }
        return dummy.next;
    }
}
