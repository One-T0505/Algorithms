package Tree;

// 一棵二叉树中，任意给出两个结点，找出其第一个公共祖先结点。
// 给出两个结点a，b，x表示一棵树的根结点，有可能是整棵树的根，也有可能是递归过程中遇到的某棵子树的根
// 分情况讨论：
// 1.与x无关（x不是最低汇聚点）
//   >1.在x的左子树上汇聚   >2.在x的右子树上汇聚  >3.在以x为根的树上，找不全a，b两个结点
// 2.与x有关（x是最低汇聚点）
//   1>.a，b分别在左右子树上，一边有一个    2>.x就是a结点，而b在左右子树上   3>.x就是b结点，而a在左右子树上

// 经过初步分析，可以列举出需要的信息：1.在树上是否发现a; 2.在树上是否发现b; 3.在树上是否找到了最低汇聚点

import java.util.HashMap;
import java.util.HashSet;

public class CommonAncestor {
    // 方法1是初级版本，利用容器，属于是暴力解法，可以当作对数器
    public static TreeNode commonAncestorV1(TreeNode root, TreeNode a, TreeNode b) {
        if (root == null)
            return null;
        // 该map用于记录一个结点的父结点
        HashMap<TreeNode, TreeNode> map = new HashMap<>();
        map.put(root, null);
        fillParent(root, map);

        HashSet<TreeNode> set = new HashSet<>();
        TreeNode cur = a;
        set.add(cur);
        // 将o1结点以及其所有的祖先结点全部加入到set中
        while (map.get(cur) != null) {
            cur = map.get(cur);
            set.add(cur);
        }
        cur = b;
        while (!set.contains(cur)) {
            cur = map.get(cur);
        }
        return cur;
    }

    private static void fillParent(TreeNode root, HashMap<TreeNode, TreeNode> map) {
        if (root.left != null) {
            map.put(root.left, root);
            fillParent(root.left, map);
        }
        if (root.right != null)  {
            map.put(root.right, root);
            fillParent(root.right, map);
        }
    }

    // 现在用树形dp及递归套路来解决
    public static TreeNode commonAncestorV2(TreeNode root, TreeNode a, TreeNode b) {
        if (root == null)
            return null;
        return process(root, a, b).res ;
    }

    private static Info process(TreeNode root, TreeNode a, TreeNode b) {
        if (root == null)
            return new Info(false, false, null);
        Info leftInfo = process(root.left, a, b);
        Info rightInfo = process(root.right, a, b);

        boolean isFindA = leftInfo.isFoundA || rightInfo.isFoundA || root == a;
        boolean isFindB = leftInfo.isFoundB || rightInfo.isFoundB || root == b;

        TreeNode res = null;
        if (leftInfo.res != null)
            res = leftInfo.res;
        else if (rightInfo.res != null)
            res = rightInfo.res;
        else {
            if (isFindA && isFindB)
                res = root;
        }
        return new Info(isFindA, isFindB, res);

    }

    static class Info {
        public boolean isFoundA;
        public boolean isFoundB;
        public TreeNode res;

        public Info(boolean A, boolean B, TreeNode res) {
            isFoundA = A;
            isFoundB = B;
            this.res = res;
        }
    }

    public static void main(String[] args) {
    }
}
