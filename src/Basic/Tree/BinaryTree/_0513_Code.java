package Basic.Tree.BinaryTree;

// 给定一个二叉树的根节点 root，请找出该二叉树的最底层最左边节点的值。
// 假设二叉树中至少有一个节点。

import utils.TreeNode;

public class _0513_Code {

    public static class Info {
        public int depth;
        public TreeNode mostLeft;

        public Info(int depth, TreeNode mostLeft) {
            this.depth = depth;
            this.mostLeft = mostLeft;
        }
    }

    public static int findBottomLeftValue(TreeNode root) {
        return dfs(root).mostLeft.val;
    }

    private static Info dfs(TreeNode x) {
        if (x == null)
            return new Info(0, null);
        Info left = dfs(x.left);
        Info right = dfs(x.right);
        int depth = Math.max(left.depth, right.depth) + 1;
        TreeNode mostLeft = left.depth >= right.depth ? left.mostLeft : right.mostLeft;
        // 什么情况下会中下面的if？ x为叶结点的时候，收上来的左右信息里的mostLeft都是null
        // 但是不可能当前结点也把mostLeft设置成null向上传吧。
        if(mostLeft == null)
            mostLeft = x;
        return new Info(depth, mostLeft);
    }
}
