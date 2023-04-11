package GreatOffer.TopHotQ;

import utils.TreeNode;

// 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。

public class _0226_InvertBinaryTree {

    public TreeNode invertTree(TreeNode root) {
        if (root == null)
            return null;
        TreeNode left = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(left);
        return root;
    }

}
