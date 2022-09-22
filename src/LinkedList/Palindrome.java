package LinkedList;

import java.util.Stack;
import utils.*;

// 判断回文
public class Palindrome {
    // 单链表判断回文
    // 基础法：O(n) T(n)  用一个栈装结点
    public static boolean isPalindromeV1(SingleNode head) {
        if (head == null)
            return true;
        Stack<SingleNode> stack = new Stack<>();
        SingleNode cur = head;
        while (cur.next != null) {
            stack.push(cur);
            cur = cur.next;
        }
        stack.push(cur);
        while (!stack.isEmpty()) {
            if (head.val != stack.pop().val)
                return false;
            head = head.next;
        }
        return true;
    }

    // 优化版：不需要把整个链表都入栈，只需要入栈后半部分。奇数个结点时，入栈中点加后半部分；偶数个结点入栈后半部分
    public static boolean isPalindromeV2(SingleNode head){
        if (head == null)
            return true;
        SingleNode mid = DoublePointer.midOrLatterMid(head);
        Stack<Integer> stack = new Stack<>();
        while (mid.next != null){
            stack.push(mid.val);
            mid = mid.next;
        }
        stack.push(mid.val);
        while (!stack.isEmpty()){
            if (head.val != stack.pop())
                return false;
            head = head.next;
        }
        return true;
    }

    // 进阶法：O(n) T(1)  快慢指针法：该算法适用于奇数个结点和偶数个结点，也适用于非常短的链表。通用模板
    public static boolean isPalindromeV3(SingleNode head){
        if (head == null || head.next == null)
            return true;
        // 说明至少有两个结点。使用快慢指针，当有奇数个结点时，慢指针到中点；当有偶数个结点时，慢指针返回下中点
        SingleNode cur = head;
        SingleNode mid = DoublePointer.midOrLatterMid(head);
        SingleNode newHead = linkedlist.reverseSingleLinkedList(mid);
        SingleNode log = newHead;
        while (newHead != null){
            if (cur.val != newHead.val)
                return false;
            cur = cur.next;
            newHead = newHead.next;
        }
        // 再将后半部分逆序回来
        linkedlist.reverseSingleLinkedList(log);
        return true;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            SingleNode src = linkedlist.generateRandomLinkedList(10, 100);
            SingleNode copy = linkedlist.copyLinkedList(src);
            if (isPalindromeV3(src) != isPalindromeV1(copy)){
                System.out.println("Failed");
                linkedlist.printLinkedList(src);
                return;
            }
        }
        System.out.println("AC");
    }
}
