package Basic.Tree.BinaryTree;

// leetCode515  这也是一个关于二叉树层序遍历的题目
// 给定一棵二叉树的根节点 root ，请找出该二叉树中每一层的最大值。

import utils.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class _0515_Code {

    // 方法1  传统的队列+BFS
    public List<Integer> largestValues(TreeNode root) {
        ArrayList<Integer> res = new ArrayList<>();
        if (root == null)
            return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.poll();
                max = Math.max(max, cur.val);
                if (cur.left != null)
                    queue.add(cur.left);
                if (cur.right != null)
                    queue.add(cur.right);
            }
            res.add(max);
        }
        return res;
    }



    // 方法2  DFS
    public List<Integer> largestValues2(TreeNode root) {
        ArrayList<Integer> res = new ArrayList<>();
        if (root == null)
            return res;
        dfs(root, 0, res);
        return res;
    }

    private void dfs(TreeNode root, int level, ArrayList<Integer> res) {
        if (root == null)
            return;
        if (level < res.size())
            res.set(level, Math.max(res.get(level), root.val));
        else
            res.add(root.val);
        dfs(root.left, level + 1, res);
        dfs(root.right, level + 1, res);
    }
}
