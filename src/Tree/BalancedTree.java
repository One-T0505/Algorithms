package Tree;

public class  BalancedTree {
    // 判断一棵树是否为平衡二叉树
    public Info isBalancedTree(TreeNode root) {
        if (root == null)
            return new Info(true, 0);
        Info left = isBalancedTree(root.left);
        Info right = isBalancedTree(root.right);

        int height = Math.max(left.height, right.height) + 1;
        boolean flag = true;
        if (!left.isBalanced || !right.isBalanced || Math.abs(left.height - right.height) > 1)
            flag = false;
        return new Info(flag, height);
    }

    public boolean mainThread(TreeNode root) {
        return isBalancedTree(root).isBalanced;
    }
}

class Info {
    public boolean isBalanced;
    public int height;

    public Info(boolean isBalanced, int height) {
        this.isBalanced = isBalanced;
        this.height = height;
    }
}


