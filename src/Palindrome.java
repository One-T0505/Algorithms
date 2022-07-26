import java.util.Stack;

// 判断回文
public class Palindrome {
    // 单链表判断回文
    // 基础法：O(n) T(n)
    public boolean isPalindrome1(Node head) {
        Stack<Node> stack = new Stack<>();
        Node cur = head;
        while (cur.next != null) {
            stack.push(cur);
            cur = cur.next;
        }
        stack.push(cur);
        while (!stack.empty()) {
            if (head.value != stack.pop().value)
                return false;
            head = head.next;
        }
        return true;
    }

    // 进阶法：O(n) T(1)  快慢指针法：该算法适用于奇数个结点和偶数个结点，也适用于非常短的链表。通用模板
    public boolean isPalindrome2(Node head) {
        //找到中点
        Node fast = head, slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        //将链表的后半部分逆置
        fast = slow.next;
        slow.next = null;
        Node temp = null;
        while (fast != null) {
            temp = fast.next;
            fast.next = slow;
            slow = fast;
            fast = temp;
        }
        temp = slow;
        fast = head;
        boolean res = true;
        while (slow != null && fast != null) {
            if (slow.value != fast.value) {
                res = false;
                break;
            }
            slow = slow.next;
            fast = fast.next;
        }
        //恢复链表
        slow = temp.next;
        temp.next = null;
        while (slow != null) {
            fast = slow.next;
            slow.next = temp;
            temp = slow;
            slow = fast;
        }
        return res;
    }
}

class Node {
    public int value;
    public Node next;

    public Node(int value) {
        this.value = value;
    }
}
