package class07;


import utils.arrays;

import java.util.Arrays;
import java.util.HashSet;


public class DifferentSquare {

    // 给定一个有序数组arr，其中值可能为正、负、0。返回arr中每个数都平方之后不同的结果有多少种?


    // 暴力解法
    public static int diffSquareV1(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length == 1)
            return 1;
        HashSet<Integer> set = new HashSet<>();
        for (int elem : arr)
            set.add(Math.abs(elem));
        return set.size();
    }


    // 思路：首尾指针。比较绝对值即可，谁的绝对值大哪个指针就移动。说几种边界情况。
    //      1：  -8 -8 -8 -6 -3 0 4 5 7 7 9 9 9   首尾-8、9不等，9的绝对值较大，所以指针往左移，并且要一直移动到
    //         第一个不等的地方；左边指针也是一样的道理。
    //      2：  -8 -8 -8 -6 -3 0 4 5 8 8 9 9 9     首尾-8、9不等，9的绝对值较大，所以指针往左移到第一个8，发现此时
    //         左右指针绝对值相等，两个指针需要同时移动，但是如果接下来依然相等并且和上一次相等的值一样，此时不再记录新的值。

    public static int diffSquareV2(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length == 1)
            return 1;
        int N = arr.length;
        int L = 0, R = N - 1;
        int left = 0, right = 0;
        int res = 0;
        while (L <= R) {
            left = Math.abs(arr[L]);
            right = Math.abs(arr[R]);
            if (left < right) {
                while (R >= 0 && Math.abs(arr[R]) == right)
                    R--;
            } else if (left > right) {
                while (L < N && Math.abs(arr[L]) == left)
                    L++;
            } else {
                while (L < N && Math.abs(arr[L]) == left)
                    L++;
                while (R >= 0 && Math.abs(arr[R]) == right)
                    R--;
            }
            res++;
        }
        return res;
    }


    // for test
    public static void test(int testTime, int maxLen, int maxVal) {
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.RandomArr(maxLen, maxVal);
            Arrays.sort(arr);
            int res1 = diffSquareV1(arr);
            int res2 = diffSquareV2(arr);
            if (res1 != res2) {
                System.out.println("Failed");
                System.out.println("暴力解法：" + res1);
                System.out.println("优化解法：" + res2);
                arrays.printArray(arr);
                return;
            }
        }
        System.out.println("AC");
    }
    // =======================================================================================================


    // 下面这道题和上面的类似
    // 给定一个数组arr，先递减然后递增，返回arr中有多少个不同的数字?


    public static void main(String[] args) {
        test(10000, 15, 30);
    }
}
