package GreatOffer.class18;

import java.util.Arrays;
import java.util.Scanner;

// 给定一个矩阵matrix，所有元素非0即1。先从左上角开始，每一步只能往右或者往下走，走到右下角。然后从右下角出发，
// 每一步只能往上或者往左走，再回到左上角。任何一个位置的数字，只能获得一遍。返回最大路径和。

// 输入描述:
// 第一行输入两个整数M和N，M,N<=200    接下来M行，每行N个整数，表示矩阵中元素
// 输出描述:
// 输出一个整数，表示最大路径和

public class MaxPathSum {

    // 这是牛客网上的原题，该题目风格需要自己处理输入和输出
    public static void cherryPickUp() {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int[][] m = new int[N][M];
        int seed = (int) (0.25 * N * M);
        for (int i = 0; i < seed; i++) {
            int r = (int) (Math.random() * N);
            int c = (int) (Math.random() * M);
            m[r][c] = 1;
        }
        System.out.println(f(m, 0, 0, 0, 0));
    }

    // 暴力递归尝试。假设有两个人A、B一起从左上角出发，每次都只能走一步，如果A、B落到了同一个格子，如果值为1，只能拿
    // 取一次。可以把A的路径看成去的路，B的路径看成回来的路。返回A和B路径的和

    // m为原始矩阵，A的坐标为(a,b) B的坐标为(c,d)，走到终点能获得的最大路径和。并且A和B一定是同时到达终点的.
    //  如果中途有一个点是A、B都能到达的，那他们必然是同步到达的。
    public static int f(int[][] m, int a, int b, int c, int d) {
        int N = m.length;
        int M = m[0].length;
        if (a == N - 1 && b == M - 1)  // 此时A、B都到达了终点，如果是0，那就返回0，如果是1，只返回一份
            return m[a][b];
        int best = 0;
        // A走下  B走下
        // A走下  B走右
        if (a < N - 1) { // 说明A可以走下
            if (c < N - 1)  // B可以走下
                best = Math.max(best, f(m, a + 1, b, c + 1, d));
            if (d < M - 1)  // B可以走右
                best = Math.max(best, f(m, a + 1, b, c, d + 1));
        }
        // A走右  B走下
        // A走右  B走右
        if (b < M - 1) { // 说明A可以走右
            if (c < N - 1)  // B可以走下
                best = Math.max(best, f(m, a, b + 1, c + 1, d));
            if (d < M - 1)  // B可以走右
                best = Math.max(best, f(m, a, b + 1, c, d + 1));
        }
        // 以上的逻辑只是找出了最好的后续，而A、B当前位置的数也需要一起加上
        int cur = 0;
        if (a == c && b == d) // 如果A、B在同一个格子
            cur = m[a][b];
        else
            cur = m[a][b] + m[c][d];

        return best + cur;
    }
    // =============================================================================================


    // 有了上面的暴力递归后就可以改成记忆化搜索了，不过上面有4个可变参数，看能否精简一下。实际上，无论何时：
    // a + b == c + d， 所以三个可变参数即可。又因为f函数返回值>=0，所以可以用-1表示没算过。

    public static void cherryPickUpV2() {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();
        int[][] m = new int[N][M];
        int seed = (int) (0.3 * N * M);
        for (int i = 0; i < seed; i++) {
            int r = (int) (Math.random() * N);
            int c = (int) (Math.random() * M);
            m[r][c] = 1;
        }
        int[][][] dp = new int[N][M][N];
        for (int[][] matrix : dp)
            for (int[] arr : matrix)
                Arrays.fill(arr, -1);
        System.out.println(g(m, 0, 0, 0, dp));
    }

    public static int g(int[][] m, int a, int b, int c, int[][][] dp) {
        if (dp[a][b][c] != -1)
            return dp[a][b][c];
        int N = m.length;
        int M = m[0].length;
        int res = 0;
        if (a == N - 1 && b == M - 1)  // 此时A、B都到达了终点，如果是0，那就返回0，如果是1，只返回一份
            res = m[a][b];
        else {
            int best = 0;
            int d = a + b - c;
            // A走下  B走下
            // A走下  B走右
            if (a < N - 1) { // 说明A可以走下
                if (c < N - 1)  // B可以走下
                    best = Math.max(best, g(m, a + 1, b, c + 1, dp));
                if (d < M - 1)  // B可以走右
                    best = Math.max(best, g(m, a + 1, b, c, dp));
            }
            // A走右  B走下
            // A走右  B走右
            if (b < M - 1) { // 说明A可以走右
                if (c < N - 1)  // B可以走下
                    best = Math.max(best, g(m, a, b + 1, c + 1, dp));
                if (d < M - 1)  // B可以走右
                    best = Math.max(best, g(m, a, b + 1, c, dp));
            }
            // 以上的逻辑只是找出了最好的后续，而A、B当前位置的数也需要一起加上
            int cur = 0;
            if (a == c && b == d) // 如果A、B在同一个格子
                cur = m[a][b];
            else
                cur = m[a][b] + m[c][d];
            res = best + cur;
        }
        dp[a][b][c] = res;
        return dp[a][b][c];

    }


    public static void main(String[] args) {
        cherryPickUpV2();
    }

}
