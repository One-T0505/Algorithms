package GreatOffer.class23;


// 有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。
// 每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
// 找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1 。

public class MergeStones {

    public static int mergeStones(int[] stones, int k) {
        if (stones == null)
            return -1;
        int N = stones.length;
        // N个数，到底能不能k个相邻的数合并，最终变成1个数！
        if ((N - 1) % (k - 1) > 0)
            return -1;
        int[] preSum = new int[N];
        preSum[0] = stones[0];
        for (int i = 1; i < N; i++)
            preSum[i] = preSum[i - 1] + stones[i];
        return f(stones, 0, N - 1, k, 1, preSum);
    }


    // 将arr[L..R]这段一定要合成只有p段需要的最小代价
    // preSum就是累加和数组，为了方便计算；arr、k、preSum都是固定参数
    private static int f(int[] arr, int L, int R, int k, int p, int[] preSum) {
        // 如果只有一个元素了，且要1份，那代价就是0，如果不是1份，那就不可能搞定
        if (L == R)
            return p == 1 ? 0 : -1;
        if (p == 1) { // 如果还剩多个元素，要合成1份，那么首先要把他们分成k份，这样k份才能合成1份
            int next = f(arr, L, R, k, k, preSum);
            // 如果next==-1，说明不可能将L..R先变成k份，那必然最后不可能弄成1份，所以直接返回-1；
            // 如果next!=-1，那么next就表示将L..R合成k份的代价了，还差最后将k份合成1份的代价
            // 这里有一个边界问题：如果L==0, R为其他数，想得到0..R的累加和，如果用：preSum[R] - preSum[L - 1]
            // 会越界，所以需要单独判断下
            return next == -1 ? -1 : next + (preSum[R] - (L == 0 ? 0 : preSum[L - 1]));
        } else {
            int res = Integer.MAX_VALUE;
            // 枚举：L..mid合成1份；mid+1..R合成p-1份
            // 如果L..mid能搞定成1份，那么mid==L+k-1，L+2k-1。。。
            for (int mid = L; mid < R; mid += k - 1) {
                int p1 = f(arr, L, mid, k, 1, preSum);
                int p2 = f(arr, mid + 1, R, k, p - 1, preSum);
                if (p1 != -1 && p2 != -1)
                    res = Math.min(res, p1 + p2);
            }
            return res;
        }
    }
    // ==============================================================================================


    // 记忆化搜索  上面的递归方法有三个可变参数，所以需要一张三维表
    public static int mergeStonesV2(int[] stones, int k) {
        if (stones == null)
            return -1;
        int N = stones.length;
        // N个数，到底能不能k个相邻的数合并，最终变成1个数！
        if ((N - 1) % (k - 1) > 0)
            return -1;
        int[] preSum = new int[N];
        preSum[0] = stones[0];
        for (int i = 1; i < N; i++)
            preSum[i] = preSum[i - 1] + stones[i];
        // 可以从题意看出给定的是一个正整数数组，所以任何一个代价不可能为0，所以就用默认值0表示没算过
        int[][][] dp = new int[N][N][k + 1];
        return g(stones, 0, N - 1, k, 1, preSum, dp);
    }


    private static int g(int[] arr, int L, int R, int k, int p, int[] preSum, int[][][] dp) {
        if (dp[L][R][p] != 0)
            return dp[L][R][p];
        if (L == R) {
            dp[L][R][p] = p == 1 ? 0 : -1;
            return dp[L][R][p];
        }
        if (p == 1) { // 如果还剩多个元素，要合成1份，那么首先要把他们分成k份，这样k份才能合成1份
            int next = g(arr, L, R, k, k, preSum, dp);
            dp[L][R][p] = next == -1 ? -1 : next + (preSum[R] - (L == 0 ? 0 : preSum[L - 1]));
            return dp[L][R][p];
        } else {
            int res = Integer.MAX_VALUE;
            for (int mid = L; mid < R; mid += k - 1) {
                int p1 = g(arr, L, mid, k, 1, preSum, dp);
                int p2 = g(arr, mid + 1, R, k, p - 1, preSum, dp);
                if (p1 != -1 && p2 != -1)
                    res = Math.min(res, p1 + p2);
            }
            dp[L][R][p] = res;
            return dp[L][R][p];
        }
    }


    public static void main(String[] args) {
        int[] arr = {3, 2, 4, 1};
        int k = 2;
        System.out.println(mergeStones(arr, k));
    }
}
