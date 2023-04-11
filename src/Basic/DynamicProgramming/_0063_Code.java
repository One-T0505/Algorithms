package Basic.DynamicProgramming;

// 一个机器人位于一个 m x n 网格的左上角（起始点在下图中标记为 “Start” ）。
// 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish”）。
// 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？
// 网格中的障碍物和空位置分别用 1 和 0 来表示。

public class _0063_Code {

    public int N;
    public int M;

    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        N = obstacleGrid.length - 1;
        M = obstacleGrid[0].length - 1;
        if (obstacleGrid[0][0] == 1 || obstacleGrid[N][M] == 1)
            return 0;
        return f(obstacleGrid, 0, 0);
    }

    private int f(int[][] grid, int n, int m) {
        if (n == N && m == M)
            return 1;
        if (grid[n][m] == 1)
            return 0;
        if (n == N)
            return f(grid, n, m + 1);
        if (m == M)
            return f(grid, n + 1, m);
        return f(grid, n, m + 1) + f(grid, n + 1, m);
    }





    // 动态规划版
    public int uniquePathsWithObstacles2(int[][] obstacleGrid) {
        N = obstacleGrid.length;
        M = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1 || obstacleGrid[N - 1][M - 1] == 1)
            return 0;
        int[][] dp = new int[N][M];
        dp[N - 1][M - 1] = 1;
        for (int j = M - 2; j >= 0; j--)
            dp[N - 1][j] = obstacleGrid[N - 1][j] == 1 ? 0 : dp[N - 1][j + 1];
        for (int i = N - 2; i >= 0; i--)
            dp[i][M - 1] = obstacleGrid[i][M - 1] == 1 ? 0 : dp[i + 1][M - 1];
        for (int i = N - 2; i >= 0; i--) {
            for (int j = M - 2; j >= 0; j--) {
                dp[i][j] = obstacleGrid[i][j] == 1 ? 0 : dp[i + 1][j] + dp[i][j + 1];
            }
        }
        return dp[0][0];
    }
}
