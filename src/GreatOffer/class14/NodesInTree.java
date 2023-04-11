package class14;


// 给定一个棵完全二叉树，返回这棵树的节点个数，要求时间复杂度小于O(树的节点数).

import utils.TreeNode;

public class NodesInTree {

    // 思路：在根结点上，找到其右子树上最左结点的高度看其是否等于根结点左子树最底的高度，如果相等，就说明
    //      根的左子树是满二叉树，如下图1所示；如果不等，则根的右子树是满的，但是是高度少1的满二叉树，如图2
    //      所示。不论如何，都可以确定有一边是满二叉，可根据公式直接算出结点，然后对另一边递归下去。
    //
    //                 ●                                    ●
    //              /     \                              /     \
    //             ●       ●                            ●       ●
    //           /  \     /  \                        /  \     /  \
    //          ●    ●    ●    ●                     ●    ●   ●    ●
    //        /  \  / \   /                        /  \  / \
    //       ●   ●  ●  ● ●                        ●   ●  ●  ●
    //
    //                图1                                  图2


    // 时间复杂度 O((logN)^2)
    public static int nodes(TreeNode root) {
        if (root == null)
            return 0;
        return bs(root, 1, depth(root, 1));
    }


    // 以cur为根，并且cur为根的树最大深度为depth，返回该树包含的结点数
    // 时间复杂度 O((logN)^2)  因为每下一层，都要遍历到最底一遍
    private static int bs(TreeNode cur, int curH, int depth) {
        if (curH == depth)
            return 1;
        int mostLeftOfRight = depth(cur.right, curH + 1);
        if (mostLeftOfRight == depth)
            // 本来应该是：1 +    (1 << (depth - curH)) - 1     前面是根结点，后面是满二叉的公式
            return (1 << (depth - curH)) + bs(cur.right, curH + 1, depth);
        else
            return (1 << (depth - curH - 1)) + bs(cur.left, curH + 1, depth);
    }


    // 以root为根，并且root处于curH的高度，返回其左子树最左结点的深度
    private static int depth(TreeNode cur, int curH) {
        while (cur.left != null) {
            curH++;
            cur = cur.left;
        }
        return curH;
    }

}
