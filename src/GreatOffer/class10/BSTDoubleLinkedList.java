package class10;

import utils.TreeNode;

// 给定一棵搜索二叉树头节点，转化成首尾相接的有序双向链表. 假如给的搜索二叉树如下左图：
//
//               4
//             /   \                         ↓---------------|
//            2     5                  head->1<=>2<=>3<=>4<=>5
//          /   \                            |_______________↑
//         1     3
//
// 可以发现，树上的每个结点都有left、right两个指针，双向链表的结点都有前后指针。所以只需要修改指针的指向即可。不用再生成新的
// 结点。这道题目用二叉树的递归套路来做。定义一个递归函数f，需要传入一个二叉树的结点node，该函数f的作用是：将以node为根的树
// 调整成双向链表，并返回首尾结点。比如调用f(4)，那么返回的就是1,5；因为将4这棵树组织成有序双向链表是：1<=>2<=>3<=>4<=>5

public class BSTDoubleLinkedList {


    // 主方法
    public static TreeNode transform(TreeNode root) {
        if (root == null)
            return null;
        Info all = f(root);
        all.tail.right = all.head;  // 尾巴连头
        all.head.left = all.tail;   // 头连尾巴
        return all.head;
    }

    private static Info f(TreeNode root) {
        if (root == null)
            return new Info(null, null);
        Info left = f(root.left);
        Info right = f(root.right);
        if (left.tail != null) {
            left.tail.right = root;
        }
        // 下面的两句执行为什么不需要条件限制？因为即便指向了null也合理
        root.left = left.tail;
        root.right = right.head;
        if (right.head != null) {
            right.head.left = root;
        }
        return new Info(left.head == null ? root : left.head, right.tail == null ? root : right.tail);
    }

    public static class Info {
        public TreeNode head;
        public TreeNode tail;

        public Info(TreeNode head, TreeNode tail) {
            this.head = head;
            this.tail = tail;
        }
    }
}
