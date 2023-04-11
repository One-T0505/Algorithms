package GreatOffer.class38;

// 360笔试题
// 长城守卫军   题目描述:
// 长城上有连成一排的n个烽火台，每个烽火台都有士兵驻守。
// 第i个烽火台驻守着ai个士兵，相邻峰火台的距离为1。另外，有m位将军，每位将军可以驻守一个峰火台，
// 每个烽火台可以有多个将军驻守，将军可以影响所有距离他驻守的峰火台小于等于x的烽火台。
// 每个烽火台的基础战斗力为土兵数，另外，每个能影响此烽火台的将军都能使这个烽火台的战斗力提升k。
// 长城的战斗力为所有烽火台的战斗力的最小值。
//
// 请问长城的最大战斗力可以是多少?
// 输入描述
//   第一行四个正整数 n, m, x, k (1<=x<=n<=10^5, 0<=m<=10^5, 1<=k<=10^5)
//   第二行n个整数 ai (0<=ai<=10^5)
// 输出描述仅一行，一个整数，表示长城的最大战斗力
//
// 样例输入  5  2  1  2
//          4  4  2  4  4
// 样例输出
//   6

public class _02_Code {

    // 这道题目非常重要，需要用到的知识点：之前讲的AOE魔法师问题  线段树  二分

    // 先找到arr中的最大值max，把所有将军的增幅都加给max->max + m * k  看这个值和limit的关系，如果
    // 这个值 < limit，那就直接false，说明不可能完成，因为最大的战斗力都达不到limit，最低战斗力的烽火台不可能达到
    // 找到arr中的最小值min，即便没有一位将军为其增幅，长城的战斗力也不可能小于min，所以二分的两个边界就找到了
    // min～max+m*k.

    // 下面就来具体讲讲如何增加长城战斗力  战斗力数组：[6, 5, 2, 7, 1, 4, 3, 6]  m=4  x=3  k=2 limit=5
    // 遍历数组，找到第一个<limit的位置2，先算出如果要让当前烽火台的战斗力>=limit，至少需要几个将军
    // ceiling((5-2)/2)==2，ceiling表示向上取整，至少需要2个将军才能完成，那么如何放置将军最经济呢？
    // 2左边的元素已经不需要增幅了，所以就让增幅的左边界刚好覆盖2即可，这样是最经济的，这就是魔法师AOE问题的核心。
    // 如果要让2刚好在增幅的左边界，那影响的范围就是2~1，所以就是说在范围上加一个值，这就是线段树。当累加完后，
    // 从现在的位置继续向右找需要增幅的位置，找到后，检查一下此时还剩多少个将军，如果没将军了，直接返回false

    // 这个将军的影响范围x可以这样理解：如果x=3，那么其实就是说一次可以影响3个烽火台  [2, 5, 1, 4, 6]
    // 可以一次影响2～1 或者 5～4 或者 1～6  但是将军具体在哪个烽火台其实不重要

    public static int maxForce(int[] wall, int m, int x, int k) {
        // R其实就在找最大值，并让最大值成熟所有增幅，看它能冲到最高的极限是多少，这也是二分的上界，所以用R
        long L = Integer.MAX_VALUE;
        long R = 0;
        for (int force : wall) {
            L = Math.min(L, force);
            R = Math.max(R, force);
        }
        R += (long) m * k;
        long res = 0;
        while (L <= R) {
            long mid = L + ((R - L) >> 1);
            if (satisfy(wall, m, x, k, mid)) {
                res = mid;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        return (int) res;
    }



    // x是将军影响范围，固定参数   k是将军增幅，固定参数
    // 该方法表示：在不超过m位将军的情况下，能否将长城的战斗力提升至>=limit
    // 也就是说能否将每个烽火台的战斗力都>=limit，在使用不超过m个将军的情况下
    private static boolean satisfy(int[] wall, int m, int x, int k, long limit) {
        int N = wall.length;
        SegmentTree st = new SegmentTree(wall);
        st.build(1, N, 1);
        int all = 0;  // 总共需要的将军数量
        for (int i = 0; i < N; i++) {
            // 当前的战斗力
            // 千万不能从wall中直接读取，因为如果在某个地方放置了将军后影响了周围烽火台的战斗力后，烽火台的战斗力
            // 就变了，但是变化的值只会在线段树中记录，原始数组中是不会有变化的
            long cur = st.query(i + 1, i + 1, 1, N, 1);
            if (cur < limit) {
                // 将当前烽火台战斗力提升到>=limit需要几个将军
                int need = (int) (limit - cur + k - 1) / k;
                all += need;
                if (all > m)
                    return false;
                // i是从0开始的下标，线段树是从1开始的下标   将军影响的右边界可能超过数组了，所以需要比较一下
                st.add(i + 1, Math.min(N, i + x), need * k, 1, N, 1);
            }
        }
        return true;
    }


    // 该题目的性质决定线段树不需要考虑设置某个区间上的值，只需要考虑add方法即可
    public static class SegmentTree {
        public int N;
        public int[] copy;
        public int[] sum;
        public int[] lazy;

        public SegmentTree(int[] ori) {
            N = ori.length + 1;
            copy = new int[N];
            System.arraycopy(ori, 0, copy, 1, N - 1);
            sum = new int[N << 2];
            lazy = new int[N << 2];
        }


        public void build(int l, int r, int rt) {
            if (l == r) {
                sum[rt] = copy[l];
                return;
            }
            int mid = l + ((r - l) >> 1);
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }


        public void add(int L, int R, int C, int l, int r, int rt) {
            if (L <= l && R >= r) {
                lazy[rt] += C;
                sum[rt] += C * (r - l + 1);
                return;
            }
            int mid = l + ((r - l) >> 1);
            distribute(rt, mid - l + 1, r - mid);
            if (L <= mid)
                add(L, R, C, l, mid, rt << 1);
            if (R > mid)
                add(L, R, C, mid + 1, r, rt << 1 | 1);
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }


        public long query(int L, int R, int l, int r, int rt) {
            if (L <= l && R >= r)
                return sum[rt];
            int mid = l + ((r - l) >> 1);
            distribute(rt, mid - l + 1, r - mid);
            long res = 0;
            if (L <= mid)
                res += query(L, R, l, mid, rt << 1);
            if (R > mid)
                res += query(L, R, mid + 1, r, rt << 1 | 1);

            return res;
        }


        private void distribute(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }


    }
}
