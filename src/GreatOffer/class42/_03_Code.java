package GreatOffer.class42;

// leetCode265
// 给定一个矩阵m，m[i][k]表示i号房子刷成k号颜色的花费
// 要让0...N-1的房子相邻不同色
// 返回最小花费

public class _03_Code {

    // 生成一个同样大小的二维矩阵dp，dp[i][j]表示从0～i号房子成功上色且没违反题目规定的情况下，且最后一个
    // 房子染成j颜色的最小代价。
    //
    //     ●  ●  ●  ●  ❌  ●  ●  ●  ●    任意一个普通位置?处，都只需要找到上一行除去自己所在列的最小值再
    //                 ?                 加上m[i][j]即可
    // 如果m是13行9列的矩阵，那么m[6][5]就需要把m[5]这一行除了5列的元素算一个最小值；m[6][6]还需要把
    // m[5]这一行除了6列的元素算一个最小值。。。 所以说每个格子都会有重复的计算过程。所以可以使用线段树，快速找出
    // 范围内的最小值。

    public static int minCost(int[][] m) {
        int N = m.length;
        int M = m[0].length;
        int[][] dp = new int[N][M];
        System.arraycopy(m[0], 0, dp[0], 0, M);


        int res = Integer.MAX_VALUE;
        for (int cost : dp[N - 1])
            res = Math.min(res, cost);

        return res;
    }
}
