package Tree.BinaryTree;

// 给定一个二叉树，找出其最小深度。最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
// 说明：叶子节点是指没有子节点的节点。

import Tree.TreeNode;

public class _0111_Code {

    public static int minDepth(TreeNode root) {
        if(root == null)
            return 0;
        int depth = 1;
        // 如果左右子树仅有一个为空，那么应该返回另一个不为空的子树的最小深度
        // 这里不能采取下面的取较小值的办法，因为如果取较小值，那么会返回0，因为有一个子树为空。
        // 实际上，当一个为空一个不为空的时候，我们应该算不为空的那边
        if(root.left == null ^ root.right == null)
            depth += root.left == null ? minDepth(root.right) : minDepth(root.left);
        else // 两个子树都为空 或者 两个子树都不为空   就是正常的比较两者的较小值
            depth += Math.min(minDepth(root.left), minDepth(root.right));
        return depth;
    }
}
