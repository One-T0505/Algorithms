package GreatOffer.TopHotQ;

import utils.TreeNode;

/**
 * ymy
 * 2023/3/16 - 16 : 42
 **/

// 给出二叉搜索树的根节点，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），使每个节
// 点 node 的新值等于原树中大于或等于 node.val 的值之和。

public class _0538_GreaterSumTree {


    // 利用morris 遍历来改写，改写成对称形式，按照右根左的顺序完成。 经典的morris遍历是按照左根右的顺序来遍历的。
    public static TreeNode convertBST(TreeNode root) {
        if (root == null)
            return null;
        TreeNode cur = root;
        TreeNode pre = null;
        while (cur != null){
            if (cur.right != null) {  // 若cur有右子树
                TreeNode mostLeft = cur.right;
                // 找到cur的右子树上真实的最左结点
                while (mostLeft.left != null && mostLeft.left != cur)
                    mostLeft = mostLeft.left;
                // 从while出来后，mostLeft一定是cur右子树的最左结点
                if (mostLeft.left == null){
                    mostLeft.left = cur;
                    cur = cur.right;
                } else {  // mostLeft.left == cur
                    mostLeft.left = null;
                    cur.val += pre.val;
                    pre = cur;
                    cur = cur.left;
                }
            } else { // cur没有右子树
                cur.val += pre == null ? 0 : pre.val;
                pre = cur;
                cur = cur.left;
            }
        }
        return root;
    }



    // 方法2 利用二叉树递归来完成

    public int pre = 0;

    public TreeNode convertBST2(TreeNode root) {
        if (root == null)
            return null;
        return f(root);
    }

    private TreeNode f(TreeNode cur) {
        if (cur == null)
            return null;
        cur.right = f(cur.right);
        cur.val += pre;
        pre = cur.val;
        cur.left = f(cur.left);
        return cur;
    }


    public static void main(String[] args) {

    }
}
