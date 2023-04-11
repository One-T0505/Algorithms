package Basic.Tree.BinaryTree;

// leetCode110
// 判断一棵树是否为平衡二叉树  这里不需要判断是否满足是二叉排序数，只需要关注高度是否满足即可。

import utils.TreeNode;

public class IsBalancedTree {

    static class Info {
        public boolean isBalanced;
        public int height;

        public Info(boolean isBalanced, int height) {
            this.isBalanced = isBalanced;
            this.height = height;
        }
    }

    public Info process(TreeNode root) {
        if (root == null)
            return new Info(true, 0);
        Info left = process(root.left);
        Info right = process(root.right);
        int height = Math.max(left.height, right.height) + 1;
        boolean balanced = left.isBalanced && right.isBalanced &&
                Math.abs(left.height - right.height) < 2;
        return new Info(balanced, height);
    }
    public boolean isBalancedV1(TreeNode root) {
        if (root == null)
            return true;
        return process(root).isBalanced;
    }

}
