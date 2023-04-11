package GreatOffer.class24;

// 长度为N的数组arr，一定可以组成N^2个数字对
// 例如arr= [3,1,2]，数字对有(3,3) (3,1) (3,2) (1,3) (1,1) (1,2) (2,3) (2,1) (2,2)
// 也就是任意两个数都可以，而且自己和自己也算数字对   数字对怎么排序?
// 第一维数据从小到大;第一维数据一样的，第二维数组也从小到大
// 所以上面的数值对排序的结果为: (1,1)(1,2)1,3)(2,1)(2,2)(2,3)(3,1)(3,2)(3,3)
// 给定一个数组arr,和整数k，返回第k小的数值对

import java.util.Arrays;

public class _01_COde {

    // 暴力方法  将所有的数字对排序，然后返回第k-1个数字对
    public static int[] kthMinPair1(int[] arr, int k) {
        if (arr == null || k < 1 || Math.pow(arr.length, 2) < k)
            return null;
        int N = arr.length;
        Pair[] pairs = new Pair[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                pairs[i * N + j] = new Pair(arr[i], arr[j]);
            }
        }
        // 对N^2长度的数组排序，时间复杂度O(N^2*log(N^2))
        Arrays.sort(pairs, (o1, o2) -> o1.x != o2.x ? o1.x - o2.x : o1.y - o2.y);
        return new int[]{pairs[k - 1].x, pairs[k - 1].y};
    }
    //


    public static int[] kthMinPair2(int[] arr, int k) {
        if (arr == null || k < 1 || Math.pow(arr.length, 2) < k)
            return null;
        int N = arr.length;
        Arrays.sort(arr);
        // 第k小的数字对的第一维数据
        int firstNum = arr[(k - 1) / N];
        // 数出比firstNum小的数有几个
        int lessFirstNumS = 0;
        // 数出等于firstNum的数有几个
        int equalFirstNumS = 0;
        for (int i = 0; i < N && arr[i] <= firstNum; i++) {
            if (arr[i] < firstNum)
                lessFirstNumS++;
            else
                equalFirstNumS++;
        }
        // 第一维数据比firstNum小的搞定了多少个数字对, rest就是说还剩多少个
        int rest = k - (lessFirstNumS * N);
        // 如果数组长度为5，firstNum这个数有3个，那么这3个数组成的15个数字对的排序是这样的：
        // (fn1,arr[0]) (fn2,arr[0]) (fn3,arr[0]) (fn1,arr[1]) (fn2,arr[1]) (fn3,arr[1])....
        // 这样才是符合题目要求的。rest其实就是表示在第一维数据是firstNum的情况下，应该找出第几小的数字对。
        // 所以，(rest - 1)/equalFirstNumSize就能确定第二维数据
        return new int[]{firstNum, arr[(rest - 1) / equalFirstNumS]};
    }
    // ============================================================================================


    // 优化方法  时间复杂度O(N*logN)
    // 先将原数组排序，花费时间O(N*logN)，我们要找出第k小的数字对，在这样一个有序数组中就很容易定位到
    // 第k小的数字对的第一维数据是多少： arr[(k - 1)/N]   (k-1)/N 就是目标数字对第一维数据应该在数组的哪个位置
    // 假设数组长度为7，如果我们要找第8小的数字对，那么第一维数据应该是(8-1)/7==1，应该是arr[1]，因为arr[0]
    // 可以组成7个数字对，并且第一维数据比arr[1]小，所以排序在前。(k-1)/N为什么要减1？因为有边界情况，假如k==7，
    // 那么按理说第一维数据应该是arr[0]，但是 k/N == 1，不符合情况。

    public static int[] kthMinPair3(int[] arr, int k) {
        if (arr == null || k < 1 || Math.pow(arr.length, 2) < k)
            return null;
        int N = arr.length;
        // 因为无序，所以不能直接找到目标的第一维数据
        int firstNum = getKthMin(arr, (k - 1) / N);
        // 数出比firstNum小的数有几个
        int lessFirstNumS = 0;
        // 数出等于firstNum小的数有几个
        int equalFirstNumS = 0;
        for (int j : arr) {
            if (j < firstNum)
                lessFirstNumS++;
            if (j == firstNum)
                equalFirstNumS++;
        }
        int rest = k - (lessFirstNumS * N);
        return new int[]{firstNum, getKthMin(arr, (rest - 1) / equalFirstNumS)};
    }
    // ============================================================================================


    // 最优方法  时间复杂度O(N)
    // 还记得之前讲的BFPRT算法吗？在无序数组中找出第k小的数且时间复杂度为O(N)，这个最优方法的基本思想
    // 方法2一样，只不过减少了排序。其实不一定要用BFPRT，只需要用改进版的快排即可，因为BFPRT相对于改进的快排优化的
    // 地方这道题用不到。

    // 改写快排，时间复杂度O(N)
    // 在无序数组arr中找到，如果排序的话，arr[i]的数是什么？
    private static int getKthMin(int[] arr, int i) {
        int L = 0;
        int R = arr.length - 1;
        int pivot = 0;
        int[] range = null;
        while (L < R) {
            // 随机找一个枢纽
            pivot = arr[L + (int) (Math.random() * (R - L + 1))];
            range = partition(arr, L, R, pivot);
            if (i < range[0])
                R = range[0] - 1;
            else if (i > range[1])
                L = range[1] + 1;
            else
                return pivot;
        }
        return arr[L];
    }
    // 该方法和上一个方法一模一样，只是获取目标数据的时候不能直接通过数组找到了

    private static int[] partition(int[] arr, int L, int R, int pivot) {
        int left = L - 1;
        int right = R + 1;
        while (L < right) {
            if (arr[L] < pivot)
                swap(arr, ++left, L++);
            else if (arr[L] > pivot)
                swap(arr, --right, L);
            else
                L++;
        }
        return new int[]{left + 1, right - 1};
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static class Pair {
        int x;
        int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
