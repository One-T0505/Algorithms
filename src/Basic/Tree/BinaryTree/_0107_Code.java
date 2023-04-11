package Basic.Tree.BinaryTree;

// leetCode107  这是一个关于二叉树层序遍历的题
// 给你二叉树的根节点 root ，返回其节点值自底向上的层序遍历 。即：按最底层到最高层的顺序输出
// 但是每层内部的顺序依然是从左至右

import utils.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class _0107_Code {

    // 方法1  常规做法   用队列
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        LinkedList<List<Integer>> res = new LinkedList<>();
        if (root == null)
            return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            int layerSize = queue.size();
            ArrayList<Integer> layer = new ArrayList<>();
            for (int i = 0; i < layerSize; i++) {
                TreeNode cur = queue.poll();
                layer.add(cur.val);
                if (cur.left != null)
                    queue.add(cur.left);
                if (cur.right != null)
                    queue.add(cur.right);
            }
            res.addFirst(layer);
        }
        return res;
    }



    // 方法2  比较新颖  是用DFS深度优先遍历做的
    public List<List<Integer>> levelOrderBottom2(TreeNode root) {
        LinkedList<List<Integer>> res = new LinkedList<>();
        if (root == null)
            return res;
        dfs(root, 0, res);
        return res;
    }


    // 默认根结点的深度为0
    private void dfs(TreeNode cur, int depth, LinkedList<List<Integer>> res) {
        if (cur == null)
            return;
        // depth == res.size()  说明是新的一层了  需要新加入一个列表
        if (depth == res.size())
            res.addFirst(new ArrayList<>());
        // res.size() - 1 - depth 是根据题目要求通过下标转换算出的，比较简单
        res.get(res.size() - 1 - depth).add(cur.val);
        // 然后去下面收集答案
        dfs(cur.left, depth + 1, res);
        dfs(cur.right, depth + 1, res);
    }
}
