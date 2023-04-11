package GreatOffer.TopInterviewQ;

import java.util.Arrays;

// 一个机器人位于一个 m x n 网格的左上角。
// 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角。
// 问总共有多少条不同的路径？

public class _0062_UniquePaths {

    // 记忆化搜索
    public static int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int[] arr : dp)
            Arrays.fill(arr, -1);
        return f(0, 0, m - 1, n - 1, dp);
    }

    private static int f(int i, int j, int endI, int endJ, int[][] dp) {
        if (dp[i][j] != -1)
            return dp[i][j];
        if (i == endI && j == endJ)
            dp[i][j] = 1;
        else if (i == endI)
            dp[i][j] = f(i, j + 1, endI, endJ, dp);
        else if (j == endJ)
            dp[i][j] = f(i + 1, j, endI, endJ, dp);
        else
            dp[i][j] = f(i, j + 1, endI, endJ, dp) + f(i + 1, j, endI, endJ, dp);
        return dp[i][j];
    }


    // 动态规划
    public static int uniquePaths2(int m, int n) {
        int[][] dp = new int[m][n];
        for (int j = n - 1; j >= 0; j--)
            dp[m - 1][j] = 1;
        for (int i = m - 1; i >= 0; i--)
            dp[i][n - 1] = 1;
        for (int i = m - 2; i >= 0; i--) {
            for (int j = n - 2; j >= 0; j--)
                dp[i][j] = dp[i + 1][j] + dp[i][j + 1];
        }
        return dp[0][0];
    }


    // 这道题最简单的做法其实就是数学公式求。走的总步数是确定的：(m - 1) + (n - 1) == m + n - 2
    // 向下走的步数和向右走的步数都是确定的，所以最终的答案就是：C(m+n-2, m-1) 或者 C(m+n-2, n-1)
    // 唯一需要注意的地方就是在算分子：(m+n-2)!时可能越界，所以需要用到gcd函数边算边化简

    public static int uniquePaths3(int m, int n) {
        int steps = m + n - 2;
        int des = Math.min(m - 1, n - 1);
        long k = 1;   // 分子
        long d = 1;   // 分母
        while (des != 0) {
            k *= steps--;
            d *= des--;
            long gcd = gcd(k, d);
            k /= gcd;
            d /= gcd;
        }
        return (int) k;
    }


    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }


    public static void main(String[] args) {
        System.out.println(uniquePaths3(3, 7));
    }
}
