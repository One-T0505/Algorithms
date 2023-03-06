package Tree.BinaryTree;

// 给你一个二叉树的根节点 root ，按任意顺序返回所有从根节点到叶子节点的路径。

import Tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class _0257_Code {

    public static List<String> binaryTreePaths(TreeNode root) {
        ArrayList<String> res = new ArrayList<>();
        dfs(root, "", res);
        return res;
    }

    private static void dfs(TreeNode x, String path, ArrayList<String> res) {
        if (x == null)
            return;
        StringBuilder sb = new StringBuilder(path);
        sb.append(x.val);
        if (x.left == null && x.right == null){
            res.add(sb.toString());
        } else {
            sb.append("->");
            dfs(x.left, sb.toString(), res);
            dfs(x.right, sb.toString(), res);
        }
    }


    public static void main(String[] args) {
        TreeNode r = new TreeNode(1);
        r.left = new TreeNode(2);
        r.right = new TreeNode(3);
        r.left.right = new TreeNode(5);
        List<String> res = binaryTreePaths(r);
        for (String path : res)
            System.out.println(path);
        System.out.println("23".length());
    }
}
