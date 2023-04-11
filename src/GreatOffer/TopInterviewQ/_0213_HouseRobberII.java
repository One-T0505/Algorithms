package GreatOffer.TopInterviewQ;


// 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都围成一圈，这意味着
// 第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上
// 被小偷闯入，系统会自动报警 。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，今晚能够偷窃到的最高金额。

public class _0213_HouseRobberII {


    // 这个和198题的唯一区别就是0号房屋和N-1号房屋也是紧挨着的。但是核心解法还是一样的，
    // 只不过我们需要在0~N-2选出最优解，再从1～N-1选一个最优解，两个解的最优解就是答案。

    public int rob(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MIN_VALUE;
        if (nums.length == 1)
            return nums[0];
        if (nums.length == 2)
            return Math.max(nums[0], nums[1]);
        int N = nums.length;
        int res1 = Integer.MIN_VALUE;
        // 先在0～N-2处找出最优解
        int prePre = nums[0];
        int pre = Math.max(nums[0], nums[1]);
        for (int i = 2; i < N - 1; i++) {
            res1 = Math.max(pre, nums[i] + prePre);
            prePre = pre;
            pre = res1;
        }
        res1 = pre;

        // 再从1～N-1找出最优解
        int res2 = Integer.MIN_VALUE;
        prePre = nums[1];
        pre = Math.max(nums[1], nums[2]);
        for (int i = 3; i < N; i++) {
            res2 = Math.max(pre, nums[i] + prePre);
            prePre = pre;
            pre = res2;
        }
        res2 = pre;

        return Math.max(res1, res2);
    }
}
