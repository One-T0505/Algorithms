package Tree;

public class MaxDistance {
    // 求以某结点为根结点的子树的最大距离。两个结点的距离是从一个结点到另一个结点经过的结点数（包含头尾结点）。

    public Info2 maxDistance(TreeNode root) {
        if (root == null)
            return new Info2(0, 0);
        Info2 left = maxDistance(root.left);
        Info2 right = maxDistance(root.right);

        int height = Math.max(left.height, right.height) + 1;
        int maxDistance = (Math.max(Math.max(left.MaxDistance, right.MaxDistance),
                left.height + right.height + 1));
        return new Info2(maxDistance, height);
    }

    public int mainThread(TreeNode root) {
        return maxDistance(root).MaxDistance;
    }

}

class Info2 {
    public int MaxDistance;
    public int height;

    public Info2(int maxDistance, int height) {
        MaxDistance = maxDistance;
        this.height = height;
    }
}


