package Basic.Tree.BinaryTree;

// 给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。一个节点也可以是它自己的祖先。

import utils.TreeNode;

public class _0235_Code {

    // 这个和leetCode236 也就是commonAncestor文件讲的题目的区别在于，那个是普通二叉树，这个是二叉搜索树
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null)
            return null;
        int min = Math.min(p.val, q.val);
        int max = Math.max(p.val, q.val);
        while(root != null){
            if(root.val > max)
                root = root.left;
            else if(root.val < min)
                root = root.right;
            else
                // 剩余情况有两种: 1.root==min || root==max   2.min < root < max
                // 这两种情况都可以直接返回root
                return root;
        }
        return null;
    }
}
