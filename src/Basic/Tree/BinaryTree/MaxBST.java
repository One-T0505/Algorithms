package Basic.Tree.BinaryTree;

import utils.*;
import utils.BinaryTree;

public class MaxBST {
    // 给一个结点，找出其最大二叉排序树

    static class Info {
        public boolean isBST;    // 以当前结点为根的树是否为二叉排序树
        public int maxSubBSTSize;   //以当前结点为根的树的最大子二叉排序树的结点个数
        public int min;         // 以当前结点为根的树的最小值
        public int max;         // 以当前结点为根的树的最大值

        public Info(boolean isBST, int maxSubBSTSize, int min, int max) {
            this.isBST = isBST;
            this.maxSubBSTSize = maxSubBSTSize;
            this.min = min;
            this.max = max;
        }
    }

    // 该方法是在整合Info时碰到不确定的情况就返回null的处理流程
    public static Info maxSubBST(TreeNode root) {
        if (root == null)
            return null;
        Info left = maxSubBST(root.left);
        Info right = maxSubBST(root.right);

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
                (left == null || left.isBST) &&
                        (right == null || right.isBST) &&
                        // 并且左子树的最大值< 当前结点的值 < 右子树的最小值
                        (left == null || left.max < root.val) &&
                        (right == null || right.min > root.val)
        ) {
            maxSubBSTSize = (left == null ? 0 : left.maxSubBSTSize) +
                    (right == null ? 0 : right.maxSubBSTSize) + 1;
            isAllBST = true;
        }

        return new Info(isAllBST, maxSubBSTSize, min, max);
    }

    // 该方法是不管怎么样都要返回Info
    public static Info isB(TreeNode root){
        if (root == null)
            return new Info(true, 0, Integer.MAX_VALUE, Integer.MIN_VALUE);

        Info left = isB(root.left);
        Info right = isB(root.right);

        boolean isBST = left.isBST && right.isBST && root.val > left.max && root.val < right.min;
        int maxSubBSTSize = isBST ? left.maxSubBSTSize + right.maxSubBSTSize + 1 :
                Math.max(left.maxSubBSTSize, right.maxSubBSTSize);
        int min = Math.min(Math.min(left.min, right.min), root.val);
        int max = Math.max(Math.max(left.max, right.max), root.val);
        return new Info(isBST, maxSubBSTSize, min, max);
    }
    // --------------------------------------------------------------------

    // 上面的两个方法是用于找到一棵树的最大二叉搜索树的结点数量；现在稍微变化一下，给定一棵二叉树的头节点root,
    // 返回这颗二叉树中最大的二叉搜索子树的头节点
    static class Info2 {

        public TreeNode maxSubBSTHead;
        public int maxSubBSTSize;
        public int max;
        public int min;

        public Info2(TreeNode maxSubBSTHead, int maxSubBSTSize, int max, int min) {
            this.maxSubBSTHead = maxSubBSTHead;
            this.maxSubBSTSize = maxSubBSTSize;
            this.max = max;
            this.min = min;
        }
    }

    public static TreeNode maxBST(TreeNode root) {
        if (root == null)
            return null;
        return process(root).maxSubBSTHead;
    }

    private static Info2 process(TreeNode root) {
        if (root == null)
            return new Info2(null, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        Info2 leftInfo = process(root.left);
        Info2 rightInfo = process(root.right);

        int max = Math.max(Math.max(root.val, leftInfo.max), rightInfo.max);
        int min = Math.min(Math.min(root.val, leftInfo.min), rightInfo.min);
        boolean isBST = (leftInfo.maxSubBSTHead == root.left && rightInfo.maxSubBSTHead == root.right &&
                root.val > leftInfo.max && root.val < rightInfo.min);
        int maxSubBSTSize = isBST ? leftInfo.maxSubBSTSize + rightInfo.maxSubBSTSize + 1 :
                Math.max(leftInfo.maxSubBSTSize, rightInfo.maxSubBSTSize);

        TreeNode maxSubBSTHead = isBST ? root : (leftInfo.maxSubBSTSize >= rightInfo.maxSubBSTSize ?
                        leftInfo.maxSubBSTHead : rightInfo.maxSubBSTHead);
        return new Info2(maxSubBSTHead, maxSubBSTSize, max, min);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int[] array = arrays.noRepeatArr(20, 50);
            TreeNode root = BinaryTree.generateRandomBinaryTree(array);
            PrintTree.printT(root);
            System.out.println(maxBST(root) == null ? null : maxBST(root).val);
            System.out.println("====================================================================");
        }
    }


}
