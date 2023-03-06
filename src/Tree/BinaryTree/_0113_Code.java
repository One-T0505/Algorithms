package Tree.BinaryTree;

// leetCode113  是第112题的升级版
// 给你二叉树的根节点 root 和一个整数目标和 targetSum ，找出所有从根节点到叶子节点路径总和等于给定目标和的路径。
// 叶子节点 是指没有子节点的节点。

import Tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class _0113_Code {

    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null)
            return res;
        ArrayList<Integer> path = new ArrayList<>();
        path.add(root.val);
        f(root, targetSum - root.val, path, res);
        return res;
    }

    private static void f(TreeNode root, int rest, ArrayList<Integer> path, List<List<Integer>> res) {
        if (root.left == null && root.right == null && rest == 0){
            res.add(new ArrayList<>(path));
        } else {
            if (root.left != null){
                path.add(root.left.val);
                f(root.left, rest - root.left.val, path, res);
                path.remove(path.size() - 1);
            }
            if (root.right != null){
                path.add(root.right.val);
                f(root.right, rest - root.right.val, path, res);
                path.remove(path.size() - 1);
            }
        }
    }
}
