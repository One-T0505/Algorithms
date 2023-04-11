package Basic.Tree.BinaryTree;

// leetCode95题  是96题的升级版
// 给你一个整数n，请你生成并返回所有由 n 个节点组成且节点值从 1 到 n互不相同的不同二叉搜索树。
// 可以按任意顺序返回答案。

import utils.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class _0095_Code {

    public List<TreeNode> generateTrees(int n) {
        if (n == 0)
            return new ArrayList<>();
        return f(1, n);
    }

    private List<TreeNode> f(int L, int R) {
        LinkedList<TreeNode> all = new LinkedList<>();
        if (L > R){
            all.add(null);
            return all;
        }
        for (int i = L; i <= R; i++) {
            List<TreeNode> left = f(L, i - 1);
            List<TreeNode> right = f(i + 1, R);
            for (TreeNode l : left){
                for (TreeNode r : right){
                    TreeNode root = new TreeNode(i);
                    root.left = l;
                    root.right = r;
                    all.add(root);
                }
            }
        }
        return all;
    }
}
