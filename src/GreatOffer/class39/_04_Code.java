package GreatOffer.class39;

import java.util.Arrays;

// 来自京东
// 给定一个二维数组m，m[i][j] = k 表示：
// 从(i,j)位置可以随意往右跳<=k步，或者从(i,j)位置可以随意往下跳<=k步
// 如果m[i][j] = 0，代表来到(i,j)位置就必须停止
// 请问：从左上角到右下角至少要跳几次
// m的行数<=5000   列数<=5000

public class _04_Code {

    // 暴力递归尝试
    public static int jump(int[][] m) {
        return f(m, 0, 0);
    }


    // 当前来到了(i,j)位置，返回从(i,j)到右下角最少跳几次
    private static int f(int[][] m, int i, int j) {
        // 如果当前位置就是终点，那就不用跳了，返回0
        if (i == m.length - 1 && j == m[0].length - 1)
            return 0;
        // 当前位置不是终点，并且必须在当前位置停下，那么肯定无法到达终点
        if (m[i][j] == 0)
            return Integer.MAX_VALUE;
        // 当前位置还可以移动
        int next = Integer.MAX_VALUE;
        // 往下的所有情况
        for (int step = 1; step <= m[i][j]; step++) { // 枚举跳的步数
            if (i + step < m.length)
                next = Math.min(next, f(m, i + step, j));
            else // 如果某次 i + step >= m.length  那么后面的循环就不用跑了，因为step会越来越大
                break;
        }
        // 往右的所有情况
        for (int step = 1; step <= m[i][j]; step++) { // 枚举跳的步数
            if (j + step < m[0].length)
                next = Math.min(next, f(m, i, j + step));
            else // 如果某次 i + step >= m.length  那么后面的循环就不用跑了，因为step会越来越大
                break;
        }
        // next表示的是：从(i, j)位置能跳到的所有位置中，找到那个最优的位置，即：该最优的位置到终点跳的次数
        // 但是还要加上从当前位置到最优位置跳的1次
        return next != Integer.MAX_VALUE ? next + 1 : next;
    }
    // 分析时间复杂度：n<=5000 m<=5000  这已经 2.5*10^7  每个位置还需要枚举两边可跳的步数
    // 每个格子最小的值就是1，因为要向右一遍，向下一遍，所以所有位置的枚举代价：2*5000*5000 必然超过10^8
    // 这个枚举代价还是尽量往小了算的   所以必须要省去枚举
    // ============================================================================================


    // 动态规划
    public static int jump2(int[][] m) {
        int N = m.length;
        int M = m[0].length;
        int[][] dp = new int[N][M];
        for (int[] col : dp)
            Arrays.fill(col, Integer.MAX_VALUE);
        dp[N - 1][M - 1] = 0;
        // 先处理最后一行
        for (int j = M - 2; j >= 0; j--) {
            if (m[N - 1][j] != 0) {  // dp中现在每个位置都是系统最大值
                for (int step = 1; step <= m[N - 1][j]; step++) { // 枚举跳的步数
                    if (j + step < M)
                        dp[N - 1][j] = Math.min(dp[N - 1][j], dp[N - 1][j + step]);
                    else // 如果某次 i + step >= m.length  那么后面的循环就不用跑了，因为step会越来越大
                        break;
                }
                if (dp[N - 1][j] != Integer.MAX_VALUE)
                    dp[N - 1][j]++;  // 加上当前跳的1步
            }
        }
        // 再处理最后一列
        for (int i = N - 2; i >= 0; i--) {
            if (m[i][M - 1] != 0) {  // dp中现在每个位置都是系统最大值
                for (int step = 1; step <= m[i][M - 1]; step++) { // 枚举跳的步数
                    if (i + step < N)
                        dp[i][M - 1] = Math.min(dp[i][M - 1], dp[i + step][M - 1]);
                    else // 如果某次 i + step >= m.length  那么后面的循环就不用跑了，因为step会越来越大
                        break;
                }
                if (dp[i][M - 1] != Integer.MAX_VALUE)
                    dp[i][M - 1]++;  // 加上当前跳的1步
            }
        }

        // 处理剩下位置
        for (int i = N - 2; i >= 0; i--) {
            for (int j = M - 2; j >= 0; j--) {
                if (m[i][j] != 0) {
                    // 枚举向右的所有情况
                    for (int step = 1; step <= m[i][j]; step++) { // 枚举跳的步数
                        if (j + step < M)
                            dp[i][j] = Math.min(dp[i][j], dp[i][j + step]);
                        else // 如果某次 i + step >= m.length  那么后面的循环就不用跑了，因为step会越来越大
                            break;
                    }
                    // 枚举向下的所有情况
                    for (int step = 1; step <= m[i][j]; step++) { // 枚举跳的步数
                        if (i + step < N)
                            dp[i][j] = Math.min(dp[i][j], dp[i + step][j]);
                        else // 如果某次 i + step >= m.length  那么后面的循环就不用跑了，因为step会越来越大
                            break;
                    }
                    if (dp[i][j] != Integer.MAX_VALUE)
                        dp[i][j]++;  // 加上当前跳的1步
                }
            }
        }
        return dp[0][0];
    }
    // 每个格子依然是没有省去枚举行为，发现这里的枚举其实就是找某个区间上的最小值，如果当前来到dp[i][j]
    // 且m[i][j]==k，那么完成这个格子的流程就是：在dp[i][j+1]～dp[i][j+k]中找出向右的最小值，然后再
    // dp[i+1][j]～dp[i+k][j]上找出向下的最小值，然后再做后续操作，主要的过程就是在某个范围上查询最小值，
    // 这不就是线段树嘛！！！！！
    // 来到每个格子时，我们需要向下的某个区间的信息和向右的某个区间的信息，所以我们需要在每行设置一个线段树，
    // 每列也需要一个线段树，每个格子上的值修改后，要对应修改其行线段树和列线段树
    // ===========================================================================================


    // 动态规划 + 斜率优化之线段树
    // 该方法是最优解了，其整体流程和上面的方法一模一样，线段树只是为了加速完成一个格子的计算
    public static int jump3(int[][] m) {
        int N = m.length;
        int M = m[0].length;
        // 为了让下标都统一起来，因为线段树下标是从1开始的，我们把这m也重新copy一下
        // m[2][3] == map[3][4]
        int[][] map = new int[N + 1][M + 1];
        for (int i = 1; i <= N; i++)
            System.arraycopy(m[i - 1], 0, map[i], 1, N);
        // 行线段树  rt[i]记录的就是map[i]这一行的信息，因为也想让rt从下标1开始，所以大小开辟了N+1
        //          rt[0]弃而不用
        SegmentTree[] rt = new SegmentTree[N + 1];
        // 每个行线段树管理的是一行，其大小是列的大小
        for (int i = 1; i <= N; i++)
            rt[i] = new SegmentTree(M); // 线段树内部会对M做下标转移，也让其从1开始

        // 列线段树
        SegmentTree[] ct = new SegmentTree[M + 1];
        // 每个列线段树管理的是一列，其大小是行的大小
        for (int i = 1; i <= M; i++)
            ct[i] = new SegmentTree(N);

        // 下面这两行就是在处理终点位置的值，在缓存中终点位置的值应该是0
        rt[N].update(M, M, 0, 1, M, 1);
        ct[M].update(N, N, 0, 1, N, 1);

        // 先处理最后一行
        for (int j = M - 1; j >= 1; j--) {
            if (map[N][j] != 0) {
                // 先确定要查询的左右边界   最后一行的元素只能水平移动  所以只有查询的左右边界
                int left = j + 1;
                int right = Math.min(j + map[N][j], M);   // 不能越界
                int next = rt[N].query(left, right, 1, M, 1);
                if (next != Integer.MAX_VALUE) {
                    rt[N].update(j, j, next + 1, 1, M, 1);
                    ct[j].update(N, N, next + 1, 1, N, 1);
                }
            }
        }

        // 再处理最后一列
        for (int i = N - 1; i >= 1; i--) {
            if (map[i][M] != 0) {
                // 先确定要查询的上下边界   最后一列的元素只能垂直移动  所以只有查询的上下边界
                int up = i + 1;
                int down = Math.min(i + map[i][M], N);   // 不能越界
                int next = ct[M].query(up, down, 1, N, 1);
                if (next != Integer.MAX_VALUE) {
                    rt[i].update(M, M, next + 1, 1, M, 1);
                    ct[M].update(i, i, next + 1, 1, N, 1);
                }
            }
        }

        // 最后处理剩余位置
        for (int i = N - 1; i >= 1; i--) {
            for (int j = M - 1; j >= 1; j--) {
                if (map[i][j] != 0) {
                    int left = j + 1;
                    int right = Math.min(j + map[i][j], M);
                    int next1 = rt[i].query(left, right, 1, M, 1);
                    int up = i + 1;
                    int down = Math.min(i + map[i][j], N);
                    int next2 = ct[j].query(up, down, 1, N, 1);
                    int next = Math.min(next1, next2);
                    if (next != Integer.MAX_VALUE) {
                        rt[i].update(j, j, next + 1, 1, M, 1);
                        ct[j].update(i, i, next + 1, 1, N, 1);
                    }
                }
            }
        }
        return rt[1].query(1, 1, 1, M, 1);
    }
    // ===========================================================================================


    // 线段树结构 	注意下标从1开始，不从0开始 比如你传入n = 8  我们希望将其对应成下标1~8，而不是0~7
    // 那么就需要让n+1
    public static class SegmentTree {
        private int N;
        private int[] min;
        private int[] change;
        private boolean[] update;

        public SegmentTree(int n) {
            N = n + 1;
            min = new int[N << 2];
            change = new int[N << 2];
            update = new boolean[N << 2];
            update(1, n, Integer.MAX_VALUE, 1, n, 1);
        }

        public void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && R >= r) {
                update[rt] = true;
                change[rt] = C;
                min[rt] = C;
                return;
            }
            int mid = l + ((r - l) >> 1);
            distribute(rt, mid - l + 1, r - mid);
            if (L <= mid)
                update(L, R, C, l, mid, rt << 1);
            if (R > mid)
                update(L, R, C, mid + 1, r, rt << 1 | 1);
            min[rt] = Math.min(min[rt << 1], min[rt << 1 | 1]);
        }


        // 查询[L..R]范围上的最小值
        public int query(int L, int R, int l, int r, int rt) {
            if (L <= l && R >= r)
                return min[rt];
            int mid = l + ((r - l) >> 1);
            distribute(rt, mid - l + 1, r - mid);
            int res = Integer.MAX_VALUE;
            if (L <= mid)
                res = Math.min(res, query(L, R, l, mid, rt << 1));
            if (R > mid)
                res = Math.min(res, query(L, R, mid + 1, r, rt << 1 | 1));

            return res;
        }


        private void distribute(int rt, int ln, int rn) {
            if (update[rt]) {
                update[rt << 1] = true;
                change[rt << 1] = change[rt];
                min[rt << 1] = change[rt];
                update[rt << 1 | 1] = true;
                change[rt << 1 | 1] = change[rt];
                min[rt << 1 | 1] = change[rt];
                update[rt] = false;
            }
        }
    }

}
