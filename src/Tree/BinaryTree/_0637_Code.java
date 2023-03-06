package Tree.BinaryTree;

// leetCode637  也是一个关于层次遍历的问题
// 给定一个非空二叉树的根节点 root , 以数组的形式返回每一层节点的平均值。与实际答案相差 10-5 以内的答案可以被接受。

import Tree.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class _0637_Code {
    
    
    // 用队列做层级遍历
    public static List<Double> averageOfLevels(TreeNode root) {
        ArrayList<Double> res = new ArrayList<>();
        if (root == null)
            return res;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            double sum = 0;
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.poll();
                sum += cur.val;
                if (cur.left != null)
                    queue.add(cur.left);
                if (cur.right != null)
                    queue.add(cur.right);
            }
            res.add(sum / size);
        }
        return res;
    }
    
    
    
    // 用深度优先遍历做
    public static List<Double> averageOfLevels2(TreeNode root) {
        ArrayList<Double> res = new ArrayList<>();
        if (root == null)
            return res;
        // sums[i]表示第i层上所有节点的累加和  根结点在第0层
        ArrayList<Long> sums = new ArrayList<>();
        // counts[i]表示第i层上所有结点的数量  根结点在第0层
        ArrayList<Integer> counts = new ArrayList<>();
        dfs(root, 0, sums, counts);
        for (int i = 0; i < sums.size(); i++) {
            res.add((double) sums.get(i) / counts.get(i));
        }
        return res;
    }

    private static void dfs(TreeNode root, int level, ArrayList<Long> sums,
                            ArrayList<Integer> counts) {
        if (root == null)
            return;
        if (level < sums.size()){
            sums.set(level, sums.get(level) + root.val);
            counts.set(level, counts.get(level) + 1);
        } else {
            sums.add((long) root.val);
            counts.add(1);
        }
        dfs(root.left, level + 1, sums, counts);
        dfs(root.right, level + 1, sums, counts);
    }


    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        List<Double> res = averageOfLevels2(root);
        for (double a : res)
            System.out.print(a + " ");
        System.out.println();
    }
}
