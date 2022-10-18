package Tree.BinaryTree;

import Tree.TreeNode;

public class MaxDistance {
    // 求以某结点为根结点的子树的最大距离。两个结点的距离是从一个结点到另一个结点经过的结点数（包含头尾结点）。

    static class Info {
        public int maxDistance;
        public int height;

        public Info(int maxDistance, int height) {
            this.maxDistance = maxDistance;
            this.height = height;
        }
    }

    public Info maxDistance(TreeNode root) {
        if (root == null)
            return new Info(0, 0);
        Info left = maxDistance(root.left);
        Info right = maxDistance(root.right);

        int height = Math.max(left.height, right.height) + 1;
        int maxDistance = (Math.max(Math.max(left.maxDistance, right.maxDistance),
                left.height + right.height + 1));
        return new Info(maxDistance, height);
    }

    public int mainThread(TreeNode root) {
        return maxDistance(root).maxDistance;
    }

}




