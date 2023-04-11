package Basic.Tree.SegmentTree;

// 给定两个非负数组x和hp，长度都是N，再给定一个正数range。x有序，x[i]表示i号怪兽在x轴上的位置; hp[i]表示i号怪兽的血量;
// range表示法师如果站在x位置，用AOE技能打到的范围是：[x, x+range]，被打到的每只怪兽损失1点血量。每次释放技能的
// 位置可以任意选择。返回要把所有怪兽血量清空，至少需要释放多少次AOE技能?

import utils.arrays;
import java.util.Arrays;

public class KillMonster {

    // 这道题需要一定的敏感度，这个题需要用到线段树！！！ 因为是频繁地在某个范围内操作。并且还需要用到一个贪心策略：
    // 让魔法覆盖的左边界刚好覆盖一个怪兽，这样不会浪费魔法范围。最左边界的怪兽有hp[0]的血，那魔法至少要覆盖到它
    // hp[0]次，所以让它刚好在魔法的最左边界上，然后施法hp[0]次，然后向右找到第一个血量>0的怪兽，让它再次作为魔法
    // 的最左边界，再把它干死。直到整个hp数组都<=0.


    // 暴力方法，之后用作对数器。该方法不用线段树，但是贪心的思路是一样的。
    // 1) 总是用技能的最左边缘刮死当前最左侧的没死的怪物
    // 2) 然后向右找下一个没死的怪物，重复步骤1)
    public static int AOE1(int[] x, int[] hp, int range){
        // 举个例子：
        // 如果怪兽情况如下，
        // 怪兽所在，x数组  : 2 3 5 6 7 9
        // 怪兽血量，hp数组 : 2 4 1 2 3 1
        // 怪兽编号        : 0 1 2 3 4 5 6
        // 技能半径，range = 2
        int N = x.length;
        int[] cover = new int[N];
        // 首先求cover数组，
        // 如果技能左边界就在0号怪兽，那么技能到5号怪兽就覆盖不到了
        // 所以cover[0] = 4;
        // 如果技能左边界就在1号怪兽，那么技能到6号怪兽（越界位置）就覆盖不到了
        // 所以cover[1] = 5;
        // 综上：
        // 如果怪兽情况如下，
        // 怪兽编号        : 0 1 2 3 4 5
        // 怪兽位置，x数组  : 2 3 5 6 7 9
        // 怪兽血量，hp数组 : 2 4 1 2 3 1
        // cover数组情况   : 4 5 5 5 5 5
        // 技能半径，range = 2
        // cover[i] = j，表示如果技能左边界在i怪兽，那么技能会影响i...j号所有的怪兽
        // 就是如下的for循环，在求cover数组
        int right = 0;
        for (int i = 0; i < N; i++) {
            while (right < N && x[right] - x[i] <= (range << 1)) {
                right++;
            }
            cover[i] = right - 1;
        }
        int res = 0;
        // 假设来到i号怪兽了，如果i号怪兽的血量>0，说明i号怪兽没死。根据我们的贪心：
        // 我们要让技能的左边界，刮死当前的i号怪兽，这样能够让技能尽可能的往右释放，去尽可能的打掉右侧的怪兽。
        // 此时cover[i]正好可以告诉我们技能影响多大范围。比如当前来到100号怪兽，血量30，假设cover[100] == 200，
        // 说明，技能左边界在100位置，可以影响100号到200号怪兽的血量。为了打死100号怪兽，我们释放技能30次，释放的
        // 时候，100号到200号怪兽都掉血30点，然后继续向右寻找没死的怪兽。
        for (int i = 0; i < N; i++) {
            if (hp[i] > 0) {
                int minus = hp[i];
                for (int index = i; index <= cover[i]; index++) {
                    hp[index] -= minus;
                }
                res += minus;
            }
        }
        return res;
    }
    // =============================================================================================



    // 线段树的主方法
    public static int AOE2(int[] x, int[] hp, int range){
        int N = x.length;
        int R = 1;
        int res = 0;
        SegmentTree st = new SegmentTree(hp);
        st.build(1, N, 1);
        for (int i = 1; i <= N; i++) {
            if (st.query(i, i, 1, N, 1) <= 0)
                continue;
            while (R <= N && x[R - 1] <= x[i - 1] + (range << 1))
                R++;
            int leftHP = st.query(i, i, 1, N, 1);
            res += leftHP;
            st.add(i, R - 1, -leftHP, 1, N, 1);
        }
        return res;
    }

    // 线段树
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



        public void build(int l, int r, int rt){
            if (l == r){
                sum[rt] = copy[l];
                return;
            }
            int mid = l + ((r - l) >> 1);
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }



        public void add(int L, int R, int C, int l, int r, int rt){
            if (L <= l && R >= r){
                lazy[rt] += C;
                sum[rt] += (r - l + 1) * C;
                return;
            }
            int mid = l + ((r - l) >> 1);
            pushDown(mid - l + 1, r - mid, rt);
            if (L <= mid)
                add(L, R, C, l, mid, rt << 1);
            if (R > mid)
                add(L, R, C, mid + 1, r,rt << 1 | 1);
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }


        public int query(int L, int R, int l, int r, int rt){
            if (L <= l && R >= r){
                return sum[rt];
            }
            int mid = l + ((r - l) >> 1);
            pushDown(mid - l + 1, r - mid, rt);
            int res = 0;
            if (L <= mid)
                res += query(L, R, l, mid, rt << 1);
            if (R > mid)
                res += query(L, R, mid + 1, r,rt << 1 | 1);
            return res;
        }

        private void pushDown(int ln, int rn, int rt) {
            if (lazy[rt] != 0){
                lazy[rt << 1] += lazy[rt];
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }
    }
    // ------------------------------------------------------------------------------------------




    // for test
    // function test
    public static void functionTest(){
        int fixedLen = 40;
        int maxVal = 400;
        int HP = 50;
        int R = 8;
        int testTime = 10000;
        System.out.println("测试开始");
        for (int i = 0; i < testTime; i++) {
            int[] x1 = arrays.fixedLenArray(fixedLen, maxVal);
            Arrays.sort(x1);
            int[] hp1 = arrays.fixedLenArray(fixedLen, HP);
            int[] x2 = new int[fixedLen];
            System.arraycopy(x1, 0, x2, 0, fixedLen);
            int[] hp2 = new int[fixedLen];
            System.arraycopy(hp1, 0, hp2, 0, fixedLen);
            int range = (int) (Math.random() * R) + 1;
            int res1 = AOE1(x1, hp1, range);
            int res2 = AOE2(x2, hp2, range);
            if (res1 != res2) {
                System.out.println("出错了！");
            }
        }
        System.out.println("测试结束");

        fixedLen = 500000;
        long start;
        long end;
        int[] x1 = arrays.fixedLenArray(fixedLen, fixedLen);
        Arrays.sort(x1);
        int[] hp1 = new int[fixedLen];
        for (int i = 0; i < fixedLen; i++) {
            hp1[i] = i * 5 + 10;
        }
        int[] x2 = new int[fixedLen];
        System.arraycopy(x1, 0, x2, 0, fixedLen);
        int[] hp2 = new int[fixedLen];
        System.arraycopy(hp1, 0, hp2, 0, fixedLen);
        int range = 10000;

        start = System.currentTimeMillis();
        System.out.println(AOE1(x1, hp1, range));
        end = System.currentTimeMillis();
        System.out.println("运行时间 : " + (end - start) + " 毫秒");

        start = System.currentTimeMillis();
        System.out.println(AOE2(x2, hp2, range));
        end = System.currentTimeMillis();
        System.out.println("运行时间 : " + (end - start) + " 毫秒");
    }


    public static void main(String[] args) {
        functionTest();
//        int[] x = {2, 5, 6, 9, 15, 18, 24, 25, 29};
//        int[] hp = {4, 2, 6, 1, 7, 5, 9, 3, 8};
//        int range = 2;
//        System.out.println(AOE2(x, hp, range));
    }
}
