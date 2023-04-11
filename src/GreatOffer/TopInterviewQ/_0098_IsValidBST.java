package GreatOffer.TopInterviewQ;


// 给你一个二叉树的根节点 root ，判断其是否是一个有效的二叉搜索树。
// 有效 二叉搜索树定义如下：
//   节点的左子树只包含 小于 当前节点的数。
//   节点的右子树只包含 大于 当前节点的数。
//   所有左子树和右子树自身必须也是二叉搜索树。

import java.util.ArrayList;
import utils.TreeNode;

public class _0098_IsValidBST {

    // 常规方法
    public boolean isValidBST(TreeNode root) {
        ArrayList<Integer> inOrder = new ArrayList<>();
        f(root, inOrder);
        for (int i = 1; i < inOrder.size(); i++) {
            if (inOrder.get(i) <= inOrder.get(i - 1))
                return false;
        }
        return true;
    }

    private void f(TreeNode cur, ArrayList<Integer> inOrder) {
        if (cur != null) {
            f(cur.left, inOrder);
            inOrder.add(cur.val);
            f(cur.right, inOrder);
        }
    }
    // ==========================================================================================





    // 二叉树递归套路解法
    public static boolean isValidBST2(TreeNode root) {
        if(root == null)
            return true;
        return f(root).isBST;
    }


    private static Info f(TreeNode cur){
        if(cur == null)
            return null;
        Info left = f(cur.left);
        Info right = f(cur.right);
        int max = cur.val;
        int min = cur.val;
        boolean isBST = true;
        if (left != null){
            max = Math.max(max, left.max);
            min = Math.min(min, left.min);
            isBST &= cur.val > left.max;
        }
        if (right != null){
            max = Math.max(max, right.max);
            min = Math.min(min, right.min);
            isBST &= cur.val < right.min;
        }
        isBST &= (left == null || left.isBST) && (right == null || right.isBST);
        return new Info(max, min, isBST);
    }


    public static class Info {
        public int max;
        public int min;
        public boolean isBST;

        public Info (int ma, int mi, boolean isB){
            max = ma;
            min =mi;
            isBST = isB;
        }
    }
    // ----------------------------------------------------------------------------------------------






    // Morris遍历解法
    public static boolean morris(TreeNode root){
        if (root == null)
            return true;
        boolean res = true;
        TreeNode cur = root;
        TreeNode pre = null;
        while (cur != null){
            if (cur.left != null){
                TreeNode mostRight = cur.left;
                while (mostRight.right != null && mostRight.right != cur)
                    mostRight = mostRight.right;
                if (mostRight.right == null){
                    mostRight.right = cur;
                    cur = cur.left;
                } else {
                    if (cur.val <= mostRight.val)
                        res = false;
                    mostRight.right = null;
                    pre = cur;
                    cur = cur.right;
                }
            } else {
                if (pre != null && cur.val <= pre.val){
                    res = false;
                }
                pre = cur;
                cur = cur.right;
            }
        }
        return res;
    }




    public static void main(String[] args) {
        TreeNode root = new TreeNode(32);
        root.left = new TreeNode(26);
        root.right = new TreeNode(47);
        root.left.left = new TreeNode(19);
        root.left.left.right = new TreeNode(27);
        root.right.right = new TreeNode(56);
        System.out.println(isValidBST2(root));
    }
}
