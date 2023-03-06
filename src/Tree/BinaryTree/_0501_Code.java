package Tree.BinaryTree;

// 给你一个含重复值的二叉搜索树（BST）的根节点 root ，找出并返回 BST 中的所有众数（即，出现频率最高的元素）。
// 如果树中有不止一个众数，可以按任意顺序返回。
// 假定 BST 满足如下定义：
//  1.结点左子树中所含节点的值 小于等于 当前节点的值
//  2.结点右子树中所含节点的值 大于等于 当前节点的值
//  3.左子树和右子树都是二叉搜索树

import Tree.TreeNode;
import java.util.ArrayList;

public class _0501_Code {

    // 传统方法就是递归遍历+哈希表记录词频  但这个方法就没有利用二叉搜索树的特性
    // 既然这棵数包含了重复结点，那么中序遍历的序列下，所有的重复的数字应该都扎堆在一起了，
    // 我们只需要遍历这个序列，然后用有限几个变量就能更新出结果了。当然我们遍历这个序列是在递归时进行的，不是先完整
    // 递归一遍写出序列再去找答案，而是一边遍历一边记录答案。


    // 用于存放多个出现次数最多的数，因为众数可能不止一个
    public ArrayList<Integer> list = new ArrayList<>();

    public int pre = Integer.MAX_VALUE;  // 上一个不同的数值
    public int count;  // 当前数字已经出现了多少次

    public int MaxCount;  // 遍历过的数中，出现最多的数的次数

    public int[] findMode(TreeNode root) {
        if (root == null)
            return null;
        inOrder(root);
        int[] res = new int[list.size()];
        int i = 0;
        for (int e : list)
            res[i++] = e;
        return res;
    }

    private void inOrder(TreeNode x) {
        if (x == null)
            return;
        inOrder(x.left);
        // 左子树已经遍历过了  当前遍历到了x这个数，需要对之前已经遍历过的数留下的记录更新了
        if (x.val == pre)
            count++;
        else {
            pre = x.val;
            count = 1;
        }
        if (count == MaxCount){
            list.add(pre);
        }
        if (count > MaxCount){
            MaxCount = count;
            list.clear();
            list.add(pre);
        }
        inOrder(x.right);
    }
}
