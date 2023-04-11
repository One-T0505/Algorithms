package GreatOffer.TopInterviewQ;

// 给定一个 m x n 整数矩阵 matrix ，找出其中最长递增路径的长度。
// 对于每个单元格，你可以往上，下，左，右四个方向移动。 你不能在对角线方向上移动或移动到边界外（即不允许环绕）。

// m == matrix.length
// n == matrix[i].length
// 1 <= m, n <= 200
// 0 <= matrix[i][j] <= 2^31 - 1

public class _0329_LongestIncrementalPath {

    public static int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0)
            return 0;
        int N = matrix.length;
        int M = matrix[0].length;
        int[][] dp = new int[N][M]; // 用0就可以表示没计算过，因为最小的答案就是1
        int res = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                res = Math.max(res, f(matrix, N, M, i, j, dp));
            }
        }
        return res;
    }


    // 因为题目规定了数值范围为非负数，所以我们可以用-1标记当前位置是否来过
    private static int f(int[][] m, int N, int M, int i, int j, int[][] dp) {
        if (i < 0 || i >= N || j < 0 || j >= M || m[i][j] == -1)
            return 0;
        if (dp[i][j] != 0)
            return dp[i][j];
        int cur = m[i][j];
        m[i][j] = -1;
        int p = 0;
        if (i > 0 && m[i - 1][j] > cur)
            p = Math.max(p, f(m, N, M, i - 1, j, dp));
        if (i < N - 1 && m[i + 1][j] > cur)
            p = Math.max(p, f(m, N, M, i + 1, j, dp));
        if (j > 0 && m[i][j - 1] > cur)
            p = Math.max(p, f(m, N, M, i, j - 1, dp));
        if (j < M - 1 && m[i][j + 1] > cur)
            p = Math.max(p, f(m, N, M, i, j + 1, dp));
        m[i][j] = cur;
        dp[i][j] = p + 1;
        return p + 1;
    }



    public static void main(String[] args) {
        int[][] m = {{9, 9, 4}, {6, 6, 8}, {2, 1, 1}};
        System.out.println(longestIncreasingPath(m));
    }
}
