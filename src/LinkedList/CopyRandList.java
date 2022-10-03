package LinkedList;

import java.util.HashMap;

// 有一个链表，结点采用RandNode型，完全复刻一个一模一样的链表，不仅要复刻出next，也要复刻出rand，并返回新头部
// 有两种方式实现，一种是借助容器，一种只用有限几个变量
public class CopyRandList {
    // 利用容器，时间复杂度为：O(N) 空间复杂度：O(N)
    public static RandNode copyRandListV1(RandNode head){
        // 该哈希表用于存储：老结点->新结点 的对应关系
        HashMap<RandNode, RandNode> map = new HashMap<>();
        RandNode cur = head;
        while (cur != null){
            map.put(cur, new RandNode(cur.val));
            cur = cur.next;
        }
        cur =head;
        while (cur != null){
            map.get(cur).next = cur.next;
            map.get(cur).rand = cur.rand;
            cur = cur.next;
        }
        return map.get(head);
    }

    // 进化方法：不用容器，使得空间复杂度降低为O(1). 将原链表中的每一个结点对应的新结点插入到原结点后面，手动做出一个对应关系
    public static RandNode copyRandListV2(RandNode head){
        if (head == null)
            return null;
        RandNode cur = head, next = null;
        // while循环，将原链表长度翻倍，并且奇数位上都是原结点，偶数位上都是新结点
        while (cur != null){
            next = cur.next;
            cur.next = new RandNode(cur.val);
            cur.next.next = next;
            cur = next;
        }
        cur = head;
        next = cur.next;
        // 复刻rand域
        while (cur != null){
            next.rand = cur.rand;
            cur = next.next;
            next = cur == null ? next : cur.next;
        }
        cur = head;
        next = cur.next;
        RandNode res = next;
        // 再将两条链分开
        while (next != null){
            cur.next = next.next;
            cur = next;
            next = next.next;
        }
        return res;
    }

    // RandList的输出
    public static void printRandList(RandNode head){
        StringBuffer next = new StringBuffer("");
        StringBuffer rand = new StringBuffer("");
        RandNode cur = head;
        while (cur != null){
            next.append(cur.val).append("  ");
            rand.append(cur.rand == null ? null : cur.rand.val).append("  ");
            cur = cur.next;
        }
        System.out.println(next);
        System.out.println(rand);
    }

    public static void main(String[] args) {
        RandNode n1 = new RandNode(3);
        RandNode n2 = new RandNode(2);
        RandNode n3 = new RandNode(7);
        RandNode n4 = new RandNode(9, null, null);
        n3.next = n4;
        n3.rand = n2;
        n2.next = n3;
        n2.rand = n1;
        n1.next = n2;
        n1.rand = n3;
        RandNode randNode = copyRandListV2(n1);
        printRandList(randNode);

    }
}
