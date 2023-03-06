package DynamicProgramming;

// 给定一个二维数组matrix，你可以从任何位置出发，走向上下左右四个方向。返回能走出来的最长的递增链长度，前提是不能越界。
// eg:
//      9  8  3  15  14  13  5                  从第3行第4列的1可以走出最长的递增链：
//      4  5  9  10  11  12  7                  1->2->3->4->5->6->7->8->9->10->11->12->13->14->15
//      2  1  8  1   2   3   8
//      6  4  7  6   5   4   10
//

public class Wander {

    // 暴力递归
    public static int wander(int[][] m){
        if (m == null || m.length == 0)
            return 0;
        int N = m.length;
        int M = m[0].length;
        int res = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                res = Math.max(res, f(m, i, j));
            }
        }
        return res;
    }

    private static int f(int[][] m, int i, int j) {
        int N = m.length;
        int M = m[0].length;
        // base case 其实可以不写，因为下方调用递归时保证了位置的合理性
        if (i < 0 || i >= N || j < 0 || j >= M)
            return 0;
        int up = i > 0 && m[i - 1][j] > m[i][j] ? f(m, i - 1, j) : 0;
        int down = i < M - 1 && m[i + 1][j] > m[i][j] ? f(m, i + 1, j) : 0;
        int left = j > 0 && m[i][j - 1] > m[i][j] ? f(m, i, j - 1) : 0;
        int right = j < M - 1 && m[i][j + 1] > m[i][j] ? f(m, i, j + 1) : 0;

        return Math.max(up, Math.max(down, Math.max(left, right))) + 1;
    }
    // =========================================================================================





    // 记忆化搜索
    public static int wander2(int[][] m){
        if (m == null || m.length == 0)
            return 0;
        int N = m.length;
        int M = m[0].length;
        int res = 0;
        int[][] dp = new int[N][M]; // 用0就可以表示没计算过了，因为任意一个答案都>=1
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                res = Math.max(res, g(m, i, j, dp));
            }
        }
        return res;
    }

    private static int g(int[][] m, int i, int j, int[][] dp) {
        int N = m.length;
        int M = m[0].length;
        // base case 其实可以不写，因为下方调用递归时保证了位置的合理性
        if (dp[i][j] != 0)
            return dp[i][j];
        int up = i > 0 && m[i - 1][j] > m[i][j] ? g(m, i - 1, j, dp) : 0;
        int down = i < M - 1 && m[i + 1][j] > m[i][j] ? g(m, i + 1, j, dp) : 0;
        int left = j > 0 && m[i][j - 1] > m[i][j] ? g(m, i, j - 1, dp) : 0;
        int right = j < M - 1 && m[i][j + 1] > m[i][j] ? g(m, i, j + 1, dp) : 0;

        dp[i][j] = Math.max(up, Math.max(down, Math.max(left, right))) + 1;
        return dp[i][j];
    }
}
