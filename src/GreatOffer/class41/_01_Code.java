package GreatOffer.class41;

import utils.arrays;

import java.util.Arrays;

// 来自小红书
// 一个无序数组长度为n,所有数字都不一样，并且值都在[0.. .n-1]范围上
// 返回让这个无序数组变成有序数组的最小交换次数

public class _01_Code {

    // 下标循环怼算法
    public static int minSwap(int[] arr) {
        int times = 0;
        for (int i = 0; i < arr.length; i++) {
            while (arr[i] != i) {
                swap(arr, i, arr[i]);
                times++;
            }
        }
        return times;
    }


    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    // 关键是如何写出一个暴力解来做对数器
    public static int minSwap2(int[] arr) {
        return f(arr, 0);
    }


    // 该递归表示将arr变成有序数组还需要多少次交换，pre是已经交换的次数
    private static int f(int[] arr, int pre) {
        boolean sorted = true;
        int N = arr.length;
        for (int i = 1; i < N; i++) {
            if (arr[i] < arr[i - 1]) {
                sorted = false;
                break;
            }
        }
        if (sorted)  // 数组已经完全有序
            return pre;
        // 长度为N的数组，最差的情况下让数组有序也只需要N-1次  如果之前交换的次数已经达到了N-1次还没让数组有序
        // 那就直接返回最大值，不用再继续做下去了，因为这个方式已经不可能是最小值了
        if (pre >= N - 1)
            return Integer.MAX_VALUE;
        int res = Integer.MAX_VALUE;
        // 枚举任何两个交换的位置，找出最小值
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                swap(arr, i, j);
                res = Math.min(res, f(arr, pre + 1));
                swap(arr, i, j);
            }
        }
        return res;
    }
    // =============================================================================================


    // for test
    // 随机生成一个长度为N的数组，数值范围为0～N-1，且数组是乱序的
    private static int[] randomArr(int maxLen) {
        int N = ((int) (Math.random() * maxLen) + 1);
        int[] arr = new int[N];
        for (int j = 0; j < N; j++) {
            arr[j] = j;
        }
        for (int j = 0; j < N; j++) {
            int pos = (int) (Math.random() * N);
            swap(arr, j, pos);
        }
        return arr;
    }


    public static void test(int testTime, int maxLen) {
        for (int i = 0; i < testTime; i++) {
            int[] arr = randomArr(maxLen);
            int[] copy = Arrays.copyOf(arr, arr.length);
            int res2 = minSwap2(arr);
            int res1 = minSwap(arr);
            if (res1 != res2) {
                System.out.println("Failed");
                System.out.println("暴力解：" + res2);
                System.out.println("优化解：" + res1);
                arrays.printArray(copy);
                return;
            }
        }
        System.out.println("AC");
    }


    public static void main(String[] args) {
        test(1000, 6);
    }
}
