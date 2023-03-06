package Tree.BinaryTree;

// 给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
// 每条从根节点到叶节点的路径都代表一个数字：例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
// 计算从根节点到叶节点生成的所有数字之和。

import Tree.TreeNode;

public class _0129_Code {

    public static int sumNumbers(TreeNode root) {
        return dfs(root, 0);
    }


    // pre表示从根结点到当前结点cur，已经累计的累加和，不包含cur的数值 就是从根到cur的父结点的累加和
    private static int dfs(TreeNode cur, int pre) {
        if (cur == null)
            return 0;
        int sum = pre * 10 + cur.val;
        if (cur.left == null && cur.right == null)
            return sum;
        return dfs(cur.left, sum) + dfs(cur.right, sum);
    }

}
