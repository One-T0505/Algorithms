package Tree.BinaryTree;

// 给你一个二叉树的根节点 root。设根节点位于二叉树的第 1 层，而根节点的子节点位于第 2 层，依此类推。
// 请返回层内元素之和最大的那几层（可能只有一层）的层号，并返回其中最小的那个。

import Tree.TreeNode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class _1161_Code {

    // 方法1 深度优先遍历
    public int maxLevelSum(TreeNode root) {
        int maxSum = Integer.MIN_VALUE;
        int level = Integer.MAX_VALUE;
        ArrayList<Integer> sum = new ArrayList<>();
        dfs(root, 1, sum);
        for(int i = 0; i < sum.size(); i++){
            int cur = sum.get(i);
            if(cur > maxSum){
                maxSum = cur;
                level = i;
            }
        }
        return level + 1;
    }


    private void dfs(TreeNode x, int level, ArrayList<Integer> sum){
        if(x == null)
            return;
        if(level <= sum.size()){
            sum.set(level - 1, sum.get(level - 1) + x.val);
        } else {
            sum.add(x.val);
        }
        dfs(x.left, level + 1, sum);
        dfs(x.right, level + 1, sum);
    }





    // 方法2  宽度优先遍历
    public int maxLevelSum2(TreeNode root) {
        int maxSum = Integer.MIN_VALUE;
        int level = 0;
        int levelSum = 0;
        int curLevel = 1;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode poll = queue.poll();
                if (poll.left != null){
                    queue.add(poll.left);
                }
                if (poll.right != null){
                    queue.add(poll.right);
                }
                levelSum += poll.val;
            }
            if (levelSum > maxSum){
                maxSum = levelSum;
                level = curLevel;
            }
            levelSum = 0;
            curLevel++;
        }
        return level;
    }
}
