package GreatOffer.class36;

import java.util.Arrays;

// 来自美团
// 给定一个数组arr,长度为N,做出一个结构，可以高效的做如下的查询:
//  1) int querySum(L,R) :查询arr[L...R]上的累加和
//  2) int queryAim(L,R) :查询arr[L.. .R]上的目标值，目标值定义如下:
//       假设arr[L.. .R]上的值为[a,b,c,d], a+b+c+d = s 目标值为:
//       (s-a)^2 + (s-b)^2 + (s-c)^2 + (s-d)^2
//  3) int queryMax(L,R) :查询arr[L.. .R]上的最大值
// 要求:
//  1)初始化该结构的时间复杂度不能超过0(N*logN)
//  2)三个查询的时间复杂度不能超过0(logN)
//  3)查询时，认为arr的下标从1开始，比如:
//    arr.=[1，1，2，3];   querySum(1, 3) -> 4   queryAim(2，4) -> 50   queryMax(1, 4) -> 3


public class _05_Code {

    // 第一个查询只需要一个前缀累加和数组就可以了
    // 第二个查询需要先化简一下题目要求：
    // (s-a)^2 + (s-b)^2 + (s-c)^2 + (s-d)^2 == 4s^2 + a^2 + b^2 + c^2 + d^2 - 2s * (a + b + c + d)
    // 再化简：2s^2 + a^2 + b^2 + c^2 + d^2  如果L..R上有5个数，那么化简之后就是：
    // 3s^2 + a^2 + b^2 + c^2 + d^2 + e^2
    // 这个查询同样只需要一个前缀数组help，只不过每个元素不是简单的前缀累加和了，而是前缀平方和：
    // help[i] == arr[0]^2 + arr[1]^2 + arr[2]^2 ... arr[i]^2
    // 第三个查询需要用到线段树


    // 线段树结构
    public static class SegmentTree {
        private int[] max;
        private int[] change;
        private boolean[] update;

        public SegmentTree(int N) {
            max = new int[N << 2];
            change = new int[N << 2];
            update = new boolean[N << 2];
            Arrays.fill(max, Integer.MIN_VALUE);
        }

        public void update(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && R >= r) {
                update[rt] = true;
                change[rt] = C;
                max[rt] = C;
                return;
            }
            int mid = (l + r) >> 1;
            distribute(rt, mid - l + 1, r - mid);
            if (L <= mid)
                update(L, R, C, l, mid, rt << 1);
            if (R > mid)
                update(L, R, C, mid + 1, r, rt << 1 | 1);

            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }


        public int query(int L, int R, int l, int r, int rt) {
            if (L <= l && R >= r)
                return max[rt];
            int mid = (l + r) >> 1;
            distribute(rt, mid - l + 1, r - mid);
            int left = 0;
            int right = 0;
            if (L <= mid)
                left = query(L, R, l, mid, rt << 1);
            if (R > mid)
                right = query(L, R, mid + 1, r, rt << 1 | 1);

            return Math.max(left, right);
        }

        private void distribute(int rt, int ln, int rn) {
            if (update[rt]) {
                update[rt << 1] = true;
                change[rt << 1] = change[rt];
                max[rt << 1] = change[rt];
                update[rt << 1 | 1] = true;
                change[rt << 1 | 1] = change[rt];
                max[rt << 1 | 1] = change[rt];
            }
        }
    }
    // ============================================================================================


    public static class Query {
        public int[] sum;   // 前缀累加和
        public int[] square;   // 前缀平方和
        public SegmentTree st;
        public int M;  // 因为题目要求下标从1开始，所以得准备比原数组多1的长度

        public Query(int[] arr) {
            int N = arr.length;
            M = N + 1;
            sum = new int[M];
            square = new int[M];
            st = new SegmentTree(M);
            for (int i = 0; i < N; i++) {
                sum[i + 1] = sum[i] + arr[i];
                square[i + 1] = square[i] + arr[i] * arr[i];
                st.update(i + 1, i + 1, arr[i], 1, M, 1);
            }
        }


        public int querySum(int L, int R) {
            return sum[R] - sum[L - 1];
        }


        public int queryAim(int L, int R) {
            int s = querySum(L, R);
            return (R - L - 1) * s * s + square[R] - square[L - 1];
        }


        public int queryMax(int L, int R) {
            return st.query(L, R, 1, M, 1);
        }
    }


}
