package GreatOffer.TopInterviewQ;


// 路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中至多
// 出现一次。该路径至少包含一个节点，且不一定经过根节点。路径和是路径中各节点值的总和。
// 给你一个二叉树的根节点 root，返回其最大路径和。

import utils.TreeNode;

public class _0124_MaxPathSum {

    // 这是一道二叉树递归套路的题。需要设计一个Info

    public static int maxPathSum(TreeNode root) {
        if (root == null)
            return 0;
        return f(root).maxSum;
    }

    private static Info f(TreeNode x) {
        if (x == null)
            return null;

        Info left = f(x.left);
        Info right = f(x.right);

        // 以x为根的子树，从根能找到的最大路径和有三种情况：1> 只包含自己 x.val
        // 2> 可以吸纳左树  3> 可以吸纳右树
        int fromHead = x.val;
        if (left != null)
            fromHead = Math.max(fromHead, x.val + left.fromHead);
        if (right != null)
            fromHead = Math.max(fromHead, x.val + right.fromHead);

        // maxSum表示以x为根的树内最大的路径和，限制比fromHead少，所以情况也多一些
        // 1> 只包含自己 x.val
        // 2> 不包含x，仅包含左树的最大路径和
        // 3> 不包含x，仅包含右树的最大路径和
        // 4> 包含x，且仅包含左树           5> 包含x，且仅包含右树
        // 6> 包含x，且包含左树和右树

        int maxSum = x.val;   // 情况1
        // 情况2
        if (left != null)
            maxSum = Math.max(maxSum, left.maxSum);
        // 情况3
        if (right != null)
            maxSum = Math.max(maxSum, right.maxSum);
        // 情况4和情况5其实就是经过处理后，fromHead的值
        maxSum = Math.max(maxSum, fromHead);

        // 情况6
        if (left != null && right != null && left.fromHead > 0 && right.fromHead > 0)
            maxSum = Math.max(maxSum, x.val + left.fromHead + right.fromHead);

        return new Info(maxSum, fromHead);

    }

    public static class Info {
        public int maxSum;   // 以当前结点为根的树的最大路径和，不一定要经过根结点
        public int fromHead; // 一定要从根结点出发找到的最大路径和

        public Info(int maxSum, int fromHead) {
            this.maxSum = maxSum;
            this.fromHead = fromHead;
        }
    }
}
