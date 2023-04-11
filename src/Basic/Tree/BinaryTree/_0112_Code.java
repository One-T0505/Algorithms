package Basic.Tree.BinaryTree;

// 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。判断该树中是否存在根节点到叶子节点的路径，
// 这条路径上所有节点值相加等于目标和 targetSum 。如果存在，返回 true ；否则，返回 false。
// 叶子节点是指没有子节点的节点。

import utils.TreeNode;

public class _0112_Code {


    // 注意：找到的这条路径的终点必须是叶子结点，不可以是非终端结点
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null)
            return false;
        if (root.left == null && root.right == null && root.val == targetSum)
            return true;
        return hasPathSum(root.left, targetSum - root.val) ||
                hasPathSum(root.right, targetSum - root.val);
    }
}
