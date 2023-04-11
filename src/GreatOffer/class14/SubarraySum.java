package class14;


import utils.arrays;

import java.util.TreeSet;

// 请返回arr中，子数组的累加和<=K的并且是最大的。返回这个最大的累加和。

public class SubarraySum {
    public int age;

    // 又是这种子数组累加和问题，就应该直接想到以某个元素作为子数组结尾去找子数组。
    // 并且需要构造前缀和。假如arr[0..i]=sum，那么以i作为结尾的子数组中，符合要求的怎么找？
    // 就是说找一个0<=j<=i，使得0..j的累加和>=sum-K，且最小的。

    // O(NlogN)
    public static int lessAndEqualSubArr(int[] arr, int k) {
        if (arr == null || arr.length == 0)
            return Integer.MIN_VALUE;  // 无效
        if (arr.length == 1) {
            return arr[0] <= k ? arr[0] : Integer.MIN_VALUE;
        }
        int N = arr.length;
        int res = Integer.MIN_VALUE;
        int sum = 0;
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);   // 一个数都没有的时候，前缀累加和就是0
        for (int e : arr) {
            sum += e;
            if (set.ceiling(sum - k) != null)
                res = Math.max(res, sum - set.ceiling(sum - k));
            set.add(sum);
        }
        return res;
    }


    // 在arr[L..R]上找出>=t的最小值
    private static int mostLeftGAE(int[] arr, int L, int R, int t) {
        int mid = 0;
        int index = 0;
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            if (arr[mid] >= t) {
                index = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return index;
    }


    // 对数器  O(N^2)
    public static int lessAndEqualSubArrV2(int[] arr, int k) {
        if (arr == null || arr.length == 0)
            return k + 1;  // 无效
        int res = Integer.MIN_VALUE;
        int N = arr.length;
        int[] help = new int[N];
        help[0] = arr[0];
        for (int i = 1; i < N; i++)
            help[i] = help[i - 1] + arr[i];
        for (int end = 0; end < N; end++) {
            for (int start = end; start >= 0; start--) {
                int tmp = start == end ? arr[start] :
                        (start == 0 ? help[end] : help[end] - help[start - 1]);
                if (tmp <= k)
                    res = Math.max(res, tmp);
            }
        }
        return res;
    }


    // for test
    public static void test() {
        for (int i = 0; i < 1000000; i++) {
            int[] arr = arrays.randomNoNegativeArr(5, 20);
            int N = arr.length;
            int[] copy = new int[N];
            System.arraycopy(arr, 0, copy, 0, N);
            int k = arrays.randomNoNegativeNum(20);
            int res1 = lessAndEqualSubArr(arr, k);
            int res2 = lessAndEqualSubArrV2(arr, k);
            if (res1 != res2) {
                System.out.println("Failed");
                arrays.printArray(copy);
                System.out.println("k: " + k);
                System.out.println("优化解法：" + res1);
                System.out.println("二分解法：" + res2);
                return;
            }
        }
        System.out.println("AC");
    }


    public static void main(String[] args) {
        test();
//        int[] arr = {10, 12, 6};
//        int k = 16;
//        System.out.println(lessAndEqualSubArr(arr, k));
    }
}
