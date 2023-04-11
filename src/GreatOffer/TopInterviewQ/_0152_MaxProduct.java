package GreatOffer.TopInterviewQ;

// 给你一个整数数组 nums，请你找出数组中乘积最大的非空连续子数组（该子数组中至少包含一个数字），并返回该
// 子数组所对应的乘积。

public class _0152_MaxProduct {

    // 该数组中可能包含负数。思路：依然是使用辅助数组dp，dp[i]表示必须以i位置元素为结尾，向左能找到的最大子数组累
    // 乘积。所以，dp[i]有两种可能性：1> arr[i]  2> arr[i] * dp[i - 1]
    // 因为有负数的可能性，所以dp[i]的结果还存在第三种可能性：arr[i] * dpmin[i-1]
    // dpmin[i]表示必须以i位置元素为结尾，向左能找到的最小子数组累乘积。比如：
    // arr = [100, 6, -2, -3]  dp[3] = dpmin[2] * arr[3] == -1200 * -3 == 3600
    // 所以对于每个元素，我们都需要求两个信息

    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MIN_VALUE;
        int N = nums.length;
        int[] dpMax = new int[N];
        dpMax[0] = nums[0];
        int[] dpMin = new int[N];
        dpMin[0] = nums[0];
        int res = nums[0];

        for (int i = 1; i < N; i++) {
            int p1 = nums[i];
            int p2 = nums[i] * dpMax[i - 1];
            int p3 = nums[i] * dpMin[i - 1];

            dpMax[i] = Math.max(p1, Math.max(p2, p3));
            dpMin[i] = Math.min(p1, Math.min(p2, p3));
            res = Math.max(dpMax[i], res);
        }
        return res;
    }


    // 只使用有限几个变量替代辅助数组
    public int maxProduct2(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MIN_VALUE;
        int N = nums.length;
        int dpMax = nums[0];
        int dpMin = nums[0];
        int res = nums[0];

        for (int i = 1; i < N; i++) {
            int p1 = nums[i];
            int p2 = nums[i] * dpMax;
            int p3 = nums[i] * dpMin;

            dpMax = Math.max(p1, Math.max(p2, p3));
            dpMin = Math.min(p1, Math.min(p2, p3));
            res = Math.max(dpMax, res);
        }
        return res;
    }
}
