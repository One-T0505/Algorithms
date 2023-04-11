package Basic.Tree.BinaryTree;

import utils.TreeNode;

public class IsFullBinaryTree {

    static class Info {
        public int height; // 以当前结点为根的树的高度
        public int nodes;  // 以当前结点为根的树的结点总数

        public Info(int height, int nodes) {
            this.height = height;
            this.nodes = nodes;
        }
    }

    // 判断一棵树是否是满二叉树,下面是用的树型递归套路做的，当然也可以用非递归方法实现
    public static boolean isFBT(TreeNode root) {
        if (root == null)
            return true;
        Info res = process(root);
        return 1 << res.height - 1 == res.nodes;
    }

    private static Info process(TreeNode root) {
        if (root == null)
            return new Info(0, 0);
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);
        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        int nodes = leftInfo.nodes + rightInfo.nodes + 1;
        return new Info(height, nodes);
    }

}
