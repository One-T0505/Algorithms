package GreatOffer.TopInterviewQ;


// 判断一刻二叉树是否为镜像树

import utils.TreeNode;

public class _0101_SymmetricTree {

    public boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);
    }

    private boolean isMirror(TreeNode r1, TreeNode r2) {
        if (r1 == null && r2 == null)
            return true;
        if (r1 != null && r2 != null)
            return r1.val == r2.val && isMirror(r1.left, r2.right) && isMirror(r1.right, r2.left);

        return false;
    }

}
