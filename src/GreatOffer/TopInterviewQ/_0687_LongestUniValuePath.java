package GreatOffer.TopInterviewQ;

// 给定一个二叉树的 root ，返回最长的路径的长度，这个路径中的每个节点具有相同值。 这条路径可以经过也可以不经过根节点。
// 两个节点之间的路径长度由它们之间的边数表示。

import utils.TreeNode;

public class _0687_LongestUniValuePath {

    // 二叉树递归套路  可能性分析：
    //  1.与当前结点x无关  Math.max(左子树上的最大路径，右子树上的最大路径)
    //  2.与x有关   左子树的根向下找到的最大路径

    public static class Info {
        //在一条路径上:要求每个节点通过且只通过一遍
        public int len; // 路径必须从x出发且只能往下走的情况下，路径的最大距离
        public int max; // 路径不要求必須从x出发的情况下，整裸樹的合法路径最大距高

        public Info(int len, int max) {
            this.len = len;
            this.max = max;
        }
    }



    public static int longestUniValuePath(TreeNode root) {
        if (root == null)
            return 0;
        return f(root).max - 1;
    }



    private static Info f(TreeNode x) {
        if (x == null)
            return new Info(0, 0);
        // 左树上，不要求从左孩子出发的最大路径 和 必须从左孩子出发往下的最大路径
        Info left = f(x.left);
        // 右树上，不要求从右孩子出发的最大路径 和 必须从右孩子出发往下的最大路径
        Info right = f(x.right);

        // 加工出自己的两个信息 先搞定len
        int len = 1;
        if (x.left != null && x.left.val == x.val)
            len = left.len + 1;
        if (x.right != null && x.right.val == x.val)
            len = Math.max(len, right.len + 1);

        // 再搞定max
        int max = Math.max(Math.max(left.max, right.max), len);
        if (x.left != null && x.left.val == x.val && x.right != null && x.right.val == x.val)
            max = Math.max(max, left.len + right.len + 1);

        return new Info(len, max);
    }



}
