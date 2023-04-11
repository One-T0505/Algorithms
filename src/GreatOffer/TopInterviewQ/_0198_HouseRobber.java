package GreatOffer.TopInterviewQ;


// 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋
// 装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
// 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。

public class _0198_HouseRobber {

    // 这道题其实就是给你一个数组，让你选出不含相邻元素的最大子序列累加和。

    public int rob(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MIN_VALUE;
        if (nums.length == 1)
            return nums[0];
        if (nums.length == 2)
            return Math.max(nums[0], nums[1]);
        // dp[i] 表示：从0..i，按要求能选出的最大非相邻子序列累加和
        int N = nums.length;
        int[] dp = new int[N];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        // 可能性有三种：1> 仅选择当前元素nums[i]
        // 2> 绝不选择nums[i]，那就等同于dp[i-1]
        // 3> 一定选择nums[i]，但又不仅仅只包含自己，所以等同于：nums[i] + dp[i-2]  因为选了i，那么i-1必不能选
        for (int i = 2; i < N; i++)
            dp[i] = Math.max(dp[i - 1], nums[i] + Math.max(0, dp[i - 2]));

        return dp[N - 1];
    }


    // 只用有限几个变量完成算法
    public int rob2(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MIN_VALUE;
        if (nums.length == 1)
            return nums[0];
        if (nums.length == 2)
            return Math.max(nums[0], nums[1]);
        // dp[i] 表示：从0..i，按要求能选出的最大非相邻子序列累加和
        int N = nums.length;
        int prePre = nums[0];
        int pre = Math.max(nums[0], nums[1]);

        for (int i = 2; i < N; i++) {
            int cur = Math.max(pre, nums[i] + Math.max(0, prePre));
            prePre = pre;
            pre = cur;
        }

        return pre;
    }
}
