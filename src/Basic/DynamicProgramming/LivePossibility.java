package Basic.DynamicProgramming;

// 给定5个参数，N, M, row，col， k
// 表示有一个二维矩阵 N 行 M 列的区域上，醉汉Bob初始在(row,col)位置
// Bob一共要迈出k步，且每步都会等概率向上下左右四个方向走一个单位，任何时候Bob只要离开N*M的区域， 就直接死亡
// 返回k步之后，Bob还在N*M的区域的概率

// 概率怎么求？ 就是求出一共有多少种走的方式让Bob最后还活着，然后除以 (4的k次方)
// (4的k次方) 就是它走k步的所有情况

public class LivePossibility {

    // 暴力递归法
    public static double livePossibilityV1(int N, int M, int row, int col, int k){
        // 列出所有无效参数
        if (row < 0 || row >= N || col < 0 || col >= M || k < 0)
            return -1;
        return process(N, M, row, col, k) / Math.pow(4, k);
    }

    private static int process(int N, int M, int row, int col, int rest) {
        if (row < 0 || row >= N || col < 0 || col >= M || rest < 0)
            return 0;
        if (rest == 0)
            return 1;
        int up = process(N, M, row - 1, col, rest - 1);
        int down = process(N, M, row + 1, col, rest - 1);
        int left = process(N, M, row, col - 1, rest - 1);
        int right = process(N, M, row, col + 1, rest - 1);
        return up + down + left + right;
    }
    // =================================================================================================


    // 动态规划法
    public static double livePossibilityV2(int N, int M, int row, int col, int k){
        if (row < 0 || row >= N || col < 0 || col >= M || k < 0)
            return -1;
        int[][][] cache = new int[N][M][k + 1];
        // 先单独填最后一层
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++)
                cache[i][j][0] = 1;
        }
        // 再处理剩余的
        for (int rest = 1; rest <= k; rest++) { // 层
            for (int i = 0; i < N; i++) {  // 行
                for (int j = 0; j < M; j++) {
                    int up = pick(cache, N, M, i - 1, j, rest - 1);
                    int down = pick(cache, N, M, row + 1, col, rest - 1);
                    int left = pick(cache, N, M, row, col - 1, rest - 1);
                    int right = pick(cache, N, M, row, col + 1, rest - 1);
                    cache[i][j][rest] = up + down + left + right;
                }
            }
        }
        return cache[row][col][k] / Math.pow(4, k);
    }

    private static int pick(int[][][] cache, int N, int M, int i, int j, int rest) {
        if (i < 0 || i >= N || j < 0 || j >= M || rest < 0)
            return 0;
        return cache[i][j][rest];
    }

    public static void main(String[] args) {

    }
}
