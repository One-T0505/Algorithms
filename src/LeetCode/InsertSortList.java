package LeetCode;

import utils.SingleNode;

/**
 * ymy
 * 2023/4/10 - 12 : 25
 **/

// leetCode147
// 给定单个链表的头 head ，使用 插入排序 对链表进行排序，并返回 排序后链表的头 。
// 插入排序 算法的步骤:
//  1.插入排序是迭代的，每次只移动一个元素，直到所有元素可以形成一个有序的输出列表。
//  2.每次迭代中，插入排序只从输入数据中移除一个待排序的元素，找到它在序列中适当的位置，并将其插入。
// 重复直到所有输入数据插入完为止。对链表进行插入排序。


public class InsertSortList {

    // 一定要去外国网站上看本题，因为外网上有一个动画演示
    public static SingleNode insertionSortList(SingleNode head) {
        if (head == null || head.next == null)
            return head;
        // 让 h、t 表示已排序区的头尾
        SingleNode h = head, t = head;
        SingleNode c = null;
        SingleNode n = t.next;
        while (n != null){
            c = n;
            n = n.next;
            t.next = null;
            SingleNode pre = null;
            SingleNode cur = h;
            while (cur != null && cur.val < c.val){
                pre = cur;
                cur = cur.next;
            }
            // 情况1：插在h之前
            if (pre == null){
                c.next = h;
                h = c;
            } else if (pre == t) {  // 情况2：插在t之后
                pre.next = c;
                t = c;
            } else { // 情况3：正常情况
                pre.next = c;
                c.next = cur;
            }
        }
        return h;
    }



    public static void main(String[] args) {
        SingleNode h = new SingleNode(3);
        h.next = new SingleNode(2);
        h.next.next = new SingleNode(4);
        SingleNode head = insertionSortList(h);
        while (head != null){
            System.out.print(head.val + " \t");
            head = head.next;
        }
        System.out.println();
    }
}
