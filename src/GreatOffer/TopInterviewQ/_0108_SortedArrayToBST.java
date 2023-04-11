package GreatOffer.TopInterviewQ;


// 给你一个整数数组 nums ，其中元素已经按升序排列，请你将其转换为一棵高度平衡二叉搜索树。
// 高度平衡 二叉树是一棵满足「每个节点的左右两个子树的高度差的绝对值不超过 1 」的二叉树。

import utils.TreeNode;

public class _0108_SortedArrayToBST {

    public TreeNode sortedArrayToBST(int[] nums) {
        return f(nums, 0, nums.length - 1);
    }

    private TreeNode f(int[] nums, int L, int R) {
        if (L > R)
            return null;
        if (L == R)
            return new TreeNode(nums[L]);
        int mid = L + ((R - L) >> 1);
        TreeNode cur = new TreeNode(nums[mid]);
        cur.left = f(nums, L, mid - 1);
        cur.right = f(nums, mid + 1, R);

        return cur;
    }

}
