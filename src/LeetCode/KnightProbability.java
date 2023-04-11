package LeetCode;


/**
 * ymy
 * 2023/4/3 - 15 : 22
 **/


// leetCode688
// 在一个 n x n 的象棋棋盘上，马从单元格 (row, column) 开始，并尝试进行 k 次移动。行和列是 从 0 开始的，所
// 以左上单元格是 (0,0) ，右下单元格是 (n - 1, n - 1) 。
// 马有8种可能的走法，其走法就是象棋中的马走日。
// 每次马要移动时，它都会随机从8种可能的移动中选择一种(即使棋子会离开棋盘)，然后移动到那里。
// 马继续移动，直到它走了 k 步或离开了棋盘。返回 马在棋盘停止移动后仍留在棋盘上的概率 。


public class KnightProbability {

    public static double knightProbability(int n, int k, int row, int column) {
        if (k < 0 || row < 0 || row >= n || column < 0 || column >= n)
            return 0;
        double[][][] dp = new double[n][n][k + 1];
        // 该问题其实就是在问，跳k步后还存在在棋盘内的方式一共有几种，然后除以 8^k  因为这个就是所有的选择
        return dfs(n, row, column, k, dp);
    }

    private static double dfs(int N, int i, int j, int rest, double[][][] dp) {
        if (i < 0 || i >= N || j < 0 || j >= N)
            return 0;
        if (rest == 0)
            return 1;
        if (dp[i][j][rest] != 0)
            return dp[i][j][rest];
        // 执行到这里说明此时的位置还在棋盘内

        double ways = 0;
        ways += dfs(N,  i - 1, j + 2, rest - 1, dp) / 8.0;
        ways += dfs(N,  i - 2, j + 1, rest - 1, dp) / 8.0;
        ways += dfs(N,  i - 2, j - 1, rest - 1, dp) / 8.0;
        ways += dfs(N,  i - 1, j - 2, rest - 1, dp) / 8.0;
        ways += dfs(N,  i + 1, j - 2, rest - 1, dp) / 8.0;
        ways += dfs(N,  i + 2, j - 1, rest - 1, dp) / 8.0;
        ways += dfs(N,  i + 2, j + 1, rest - 1, dp) / 8.0;
        ways += dfs(N,  i + 1, j + 2, rest - 1, dp) / 8.0;
        dp[i][j][rest] = ways;
        return  ways;
    }


    public static void main(String[] args) {
        System.out.println(knightProbability(8, 30, 6, 4));

    }
}
