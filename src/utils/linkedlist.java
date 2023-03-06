package utils;

import LinkedList.SingleNode;

// 一些和链表相关的常用公共方法
public class linkedlist {

    // 生成一个长度随机，值随机的单链表，并返回头结点
    public static SingleNode generateRandomLinkedList(int maxSize, int maxVal){
        int[] arr = arrays.randomNoNegativeArr(maxSize, maxVal);
        return tailInsert(arr);
    }


    // 头插法建立单链表
    public static SingleNode headInsert(int[] arr){
        int len = arr.length;
        SingleNode head = null, pointer = null;
        for (int j : arr) {
            SingleNode cur = new SingleNode(j, null);
            head = cur;
            cur.next = pointer;
            pointer = cur;
        }
        return head;
    }

    // 尾插法建立单链表
    public static SingleNode tailInsert(int[] arr){
        if (arr == null || arr.length == 0)
            return null;
        int len = arr.length;
        SingleNode head = null, pointer = null;
        for (int j : arr) {
            SingleNode cur = new SingleNode(j, null);
            if (head == null)
                head = cur;
             else
                pointer.next = cur;
            pointer = cur;
        }
        return head;
    }

    public static void printLinkedList(SingleNode head){
        if (head == null)
            return;
        while (head != null){
            System.out.print(head.val + "\t");
            head = head.next;
        }
        System.out.println();
    }

    // 求单链表长度
    public static int len(SingleNode head){
        if (head == null)
            return 0;
        int length = 0;
        while (head != null){
            length++;
            head = head.next;
        }
        return length;
    }

    // 判断两个单链表是否完全一样
    public static boolean isSame(SingleNode list1, SingleNode list2){
        if (len(list1) >= 0 && len(list2) >= 0 && len(list1) == len(list2)){
            while (list1 != null && list2 != null){
                if (list1.val != list2.val)
                    return false;
                list1 = list1.next;
                list2 = list2.next;
            }
            return true;
        }else
            return false;
    }

    // 逆序单链表
    public static SingleNode reverseSingleLinkedList(SingleNode head){
        if (head == null)
            return null;
        SingleNode pre = null, next = null;
        while (head != null){
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    // 单链表拷贝
    public static SingleNode copyLinkedList(SingleNode head){
        if (head == null)
            return null;
        SingleNode cur = new SingleNode(head.val, null);
        SingleNode res = cur;
        head = head.next;
        while (head != null){
            cur.next = new SingleNode(head.val, null);
            cur =cur.next;
            head = head.next;
        }
        return res;
    }


    public static void main(String[] args) {
    }
}
