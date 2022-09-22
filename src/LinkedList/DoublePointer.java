package LinkedList;

import java.util.ArrayList;
import java.util.HashMap;
import utils.*;

// 快慢指针问题
public class DoublePointer {

    // 1.输入链表头结点，奇数长度返回中点，偶数长度返回上中点（因为偶数长度会有两个中点）
    // 比如：4->2->11->7  2和11就是中点，2是上中点，11是下中点
    public static SingleNode midOrFormerMid(SingleNode head){
        // base case: 头结点为空 或者 只有一个头结点 或者 只有两个结点
        if (head == null || head.next == null || head.next.next == null)
            return head;
        // 链表有3个结点或以上
        SingleNode slow = head.next, fast = head.next.next;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


    // 2.输入链表头结点，奇数长度返回中点，偶数长度返回下中点（因为偶数长度会有两个中点）
    public static SingleNode midOrLatterMid(SingleNode head){
        // base case: 头为空或者只有一个结点或者只有两个结点，直接返回head
        if (head == null || head.next == null)
            return head;
        // 执行到这里说明至少有2个结点
        SingleNode slow = head.next, fast = head.next;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // 3.输入链表头结点，奇数长度返回中点的前驱，偶数长度返回上中点的前驱（因为偶数长度会有两个中点）
    public static SingleNode midOrFormerMidPre(SingleNode head){
        // base case: 头为空或者只有一个结点或者只有两个结点，直接返回null
        if (head == null || head.next == null || head.next.next == null)
            return null;
        // 执行到这里说明至少有3个结点
        SingleNode slow = head, fast = head.next.next;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // 4.输入链表头结点，奇数长度返回中点的前驱，偶数长度返回上中点（因为偶数长度会有两个中点）
    public static SingleNode midOrLatterMidPre(SingleNode head){
        // base case: 头为空或者只有一个结点，直接返回null
        if (head == null || head.next == null)
            return null;
        // 执行到这里说明至少有2个结点
        SingleNode slow = head, fast = head.next;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // 5.将单链表按某个值划分成左边小、中间相等、右边大的形式
    //   方法1：把链表放数组里，在数组上做partition，在把数组构建成单链表（笔试用）时间复杂度：O(N), 空间复杂度：O(N)
    //   方法2：就在链表上分成小、中、大三部分再把各部分之间串起来（面试用）时间复杂度：O(N), 空间复杂度：O(1)
    public static SingleNode linkedListPartitionV1(SingleNode head, int pivot){
        if (head == null)
            return null;
        // 统计单链表的长度
        SingleNode cur = head;
        int i = 0;
        while (cur != null){
            i++;
            cur = cur.next;
        }
        SingleNode[] arr = new SingleNode[i];
        cur =head;
        for (i = 0; i < arr.length; i++) {
            arr[i] = cur;
            cur = cur.next;
        }
        partition(arr, pivot);
        for (i = 1; i != arr.length; i++) {
            arr[i - 1].next = arr[i];
        }
        arr[i - 1].next = null;
        return arr[0];
    }

    private static void partition(SingleNode[] arr, int pivot) {
        int left = -1, right = arr.length, index = 0;
        while (index < right){
            if (arr[index].val == pivot)
                index++;
            else if (arr[index].val < pivot) {
                swap(arr, index++, ++left);
            }else
                swap(arr, index, --right);
        }
    }

    private static void swap(SingleNode[] arr, int i, int j){
        SingleNode tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 升级版：只需要6个引用变量
    // 小于区的头尾指针：lH、lT；等于区的头尾指针：eH、eT；大于区的头尾指针：gH、gT
    // 遍历链表，将元素按照大小放在合适的区，最后再将小于区的尾指针->等于区的头指针 等于区的尾指针->大于区的头指针
    // 这样还能保证稳定性
    public static SingleNode linkedListPartitionV2(SingleNode head, int pivot){
        SingleNode lH = null, lT = null, eH = null, eT = null, gH = null, gT = null, next = null;
        while (head != null){
            next = head.next;
            head.next = null;
            // 第一种情况：进入小于区
            if (head.val < pivot){
                if (lH == null){ // 还需要判断是不是小于区的第一个元素
                    lH = head;
                    lT = head;
                }else {
                    lT.next = head;
                    lT = head;
                }
            } else if (head.val == pivot) {  // 第二种情况：进入等于区
                if (eH == null){
                    eH = head;
                    eT = head;
                }else {
                    eT.next = head;
                    eT = head;
                }
            } else {    // 第三种情况：进入大于区
                if (gH == null){
                    gH = head;
                    gT = head;
                }else {
                    gT.next = head;
                    gT = head;
                }
            }
            head = next;
        }
        // 遍历完后，链表的元素都会进入到该进的区；但是有可能链表里所有的元素都大于等于pivot，压根不存在小于区，所以在
        // 合并前还需要检查三个区是否真的存在
        if (lT != null){
            // 说明有小于区
            lT.next = eH; // 即使等于区不存在，那么这句话也只是将lT.next = null；等于区存在那就是切实首尾相连
            eT = eT == null ? lT : eT;  // 判断等于区是否真的存在，若不存在则让小于区的尾指向大于区的头
        }
        if (eT != null)
            eT.next = gH; // 即使大于区为空也无妨
        // 执行到这里时，小于区的尾连接等于区的头，等于区的尾连接大于区的头，这样的首尾连接问题已经全部解决，所有情况都考虑进去了
        // 最后一步就是确定返回哪个当头
        return lH != null ? lH : (eH != null ? eH : gH);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            SingleNode head = linkedlist.generateRandomLinkedList(6, 30);
            linkedlist.printLinkedList(head);
            SingleNode newHead = linkedListPartitionV2(head, 15);
            linkedlist.printLinkedList(newHead);
            System.out.println("====================================");
        }
    }
}

