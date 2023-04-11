package GreatOffer.class26;

import java.util.Arrays;
import java.util.TreeSet;

// 给三个有序的正整数数组，每个数组的长度可能不同，要求：
// 每个数组中选择一个数，假如三个数组选出来的数分别是x、y、z，求使得：|x - y| + |y - z| + |z - x|结果
// 最小的情况，返回该最小的绝对值和。


public class _01_Code {

    // 思路：假设选出来的三个数中最小的是a、最大的是c，中间的是b，那么目标表达式就简化成了：
    // (b - a) + (c - b) + (c - a) == 2(c - a)  两倍的(最大值-最小值)。题目说的是希望该结果越小越好，
    // 所以我们要把这两个最值的差值越小越好，并且中间还包含一个来自不同数组的值。到这里，原始题目已经转变成了：
    // 找一个最小的区间，使得每个数组在这个区间上都至少包含一个数，返回该最小区间长度的两倍。
    // 所以，破题之后发现，这和之前做过的一道题目一样。

    // 暴力方法
    // 枚举所有的可能，按照题目给的目标表达式硬算
    public static int minRange(int[][] m) {
        if (m == null || m.length < 2)
            return -1;
        for (int[] arr : m) {
            if (arr == null || arr.length == 0)
                return -1;
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < m[0].length; i++) {
            for (int j = 0; j < m[1].length; j++) {
                for (int k = 0; k < m[2].length; k++) {
                    res = Math.min(res, Math.abs(m[0][i] - m[1][j]) + Math.abs(m[1][j] - m[2][k])
                            + Math.abs(m[2][k] - m[0][i]));
                }
            }
        }
        return res;
    }
    // ================================================================================================





    // 优化方法，使用有序表，先把每个数组中第一个，也就是每个数组中最小的元素加入有序表
    public static class Node {
        public int val;
        public int arrIndex;  // 位于第几个数组
        public int index;     // 位于该数组的第几个元素

        public Node(int val, int arrIndex, int index) {
            this.val = val;
            this.arrIndex = arrIndex;
            this.index = index;
        }
    }


    public static int minRange2(int[][] m) {
        if (m == null || m.length < 2)
            return -1;
        for (int[] arr : m) {
            if (arr == null || arr.length == 0)
                return -1;
        }
        int N = m.length;
        int res = Integer.MAX_VALUE;
        // 传入的比较器规则，因为只有val是有用的，剩下的属性的比较只是为了不让有序表将其覆盖
        // 因为有序表中，每个数组中只有一个元素在表中，所以arrIndex肯定不同
        TreeSet<Node> set = new TreeSet<>(((o1, o2) ->
                o1.val != o2.val ? o1.val - o2.val : o1.arrIndex - o2.arrIndex));
        // 要先把每个数组的最小元素加进去
        for (int i = 0; i < N; i++)
            set.add(new Node(m[i][0], i, 0));
        while (set.size() == N) {
            int cur = set.last().val - set.first().val;
            res = Math.min(res, cur);
            Node pop = set.pollFirst();
            int arrIndex = pop.arrIndex;
            int index = pop.index;
            if (index < m[arrIndex].length - 1)
                set.add(new Node(m[arrIndex][index + 1], arrIndex, index + 1));
        }
        return res << 1;
    }

    // for test
    public static int[][] generateRandomMatrix(int n, int v) {
        int[][] m = new int[3][];
        int s = 0;
        for (int i = 0; i < 3; i++) {
            s = (int) (Math.random() * n) + 1;
            m[i] = new int[s];
            for (int j = 0; j < s; j++) {
                m[i][j] = (int) (Math.random() * v);
            }
            Arrays.sort(m[i]);
        }
        return m;
    }
    // ================================================================================================

    public static void main(String[] args) {
        int n = 20;
        int v = 200;
        int t = 1000000;
        System.out.println("测试开始");
        for (int i = 0; i < t; i++) {
            int[][] m = generateRandomMatrix(n, v);
            int ans1 = minRange(m);
            int ans2 = minRange2(m);
            if (ans1 != ans2) {
                System.out.println("出错了!");
                System.out.println("暴力方法：" + ans1);
                System.out.println("优化方法：" + ans2);
                return;
            }
        }
        System.out.println("测试结束");
    }

}
