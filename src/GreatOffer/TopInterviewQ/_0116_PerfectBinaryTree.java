package GreatOffer.TopInterviewQ;

// 给定一个完美二叉树，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下:
// struct Node {
//  int val;
//  Node *left;
//  Node *right;
//  Node *next;
// }
// 填充它的每个 next 指针，让这个指针指向其同层的下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为NULL。
// 初始状态下，所有 next 指针都被设置为 NULL。

public class _0116_PerfectBinaryTree {

    // 主方法
    public Node connect(Node root) {
        if (root == null)
            return null;
        MyQueue queue = new MyQueue();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node pre = null;
            int size = queue.size;
            for (int i = 0; i < size; i++) {
                Node cur = queue.poll();
                if (cur.left != null)
                    queue.add(cur.left);
                if (cur.right != null)
                    queue.add(cur.right);
                if (pre != null)
                    pre.next = cur;
                pre = cur;
            }
        }
        return root;
    }


    // 这个题目我们希望实现的同时能达到额外空间复杂度为:O(1)，所以需要我们自己手动写个双端队列

    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

    public static class MyQueue {  // 该结构只有三个变量，不停地复用，所以额外空间复杂度为O(1)
        Node head;
        Node tail;
        int size;

        public MyQueue() {
            head = null;
            tail = null;
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void add(Node cur) {
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                tail.next = cur;
                tail = cur;
            }
            size++;
        }


        // 上游自己判断队列是否为空
        public Node poll() {
            size--;
            Node poll = head;
            if (head == tail)
                tail = null;
            head = head.next;
            poll.next = null;
            return poll;
        }
    }
}
