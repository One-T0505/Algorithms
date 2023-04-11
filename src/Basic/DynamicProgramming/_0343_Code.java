package Basic.DynamicProgramming;

// 给定一个正整数 n ，将其拆分为 k 个 正整数 的和（ k >= 2 ），并使这些整数的乘积最大化。
// 返回你可以获得的最大乘积 。

import java.util.Arrays;

public class _0343_Code {


    // 暴力递归法
    public static int integerBreak(int n) {
        if (n == 2)
            return 1;
        // 首先要对需要划分成多少个数做范围限定，最少划分成2个数，最多划分成n-1个数，如果划分成n个正整数，那必然全为1，
        // 所以划分个数的上下界就确定了。
        // 这里还有个小技巧，如果要划分成两个数且使乘积最大，那必然是中间数相乘最大。
        int res = (n >> 1) * (n - (n >> 1));
        for (int parts = 3; parts < n; parts++) {
            res = Math.max(res, f(n, parts));
        }
        return res;
    }


    // 将数n分成parts份能得到的最大乘积
    private static int f(int n, int parts) {
        if (n < 0)
            return 0;
        if (n == 0)
            return parts == 0 ? 1 : 0;
        if (parts == 0)
            return 0;
        int res = 0;
        // 枚举第一部分的值，不可能分个1出来的，因为对最终的乘积没有一点收益
        for (int i = 1; i <= n; i++) {
            res = Math.max(res, i * f(n - i, parts - 1));
        }
        return res;
    }
    // ===============================================================================================




    // 记忆化搜索
    public static int integerBreak2(int n) {
        if (n == 2)
            return 1;
        // 首先要对需要划分成多少个数做范围限定，最少划分成2个数，最多划分成n-1个数，如果划分成n个正整数，那必然全为1，
        // 所以划分个数的上下界就确定了。
        // 这里还有个小技巧，如果要划分成两个数且使乘积最大，那必然是中间数相乘最大。
        int res = (n >> 1) * (n - (n >> 1));
        int[][] dp = new int[n + 1][n];
        for (int i = 0; i <= n; i++)
            Arrays.fill(dp[i], -1);
        for (int parts = 3; parts < n; parts++) {
            res = Math.max(res, g(n, parts, dp));
        }
        return res;
    }


    // 将数n分成parts份能得到的最大乘积
    private static int g(int n, int parts, int[][] dp) {
        if (n < 0)
            return 0;
        if (dp[n][parts] != -1)
            return dp[n][parts];
        int res = 0;
        if (n == 0)
            res = parts == 0 ? 1 : 0;
        else if (parts == 0)
            res = 0;
        else {
            // 枚举第一部分的值，不可能分个1出来的，因为对最终的乘积没有一点收益
            for (int i = 1; i <= n; i++) {
                res = Math.max(res, i * g(n - i, parts - 1, dp));
            }
        }
        dp[n][parts] = res;
        return res;
    }
    // ===============================================================================================




    // 动态规划
    public static int integerBreak3(int n) {
        if (n == 2)
            return 1;
        // dp[i]表示：将数i切分能得到的最大乘积 第0、1个元素弃用
        int[] dp = new int[n + 1];
        dp[2] = 1;
        // 再完成剩下的
        for (int i = 3; i <= n; i++) {
            int tmp = 0;
            for (int k = 1; k < i; k++) {
                // 有两种情况：将第一部分分成k，剩余部分继续切分；或者是直接将i分成k和i-k，不再继续切分
                tmp = Math.max(tmp, Math.max(k * (i - k), k * dp[i - k]));
            }
            dp[i] = tmp;
        }
        return dp[n];
    }


    public static void main(String[] args) {
        System.out.println(f(2, 2));
    }
}
