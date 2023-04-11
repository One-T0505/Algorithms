package class08;

import java.util.Arrays;

// 给定一个矩阵matrix， 值有正、负、0。蛇可以空降到最左列的任何一个位置，初始增长值是0。蛇每一步可以选择
// 右上、右、右下三个方向的任何一个前进，沿途的数字累加起来，作为增长值;但是蛇一旦增长值为负数，就会死去。
// 蛇有一种能力，可以使用一次：把某个格子里的数变成相反数。蛇可以走到任何格子的时候停止。
// 返回蛇能获得的最大增长值。

public class Snake {

    // 思路：最简单的思路就是用递归来尝试。很容易想到一个递归函数的定义：f(pos1, pos2) 从pos1到pos2能得到的最大增长值，
    //      要对每个格子都来一次。并且该f函数还应该返回两个信息：用了技能和不用技能分别能得到的最大值。

    public static int snake(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;
        int res = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                Info cur = f(matrix, i, j);
                res = Math.max(res, Math.max(cur.no, cur.yes));
            }
        }
        return res;
    }

    // 蛇从某一个最左列，且最优的空降点降落
    // 沿途走到(i,j)必须停！
    // 返回：1⃣️一次能力也不用，获得的最大成长值   2⃣️用了一次能力，获得的最大成长值
    // 如果蛇从某一个最左列，且最优的空降点降落，不用能力，怎么都到不了(i,j)，那么no = -1
    // 如果蛇从某一个最左列，且最优的空降点降落，用了一次能力，怎么都到不了(i,j)，那么yes = -1
    // 因为蛇有这样的能力，所以不可能获得最大增长为负数，所以用-1不会有歧义
    public static Info f(int[][] matrix, int i, int j) {
        if (j == 0) {  // 如果j==0，说明是第0列，就是空降地，那就是直接看这个位置元素的正负
            int yes = Math.max(-1, -matrix[i][0]);  // 必须使用一次，使用后如果<0,也是不可达
            int no = Math.max(-1, matrix[i][0]);  // 如果出生地直接<0，那么用-1表示不可达
            return new Info(yes, no);
        }
        // j > 0  不是最左列
        // ===========================================================================
        int preNo = -1;
        int preYes = -1;
        // 一定有左边的信息
        Info pre = f(matrix, i, j - 1);
        preNo = Math.max(pre.no, preNo);
        preYes = Math.max(pre.yes, preYes);

        // i > 0 才有可能有来自左上的信息到(i,j)
        if (i > 0) {
            pre = f(matrix, i - 1, j - 1);
            preNo = Math.max(preNo, pre.no);
            preYes = Math.max(preYes, pre.yes);
        }

        // i < matrix.length - 1  才有可能有来自左下的信息到(i,j)
        if (i < matrix.length - 1) {
            pre = f(matrix, i + 1, j - 1);
            preNo = Math.max(preNo, pre.no);
            preYes = Math.max(preYes, pre.yes);
        }
        // ===========================================================================
        // 上面被包围的这段代码只是在整理所有之前的信息，下面的代码才是正理当前位置(i, j)的信息

        // 如果当前不用能力，那就是直接加上该元素
        int no = preNo == -1 ? -1 : (Math.max(-1, preNo + matrix[i][j]));

        // 用当前元素包括两种情况：之前就用过了；之前没用过现在使用
        int p1 = preYes == -1 ? -1 : Math.max(-1, preYes + matrix[i][j]);   // 如果之前用过，那现在就不能用
        // 如果决定现在用，那就只能选择之前没用过能力的数值
        int p2 = preNo == -1 ? -1 : Math.max(-1, preNo - matrix[i][j]);

        int yes = Math.max(Math.max(p1, p2), -1);

        return new Info(yes, no);
    }

    // 动态规划
    public static int snakeV2(int[][] m) {
        if (m == null || m.length == 0 || m[0].length == 0)
            return 0;
        int res = Integer.MIN_VALUE;
        int N = m.length;
        int M = m[0].length;
        // 最后的维度2，对应的是每个格子不用能力和用能力时能获得的最大增长值
        int[][][] dp = new int[N][M][2];
        // 先处理第0列
        for (int i = 0; i < N; i++) {
            dp[i][0][0] = m[i][0];
            dp[i][0][1] = -m[i][0];
            res = Math.max(res, Math.max(dp[i][0][0], dp[i][0][1]));
        }

        for (int j = 1; j < M; j++) {
            for (int i = 0; i < N; i++) {
                // 必然有左边的信息，所以先让当前信息全部等于左边的信息
                int preUnused = dp[i][j - 1][0];
                int preUsed = dp[i][j - 1][1];
                dp[i][j][0] = preUnused;
                dp[i][j][1] = preUsed;

                // i > 0 才有可能有来自左上的信息到(i,j)
                if (i > 0) {
                    preUnused = Math.max(preUnused, dp[i - 1][j - 1][0]);
                    preUsed = Math.max(preUsed, dp[i - 1][j - 1][1]);
                }

                // i < matrix.length - 1  才有可能有来自左下的信息到(i,j)
                if (i < N - 1) {
                    preUnused = Math.max(preUnused, dp[i + 1][j - 1][0]);
                    preUsed = Math.max(preUsed, dp[i + 1][j - 1][1]);
                }
                dp[i][j][0] = -1;
                dp[i][j][1] = -1;

                if (preUnused >= 0) {
                    dp[i][j][0] = preUnused + m[i][j];
                    dp[i][j][1] = preUnused - m[i][j];
                }

                if (preUsed >= 0)
                    dp[i][j][1] = Math.max(dp[i][j][1], preUsed + m[i][j]);

                res = Math.max(res, Math.max(dp[i][j][0], dp[i][j][1]));
            }
        }
        return res;
    }
    // ====================================================================================================

    // for test
    public static int[][] generateRandomArray(int row, int col, int value) {
        int[][] arr = new int[row][col];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = (int) (Math.random() * value) * (Math.random() > 0.5 ? -1 : 1);
            }
        }
        return arr;
    }
    // ====================================================================================================

    public static void main(String[] args) {
        int N = 7;
        int M = 7;
        int V = 10;
        int times = 1000000;
        for (int i = 0; i < times; i++) {
            int r = (int) (Math.random() * (N + 1));
            int c = (int) (Math.random() * (M + 1));
            int[][] matrix = generateRandomArray(r, c, V);
            int ans1 = snake(matrix);
            int ans2 = snakeV2(matrix);
            if (ans1 != ans2) {
                for (int[] ints : matrix)
                    System.out.println(Arrays.toString(ints));
                System.out.println("Oops   ans1: " + ans1 + "   ans2:" + ans2);
                break;
            }
        }
        System.out.println("finish");
    }

    public static class Info {
        public int yes;  // 用了技能能得到的最大增长值
        public int no;   // 不用技能能得到的最大增长值

        public Info(int yes, int no) {
            this.yes = yes;
            this.no = no;
        }
    }
}
