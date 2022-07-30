package LinkedList;

import java.util.Stack;

// 判断回文
public class Palindrome {
    // 单链表判断回文
    // 基础法：O(n) T(n)
    public static boolean isPalindrome1(SingleNode head) {
        if (head == null)
            return true;
        Stack<SingleNode> stack = new Stack<>();
        SingleNode cur = head;
        while (cur.next != null) {
            stack.push(cur);
            cur = cur.next;
        }
        stack.push(cur);
        while (!stack.empty()) {
            if (head.val != stack.pop().val)
                return false;
            head = head.next;
        }
        return true;
    }

    // 进阶法：O(n) T(1)  快慢指针法：该算法适用于奇数个结点和偶数个结点，也适用于非常短的链表。通用模板
    public static boolean isPalindrome2(SingleNode head) {
        // base case
        if (head == null || head.next == null)
            return true;
        // 执行到这里说明至少有两个结点
        SingleNode mid = DoublePointer.method1(head);
        //将链表的后半部分逆置
        SingleNode cur = mid.next, next = null;
        mid.next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = mid;
            mid = cur;
            cur = next;
        }
        next = mid;
        cur = head;
        boolean res = true;
        while (mid != null && cur != null) {
            if (mid.val != cur.val) {
                res = false;
                break;
            }
            mid = mid.next;
            cur = cur.next;
        }
        //恢复链表
        mid = next.next;
        next.next = null;
        while (mid != null) {
            cur = mid.next;
            mid.next = next;
            next = mid;
            mid = cur;
        }
        return res;
    }

    // 对数器
    public static int[] generateRandomArray(int maxSize, int range){
        int size = (int) (Math.random() * (maxSize + 1));
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++)
            arr[i] = (int) (Math.random() * (range + 1));
        return arr;
    }

    // 给一个数组，按照尾插法构造成单链表，并返回head
    public static SingleNode tailInsert(int[] arr){
        if (arr == null || arr.length == 0)
            return null;
        SingleNode head = new SingleNode(arr[0], null);
        SingleNode tmp = head;
        for (int i = 1; i < arr.length; i++) {
            SingleNode cur = new SingleNode(arr[i], null);
            tmp.next = cur;
            tmp = cur;
        }
        return head;
    }

    public static void display(int[] arr){
        for (int item : arr) System.out.print(item + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        // 对进阶方法测试 100，0000次
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            int[] randomArray = generateRandomArray(40, 1000);
//            display(randomArray);
            SingleNode head = tailInsert(randomArray);
            if (isPalindrome1(head) != isPalindrome2(head))
                throw new RuntimeException("不一致，失败！！！");
        }
        System.out.println("测试成功！！");
        long end = System.currentTimeMillis();
        System.out.println((end - start));
    }
}
