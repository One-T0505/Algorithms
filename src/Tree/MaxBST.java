package Tree;

public class MaxBST {
    // 给一个结点，找出其最大二叉排序树
    public Info3 maxSubBST(TreeNode root) {
        if (root == null)
            return null;
        Info3 left = maxSubBST(root.left);
        Info3 right = maxSubBST(root.right);

        // 开始整合当前结点需要返回Info3中的4份信息
        int min = root.val, max = root.val;
        if (left != null) {
            min = Math.min(min, left.min);
            max = Math.max(max, left.max);
        }
        if (right != null) {
            min = Math.min(min, right.min);
            max = Math.max(max, right.max);
        }

        int maxSubBSTSize = 0;
        if (left != null)
            maxSubBSTSize = left.maxSubBSTSize;;
        if (right != null)
            maxSubBSTSize = Math.max(maxSubBSTSize, right.maxSubBSTSize);

        boolean isAllBST = false;
        if (
            // 左、右子树都需要整体是二叉排序树
                (left == null ? true : left.isBST) &&
                        (right == null ? true : right.isBST) &&
                        // 并且左子树的最大值< 当前结点的值 < 右子树的最小值
                        (left == null ? true : left.max < root.val) &&
                        (right == null ? true : right.min > root.val)
        ) {
            maxSubBSTSize = (left == null ? 0 : left.maxSubBSTSize) +
                    (right == null ? 0 : right.maxSubBSTSize) + 1;
            isAllBST = true;
        }

        return new Info3(isAllBST, maxSubBSTSize, min, max);
    }

}

class Info3 {
    public boolean isBST;
    public int maxSubBSTSize;   //最大子二叉排序树的结点个数
    public int min;
    public int max;

    public Info3(boolean isBST, int maxSubBSTSize, int min, int max) {
        this.isBST = isBST;
        this.maxSubBSTSize = maxSubBSTSize;
        this.min = min;
        this.max = max;
    }
}
