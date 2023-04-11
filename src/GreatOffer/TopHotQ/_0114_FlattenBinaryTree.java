package GreatOffer.TopHotQ;

import utils.TreeNode;

// 给你二叉树的根结点 root ，请你将它展开为一个单链表：
// 展开后的单链表应该同样使用 TreeNode ，其中 right 指针指向链表中下一个结点，而左子指针始终为 null 。
// 展开后的单链表应该与二叉树 先序遍历 顺序相同。

public class _0114_FlattenBinaryTree {

    // 比如给定的二叉树如下图所示，希望转换成如下右图所示
    //
    //        1
    //       / \
    //      2   5        1 -> 2 -> 3 -> 4 -> 5 -> 6
    //     / \   \
    //    3   4   6
    //


    // 常规解法  设计一个递归函数f(x)，该方法将传入的以x为根结点的树，x上面的不用管，只用把
    // x这棵子树转换成单链表，并返回串好后的头尾结点
    private static Info f(TreeNode x) {
        if (x == null)
            return null;
        Info left = f(x.left);
        Info right = f(x.right);
        x.left = null;
        x.right = left == null ? null : left.head;
        TreeNode tail = left == null ? x : left.tail;
        tail.right = right == null ? null : right.head;
        tail = right == null ? tail : right.tail;
        return new Info(x, tail);
    }

    public void flatten(TreeNode root) {
        f(root);
    }

    public static class Info {
        public TreeNode head;
        public TreeNode tail;

        public Info(TreeNode head, TreeNode tail) {
            this.head = head;
            this.tail = tail;
        }
    }
    // ==========================================================================================


    // 优化解法：morris遍历做
}
