package GreatOffer.class24;

import java.util.ArrayList;

// N*M的棋盘 (N相M是输入参数)
// 每种颜色的格子数必须相同的，上下左右的格子算相邻，相邻格子染的颜色必须不同。
// 所有格子必须染色，返回至少多少种颜色可以完成任务。

public class _04_COde {

    public static int minColors(int N, int M) {
        if (N < 1 || M < 1)
            return 0;
        if (N == 1 && M == 1)
            return 1;
        int[][] m = new int[N][M];  // 数值全为0
        // 枚举颜色数，1种肯定不行，N*M一定可以，只不过是最差的情况
        for (int i = 2; i < N * M; i++) {  // 如果用3种颜色，那就在矩阵上填1～3，对应每种颜色
            // 格子数要能整除颜色数
            if ((N * M) % i == 0 && paint(m, N, M, i))
                return i;
        }
        return N * M;
    }


    // 只能用colors种颜色去涂一个N*M的矩阵，能否符合题目要求
    private static boolean paint(int[][] m, int N, int M, int colors) {

        int all = N * M;
        int every = all / colors;  // 每种颜色需要涂多少个格子
        ArrayList<Integer> rest = new ArrayList<>();
        // 0号颜色不需要填格子，所以加了0
        rest.add(0);
        // 索引代表第几种颜色，数值表示这种颜色还需要涂多少个格子
        for (int i = 1; i <= colors; i++)
            rest.add(every);
        return f(m, N, M, colors, 0, 0, rest);
    }

    // 现在来到matrix[i][j]这个格子了，rest记录着每种颜色还需要涂多少个格子
    // 该方法表示：从当前(i,j)这个位置开始，只使用colors种颜色，能否搞定剩下所有格子
    // 在这个设定中我们涂格子的顺序也是从左到右，从上到下，一行一行地涂
    private static boolean f(int[][] m, int N, int M, int colors, int i, int j,
                             ArrayList<Integer> rest) {
        // 说明是从矩阵最右下角的格子过来的，说明已经全部涂完了
        if (i == N)
            return true;
        // 这是表示一行结束了，该去下一行的第0列了
        if (j == M)
            return f(m, N, M, colors, i + 1, 0, rest);
        // 剩余的普通位置
        int left = j == 0 ? 0 : m[i][j - 1]; // 左边格子的颜色
        int up = i == 0 ? 0 : m[i - 1][j]; // 上面格子的颜色
        // 因为按照刚才说的涂格子的顺序，当我们来到一个格子的时候，其左边的和上面的格子肯定已经涂完了
        // 而其右侧的格子是待涂的
        for (int k = 1; k <= colors; k++) {
            if (k != left && k != up && rest.get(k) != 0) {
                int need = rest.get(k);
                rest.set(k, need - 1);
                m[i][j] = k;
                if (f(m, N, M, colors, i, j + 1, rest))
                    return true;
                // 还原现场
                rest.set(k, need);
                m[i][j] = 0;
            }
        }
        return false;
    }
}
