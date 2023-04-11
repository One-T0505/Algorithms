package LeetCode;

import utils.TreeNode;

/**
 * ymy
 * 2023/4/3 - 11 : 40
 **/


// leetCode654
// 给定一个不重复的整数数组 nums 。 最大二叉树 可以用下面的算法从 nums 递归地构建:
//  1.创建一个根节点，其值为 nums 中的最大值。
//  2.递归地在最大值 左边 的 子数组前缀上 构建左子树。
//  3.递归地在最大值 右边 的 子数组后缀上 构建右子树。
// 返回 nums 构建的 最大二叉树 。

public class MaxBinaryTree {

    // 最基本的递归解法就不写了，下面的是用线段树进行优化后的结果
    // 我们可以继续在寻找最大值这件事上面优化，比如可以使用线段树来优化。
    // 但是一定要注意下标变化，线段树里是从1开始的，而dfs的L、R边界都是从0开始的
    public static TreeNode constructMaximumBinaryTree(int[] nums) {
        if(nums == null || nums.length == 0)
            return null;
        int N = nums.length;
        SegmentTree st = new SegmentTree(nums);
        st.build(1, N, 1);
        return dfs(nums, 0, N - 1, st);
    }


    private static TreeNode dfs(int[] nums, int L, int R, SegmentTree st){
        if(L > R)
            return null;
        if(L == R)
            return new TreeNode(nums[L]);
        int pos = st.query(L + 1, R + 1);
        // 用线段树查询出来的pos是从1开始的
        TreeNode root = new TreeNode(nums[pos - 1]);
        root.left = dfs(nums, L, pos - 2, st);
        root.right = dfs(nums, pos, R, st);
        return root;
    }




    public static class SegmentTree {
        public int N;
        public int[] copy;
        public int[] max;

        public SegmentTree(int[] ori) {
            N = ori.length + 1;
            copy = new int[N];
            max = new int[N << 2];
            System.arraycopy(ori, 0, copy, 1, N - 1);

        }


        public void build(int l, int r, int rt){
            if (l == r){
                max[rt] = l;
                return;
            }
            int mid = l + ((r - l) >> 1);
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);

            max[rt] = copy[max[rt << 1]] >= copy[max[rt << 1 | 1]] ? max[rt << 1] : max[rt << 1 | 1];
        }


        public int query(int L, int R){
            return query(L, R, 1, N - 1, 1);
        }

        private int query(int L, int R, int l, int r, int rt){
            if (L <= l && R >= r)
                return max[rt];
            int mid = l + ((r - l) >> 1);
            int res = Integer.MIN_VALUE;
            int pos = -1;
            if (L <= mid){
                int p = query(L, R, l, mid, rt << 1);
                if (copy[p] > res){
                    res = copy[p];
                    pos = p;
                }
            }
            if (R > mid){
                int p = query(L, R, mid + 1, r, rt << 1 | 1);
                if (copy[p] > res){
                    res = copy[p];
                    pos = p;
                }
            }
            return pos;
        }
    }

    public static void main(String[] args) {
        int[] arr = {3, 2, 1, 6, 0, 5};
        TreeNode r = constructMaximumBinaryTree(arr);
    }
}
