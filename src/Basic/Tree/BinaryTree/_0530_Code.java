package Basic.Tree.BinaryTree;

// 给你一个二叉搜索树的根节点 root ，返回树中任意两不同节点值之间的最小差值。
// 差值是一个正数，其数值等于两值之差的绝对值。

import utils.TreeNode;

public class _0530_Code {


    // 有了二叉搜索树这个限制，我们就知道了，对于任意一个结点cur，能和cur产生最小的差值的结点，必然是其左子树的最右结点
    // 或者右子树的最左结点，这样我们的尝试就有了很明确的目标，而不会让整棵树的结点都和cur去算差值。
    public int getMinimumDifference(TreeNode root) {
        if (root == null)
            return Integer.MAX_VALUE;
        int res = Integer.MAX_VALUE;
        if (root.left != null){
            TreeNode mostRight = root.left;
            while (mostRight.right != null){
                mostRight = mostRight.right;
            }
            res = Math.abs(root.val - mostRight.val);
        }
        if (root.right != null){
            TreeNode mostLeft = root.right;
            while (mostLeft.left != null){
                mostLeft = mostLeft.left;
            }
            res = Math.min(res, Math.abs(root.val - mostLeft.val));
        }

        return Math.min(res, Math.min(getMinimumDifference(root.left), getMinimumDifference(root.right)));
    }
}
