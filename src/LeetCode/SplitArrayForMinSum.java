package LeetCode;

import java.util.Arrays;

/**
 * ymy
 * 2023/3/22 - 16 : 48
 **/

// 给定一个非负整数数组 nums 和一个整数 k ，你需要将这个数组分成 k 个非空的连续子数组。
// 设计一个算法使得这 k 个子数组各自和的最大值最小。

public class SplitArrayForMinSum {

    public static int res = Integer.MAX_VALUE;

    public static int splitArray(int[] nums, int k) {
        if (nums == null || k < 1 || nums.length < k)
            return Integer.MAX_VALUE;
        dfs(nums, Integer.MIN_VALUE, nums[0], 1, k);
        return res;
    }


    // 当前来到i位置，但是决策的是到上一部分到i-1位置是否需要截断
    // rest 表示还要分成几份
    private static void dfs(int[] nums, int pre, int part, int i, int rest) {
        if (i == nums.length){
            if (rest == 1){
                res = Math.min(res, Math.max(pre, part));
            }
        } else {
            // 决定不截断
            dfs(nums, pre, part + nums[i], i + 1, rest);
            // 决定截断
            dfs(nums, Math.max(pre, part), nums[i], i + 1, rest - 1);
        }
    }
    // -------------------------------------------------------------------------------------------




    // 动态规划
    // 令 dp[i][j] 表示将数组的前 i 个数分割为 j 段所能得到的最大连续子数组和的最小值.
    // 在进行状态转移时，我们可以考虑第 j 段的具体范围，即我们可以枚举 k，其中前 k 个数被分割为
    // j−1 段，而第 k+1 到第 i 个数为第 j 段。此时，这 j 段子数组中和的最大值，就等于
    // dp[k][j−1] preSum(k+1,i) 中的较大值
    // 因为不可以分出空的子数组，所以 i >= j 才是有效空间
    public static int splitArray2(int[] nums, int k){
        if (nums == null || k < 1 || nums.length < k)
            return Integer.MAX_VALUE;
        int N = nums.length;

        int[] preSum = new int[N + 1];
        for (int i = 0; i < N; i++) {
            preSum[i + 1] = preSum[i] + nums[i];
        }

        int[][] dp = new int[N + 1][k + 1];
        for (int[] a : dp)
            Arrays.fill(a, Integer.MAX_VALUE);
        // 因为假如j==1，那么我们枚举前i个数中前k个数被分成了j-1段，而第k～i-1的数被分在了第j段
        // 如果k==0，那么就是dp[0][0]  入股哦我们将其设置为全局最大值，那必然会影响我们找到正确的子数组最大和
        dp[0][0] = 0;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= Math.min(i, k); j++) {
                for (int p = 0; p < i; p++) {
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[p][j - 1], preSum[i] - preSum[p]));
                }
            }
        }
        return dp[N][k];
    }
    // 如果还不明白，就去看官方题解




    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int k = 1;
        System.out.println(splitArray(arr, k));
    }
}
