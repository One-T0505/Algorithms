package GreatOffer.TopHotQ;

/**
 * ymy
 * 2023/3/15 - 10 : 50
 **/

import utils.TreeNode;

// 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径
// 可能穿过也可能不穿过根结点。

public class _0543_DiameterOfBST {

    public static int diameterOfBinaryTree(TreeNode root) {
        if (root == null)
            return 0;
        return f(root).len - 1;
    }

    private static Info f(TreeNode cur) {
        if (cur == null)
            return new Info(0,0);
        Info left = f(cur.left);
        Info right = f(cur.right);

        int height = Math.max(left.height, right.height) + 1;
        int len = Math.max(left.height + right.height + 1, Math.max(left.len, right.len));

        return new Info(height, len);
    }


    public static class Info {
        public int height;
        public int len;

        public Info(int height, int len) {
            this.height = height;
            this.len = len;
        }
    }

}
