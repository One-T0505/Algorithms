package Basic.Tree.BinaryTree;

// 给你一个二叉树的根结点 root ，请返回出现次数最多的子树元素和。如果有多个元素出现的次数相同，返回所有出现次数
// 最多的子树元素和（不限顺序）。
// 一个结点的 「子树元素和」 定义为以该结点为根的二叉树上所有结点的元素之和（包括结点本身）。

import utils.TreeNode;
import java.util.HashMap;

public class _0508_Code {

    public HashMap<Integer, Integer> dp;
    public int maxTime;

    public int[] findFrequentTreeSum(TreeNode root) {
        if (root == null)
            return new int[0];
        dp = new HashMap<>();
        f(root);
        int count = 0;
        for (int val : dp.values()){
            if (val == maxTime){
                count++;
            }
        }
        int[] res = new int[count];
        int i = 0;
        for (int key : dp.keySet()) {
            if (dp.get(key) == maxTime)
                res[i++] = key;
        }
        return res;
    }

    private int f(TreeNode cur) {
        if (cur == null)
            return 0;
        int sum = cur.val + f(cur.left) + f(cur.right);
        dp.put(sum, dp.getOrDefault(sum, 0) + 1);
        maxTime = Math.max(maxTime, dp.get(sum));
        return sum;
    }
}
