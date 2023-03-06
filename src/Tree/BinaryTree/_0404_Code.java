package Tree.BinaryTree;

// 给定二叉树的根节点 root ，返回所有左叶子之和。

import Tree.TreeNode;

import java.util.*;

public class _0404_Code {

    public static int sumOfLeftLeaves(TreeNode root) {
        if (root == null || (root.left == null && root.right == null))
            return 0;
        return f(root, null, 0);
    }


    // x表示当前来到的结点  parent表示x的父结点  pre表示已经积攒的左叶子之和
    private static int f(TreeNode x, TreeNode parent, int pre) {
        if (x == null)
            return 0;
        if (x.left == null && x.right == null && parent.left == x)
            return pre + x.val;
        return f(x.left, x, pre) + f(x.right, x, pre);
    }

}
