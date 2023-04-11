package class05;


import utils.TreeNode;

// 如果一个节点X，它左树结构和右树结构完全一样，那么我们说以X为头的树是相等子树。
// 给定一棵二叉树的头节点root，返回root整棵树上有多少棵相等子树。
//
//            6
//          /   \
//         4     4                    最下面的4个叶结点都是的，因为2的左右子树都为null；两个以4为根的子树都不是；
//       /  \   /  \                  我们就拿左边那个4来看，其左右子树结点个数虽然相等都为1，但是不是一样的结点。
//      2    3  2   3                 以6为根的树是相等子树，因为其左右子树结构一样，并且数值相等。
//
public class SameSubTree {


    // 该方法的作用是：传入一个结点，返回以该结点为根的树有多少个相等子树
    public static int sameSubTreeV1(TreeNode root) {
        if (root == null)
            return 0;
        //    左子树的相等子树   右子树的相等子树
        return sameSubTreeV1(root.left) + sameSubTreeV1(root.right) +
                (isSame(root.left, root.right) ? 1 : 0); // 判断自己是不是相等子树
    }


    // 该方法的作用是：传入两个根结点，判断这两个子树的结构是否完全一样
    private static boolean isSame(TreeNode r1, TreeNode r2) {
        // 异或：相同为0，不同为1；如果返回结果为1，说明其中一个为null，一个不为null
        // 这个if就过滤掉了两种情况，一个为空，一个不为空
        if (r1 == null ^ r2 == null)
            return false;
        // 第三种情况，都为空
        if (r1 == null)
            return true;
        // 第四种情况：都不为空
        //    保证r1和r2的左子树结构相同  保证r1和r2的右子树结构相同  还得保证这两个根结点是否相等
        // 三者都成立，才能说r1和r2两棵树结构相同
        return isSame(r1.left, r2.left) && isSame(r1.right, r2.right) && r1.val == r2.val;
    }
    // =================================================================================================


    // 上面的方法时间复杂度为：O(NlogN). isSame方法对于越不平衡的树判断非常快，它的上界其实是最平衡的时候。
    // sameSubTreeV1(root.left) + sameSubTreeV1(root.right) + (isSame(root.left, root.right) ? 1 : 0);
    // 最平衡的时候，那左右子树都是现有规模的一半。根据Master公式：T(N) = 2 * T(N/2) + O(N)
    // 所以时间复杂度为：O(NlogN)
    // 下面的方法是优化后的，可达到O(1)。主要的优化在于比对两个子树的结构是否相同。但是要导入别的包。

}
