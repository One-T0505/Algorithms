package Basic.Tree.BinaryTree;


import utils.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class IsCompleteBinaryTree {

    // 判断一棵树是否为完全二叉树，我们知道，如果一棵树的结点数量为N，则其后半部分⎡N/2⎤ 的结点全都是叶结点。
    // 根据这条性质，层次遍历时发现某个结点违反下面两条原则中任意一条，就可以断定该树不是完全二叉树：
    // 1.任意一个结点如果没有左子树而有右子树
    // 2.如果该结点位于后半部分⎡N/2⎤，则必须无左右孩子

    public static boolean isCBTv1(TreeNode root) {
        if (root == null)
            return true;  // 一般认为空树也是完全二叉树
        boolean isLeaf = false;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            TreeNode leftChild = cur.left;
            TreeNode rightChild = cur.right;
            if ((isLeaf && (leftChild != null || rightChild != null)) ||
                    (leftChild == null && rightChild != null))
                return false;
            if (leftChild != null)
                queue.add(leftChild);
            if (rightChild != null)
                queue.add(rightChild);
            // 当碰到第一个不是左右孩子都有的结点时，就可以把isLeaf这个开关打开，表示其后面的结点都应该是叶结点
            if (leftChild == null || rightChild == null)
                isLeaf = true;
        }
        return true;
    }

    // 现在用树形dp套路来解决这个问题。同样地，我们要先列可能性，假如来到了一个结点x，应该怎么分情况呢？
    // 1.其左子树和右子树均为满二叉树，并且其高度一样              ●
    //                                                    /  \
    //                                                   ●    ●
    //                                                  / \  / \
    //                                                 ●  ●  ●  ●
    //
    // 2.左子树是完全二叉树，右子树是满二叉树，此时左树高度比右树高度大1             ●
    //                                                                   /   \
    //                                                                  ●     ●
    //                                                                 / \   / \
    //                                                                ●   ● ●   ●
    //                                                               /
    //                                                              ●
    //
    // 3.左子树和右子树均为满二叉树，但左树高度比右树高度大1             ●
    //                                                         /    \
    //                                                        ●      ●
    //                                                       / \    / \
    //                                                      ●   ●  ●   ●
    //                                                     / \ / \
    //                                                    ●  ● ●  ●
    //
    // 4.左树是满二叉树，并且有一部分已经到了右子树，使其右子树为                  ●
    //   完全二叉树，此时左树高度等于右树高度                                /    \
    //                                                                ●      ●
    //                                                              /   \   /
    //                                                             ●     ● ●
    //
    // 根据以上四种情况，我们需要的信息为：左右子树是否为满二叉树，左右子树是否为完全二叉树，左右子树高度

    static class Info {
        public boolean isFull;
        public boolean isComplete;
        public int height;

        public Info(boolean isFull, boolean isComplete, int height) {
            this.isFull = isFull;
            this.isComplete = isComplete;
            this.height = height;
        }
    }

    public static boolean isCBTv2(TreeNode root) {
        if (root == null)
            return true;
        return process(root).isComplete;
    }

    private static Info process(TreeNode root) {
        if (root == null)
            return new Info(true, true, 0);
        Info leftInfo = process(root.left);
        Info rightInfo = process(root.right);

        int height = Math.max(leftInfo.height, rightInfo.height) + 1;
        // 情况1
        boolean isFull = leftInfo.isFull && rightInfo.isFull && leftInfo.height == rightInfo.height;
        boolean isComplete =
                // 情况2
                (leftInfo.isComplete && rightInfo.isFull && leftInfo.height - 1 == rightInfo.height) ||
                // 情况3
                (leftInfo.isFull && rightInfo.isFull && leftInfo.height - 1 == rightInfo.height) ||
                // 情况4
                (leftInfo.isFull && rightInfo.isComplete && leftInfo.height == rightInfo.height);

        return new Info(isFull, isComplete, height);
    }


}
