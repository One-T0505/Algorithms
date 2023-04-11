package GreatOffer.TopHotQ;


// 在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。

public class _0221_MaximalSquare {


    // 动态规划。遍历矩阵，每碰到1，就让他作为正方形最右下角的1，看它能扩到多大。
    // 一个普通位置(i,j)依赖其左、上、左上三者的值。那具体是什么转移方程呢？
    // 假设dp[i][j-1] = 5，就是说以(i, j-1)这个位置的1作为正方形的右下角，可以最多找到一个边长为5的正方形
    // dp[i-1][j-1] = 4, dp[i-1][j] = 3   ●处为(i,j)
    // 那么 dp[i][j] = Math.min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1]) + 1
    //
    //   1  1  1  1  1  0
    //   1  1  1  1  1  1
    //   1  1  1  1  1  1
    //   1  1  1  1  1  1
    //   1  1  1  1  1  ●

    public int maximalSquare(char[][] m) {
        if (m == null || m.length == 0 || m[0].length == 0)
            return 0;
        int N = m.length;
        int M = m[0].length;
        int[][] dp = new int[N][M];
        int max = 0;
        // 先处理第0列
        for (int i = 0; i < N; i++) {
            if (m[i][0] == '1') {
                dp[i][0] = 1;
                max = 1;
            }
        }
        // 再处理第0行
        for (int j = 1; j < M; j++) {
            if (m[0][j] == '1') {
                dp[0][j] = 1;
                max = 1;
            }
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (m[i][j] == '1') {
                    dp[i][j] = Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
                    max = Math.max(max, dp[i][j]);
                }
            }
        }
        return max * max;
    }
}
