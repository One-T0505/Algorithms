package class03;


// 给定一个数组arr，代表每个人的能力值。再给定一个非负数k。如果两个人能力差值正好为k，那么可以凑在一起比赛。
// 一局比赛只有两个人，返回最多可以同时有多少场比赛。

import java.util.Arrays;

public class _02_ArrangeGame {

    // 先排序完，再用滑动窗口。用一个布尔型数组标记对应元素是否使用过；这个方法是包含贪心策略的：为什么要排序完从左开始安排呢？
    // 我们的贪心策略就是先让紧挨着的数去比赛，能找到最优解。比如：排完序后：[1 3 5 7] k=2，那么安排1和3，5和7，是最优解。
    // 如果安排3和5，剩下的就不能安排。
    public static int arrangeGame(int[] arr, int k) {
        if (arr == null || arr.length < 2 || k < 0)
            return 0;
        Arrays.sort(arr);
        int N = arr.length;
        int L = 0, R = 0;
        int res = 0;
        boolean[] used = new boolean[N];
        while (L < N && R < N) {
            if (used[L])
                L++;
            else if (L == R)
                R++;
            else {
                int distance = arr[R] - arr[L];
                if (distance == k) {
                    used[R++] = true;
                    L++;
                    res++;
                } else if (distance > k)
                    L++;
                else
                    R++;
            }
        }
        return res;
    }
}
