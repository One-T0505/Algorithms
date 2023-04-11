package GreatOffer.class41;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


// 来自小红书
// 有四种诗的韵律分别为: AABB、 ABAB、ABBA、AAAA
// 比如: 1 1 3 3 就属于AABB型的韵律、6 6 6 6就属于 AAAA 型的韵律等等
// 一个数组arr，当然可以生成很多的子序列，如果某个子序列一直以韵律的方式连接起来，我们称这样的子序列是有效的
// 比如 arr={1, 1, 15, 1, 34, 1, 2, 67, 3, 3, 2, 4, 15, 3, 17, 4, 3, 7, 52, 7, 81, 9, 9}
// arr的一个子序列为{1, 1, 1, 1, 2, 3, 3, 2, 4, 3, 4, 3, 7, 7, 9，9}
// 其中 1，1, 1，1 是AAAA   2, 3, 3，2 是ABBA  4, 3, 4，3 是ABAB  7, 7, 9，9 是AABB
// 可以看到，整个子序列一直以韵律的方式连接起来，所以这个子序列是有效的
// 给定一个数组arr,返回最长的有效子序列长度
// 题目限制: arr长度<= 4000， arr中的值<= 10^9

public class _02_Code {

    // 先将数组的值归一化。假如数组为：[45, 288, 96] --> [0, 2, 1]  就是保持数组的相对次序，但是让
    // 数值都变小点
    public static int maxPoemLen(int[] arr) {
        if (arr == null || arr.length < 4)
            return 0;
        int N = arr.length;
        int[] sorted = Arrays.copyOf(arr, N);
        Arrays.sort(sorted);
        // key : 原数组中某个值归一化后的结果   value : 是一个数组，表示原数组中归一化结果相同的元素都分别
        // 在哪些位置    比如：(2, [14, 22, 34]) 表示 normalized[14, 22, 34] == 2
        HashMap<Integer, List<Integer>> dp = new HashMap<>();
        normalized(arr, sorted, dp);  // 将排序后的数组归一化
        return f(arr, dp, 0);
    }


    // 该方法不仅按照原数组的顺序将其归一化了，并且还完成了dp的填写
    private static void normalized(int[] arr, int[] sorted, HashMap<Integer, List<Integer>> dp) {
        // (值，归一化后的结果)
        HashMap<Integer, Integer> record = new HashMap<>();
        int N = sorted.length;
        // 这里的index也揭示了当前元素被归一化后的结果
        int res = 0;
        record.put(sorted[0], res++); // sorted[0]是全局最小值，将其归一化为0
        for (int i = 1; i < N; i++) {
            if (sorted[i] != sorted[i - 1])
                record.put(sorted[i], res++);
        }
        for (int i = 0; i < N; i++) {
            int cur = record.get(arr[i]);
            arr[i] = cur;
            if (!dp.containsKey(cur))
                dp.put(cur, new ArrayList<>());
            dp.get(cur).add(i);
        }
    }


    // 最核心的递归方法
    // 从arr[i]开始到最后能找出的有效的最长子序列长度
    private static int f(int[] arr, HashMap<Integer, List<Integer>> dp, int i) {
        if (i + 4 > arr.length) // i到最后还不够4个元素 根本不可能找出
            return 0;
        int p0 = f(arr, dp, i + 1);  // 根本不考虑arr[i]
        // 考虑arr[i]  那么我们准备让arr[i]去完成哪个韵律呢
        // 让它去完成AABB
        int p1 = 0;
        // 如果让arr[i]做第一个A，那么我们需要在i+1到N-1上寻找最近的和arr[i]相等的元素
        // 这里就体现出了dp的作用  其实就是在dp.get(arr[i])这个数组上二分
        int p1A2 = mostLeftG(dp.get(arr[i]), i);
        if (p1A2 != -1) { // 如果真的在i后面找到了最近的A
            // 那么就在p1A2后面开始枚举每一个可以做第一个B的位置
            for (int j = p1A2 + 1; j < arr.length - 1; j++) {
                if (arr[j] != arr[i]) { // B要和A不一样
                    int p1B2 = mostLeftG(dp.get(arr[j]), j); // 找到>j且最左的位置
                    if (p1B2 != -1)
                        p1 = Math.max(p1, 4 + f(arr, dp, p1B2 + 1));
                }
            }
        }

        // 让它去完成ABAB
        int p2 = 0;
        for (int j = i + 1; j < arr.length; j++) {
            if (arr[j] != arr[i]) { // j就是第一个B的位置
                int p2A2 = mostLeftG(dp.get(arr[i]), j);
                if (p2A2 != -1) {
                    int p2B2 = mostLeftG(dp.get(arr[j]), p2A2);
                    if (p2B2 != -1)
                        p2 = Math.max(p2, 4 + f(arr, dp, p2B2 + 1));
                }
            }
        }

        // 让它去完成ABBA
        int p3 = 0;
        for (int j = i + 1; j < arr.length; j++) {
            if (arr[j] != arr[i]) { // j就是第一个B的位置
                int p2B2 = mostLeftG(dp.get(arr[j]), j);
                if (p2B2 != -1) {
                    int p2A2 = mostLeftG(dp.get(arr[i]), p2B2);
                    if (p2A2 != -1)
                        p3 = Math.max(p3, 4 + f(arr, dp, p2A2 + 1));
                }
            }
        }

        // 让它去完成AAAA
        int p4 = 0;
        int p4A2 = mostLeftG(dp.get(arr[i]), i);
        int p4A3 = p4A2 == -1 ? -1 : mostLeftG(dp.get(arr[i]), p4A2);
        int p4A4 = p4A3 == -1 ? -1 : mostLeftG(dp.get(arr[i]), p4A3);
        if (p4A4 != -1)
            p4 = Math.max(p4, 4 + f(arr, dp, p4A4 + 1));

        return Math.max(p0, Math.max(p1, Math.max(p2, Math.max(p3, p4))));
    }


    // 在list上找到>t且最左的位置
    private static int mostLeftG(List<Integer> list, int t) {
        int L = 0;
        int R = list.size() - 1;
        int res = -1;
        while (L <= R) {
            int M = L + ((R - L) >> 1);
            if (list.get(M) <= t)
                L = M + 1;
            else {
                R = M - 1;
                res = list.get(M);
            }
        }
        return res;
    }
    // =========================================================================================


    // 改写成动态规划  上面的递归函数只有只有一个可变参数，所以缓存表只需要一个数组，每个格子的填写时间
    // 为O(N)  所以总时间复杂度为O(N^2)
    // 如果像优化常数时间则可以使用矩阵来替代上面系统的哈希表
    // m[i][] : 表示arr[i]这个元素出现在哪些位置
    public static int maxPoemLen2(int[] arr) {
        if (arr == null || arr.length < 4)
            return 0;
        int N = arr.length;
        int[] sorted = Arrays.copyOf(arr, N);
        Arrays.sort(sorted);
        int[][] m = preProcess(arr, sorted);
        int[] dp = new int[N + 1];
        for (int i = N - 4; i >= 0; i--) { // i==N-3的话，一共只有3个字符，不可能，从N-4开始才有可能
            int p0 = dp[i + 1];
            int p1 = 0; // 让它去完成AABB
            int p1A2 = mostLeftG(m[arr[i]], i);
            if (p1A2 != -1) { // 如果真的在i后面找到了最近的A
                for (int j = p1A2 + 1; j < arr.length - 1; j++) {
                    if (arr[j] != arr[i]) { // B要和A不一样
                        int p1B2 = mostLeftG(m[arr[j]], j); // 找到>j且最左的位置
                        if (p1B2 != -1)
                            p1 = Math.max(p1, 4 + dp[p1B2 + 1]);
                    }
                }
            }

            int p2 = 0; // 让它去完成ABAB
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] != arr[i]) { // j就是第一个B的位置
                    int p2A2 = mostLeftG(m[arr[i]], j);
                    if (p2A2 != -1) {
                        int p2B2 = mostLeftG(m[arr[j]], p2A2);
                        if (p2B2 != -1)
                            p2 = Math.max(p2, 4 + dp[p2B2 + 1]);
                    }
                }
            }

            int p3 = 0; // 让它去完成ABBA
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] != arr[i]) { // j就是第一个B的位置
                    int p2B2 = mostLeftG(m[arr[j]], j);
                    if (p2B2 != -1) {
                        int p2A2 = mostLeftG(m[arr[i]], p2B2);
                        if (p2A2 != -1)
                            p3 = Math.max(p3, 4 + dp[p2A2 + 1]);
                    }
                }
            }

            // 让它去完成AAAA
            int p4 = 0;
            int p4A2 = mostLeftG(m[arr[i]], i);
            int p4A3 = p4A2 == -1 ? -1 : mostLeftG(m[arr[i]], p4A2);
            int p4A4 = p4A3 == -1 ? -1 : mostLeftG(m[arr[i]], p4A3);
            if (p4A4 != -1)
                p4 = Math.max(p4, 4 + dp[p4A4 + 1]);

            dp[i] = Math.max(p0, Math.max(p1, Math.max(p2, Math.max(p3, p4))));
        }
        return dp[0];
    }


    // 归一化方法，将原数组arr归一化，并把用矩阵表示的dp返回
    private static int[][] preProcess(int[] arr, int[] sorted) {
        int N = arr.length;
        // key: 元素值   value: 应该被归一化为多少
        HashMap<Integer, Integer> map = new HashMap<>();
        int index = 0;
        map.put(sorted[0], index++);
        for (int i = 1; i < N; i++) {
            if (sorted[i] != sorted[i - 1])
                map.put(sorted[i], index++);
        }
        // 统计每种值的个数
        int[] sizeArr = new int[index];
        for (int i = 0; i < N; i++)
            sizeArr[map.get(sorted[i])]++;

        int[][] dp = new int[index][];

        for (int i = 0; i < index; i++)
            dp[i] = new int[sizeArr[i]];

        // 将arr上的元素值都修改为对应的归一化值 并 准备dp
        for (int i = 0; i < N; i++) {
            arr[i] = map.get(arr[i]);
            dp[arr[i]][--sizeArr[arr[i]]] = i;
        }
        return dp;
    }


    private static int mostLeftG(int[] arr, int t) {
        int L = 0;
        int R = arr.length - 1;
        int res = -1;
        while (L <= R) {
            int M = L + ((R - L) >> 1);
            if (arr[M] > t) {
                R = M - 1;
                res = arr[M];
            } else
                L = M + 1;
        }
        return res;
    }
}
