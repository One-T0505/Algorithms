package GreatOffer.class23;

// 给定一个数组arr，长度为N
// 从中间切一刀，保证左部分和右部分都有数字，一共有N-1种切法
// 如此多的切法中，每一种都有: 绝对值(左部分最大值-右部分最大值)
// 返回最大的绝对值是多少

public class _01_Code {

    // 最暴力的方法就不讲了，这里写一个稍微有点优化的，利用辅助数组将时间复杂度降为O(N)的方法

    public static int solutionV1(int[] arr) {
        if (arr == null || arr.length < 2)
            return 0;
        int N = arr.length;
        // 生成两个辅助数组left和right，left[i]表示0..i上最大值是多少
        // right[i]表示i..N-1上最大值是多少
        int[] left = new int[N];
        int[] right = new int[N];
        left[0] = arr[0];
        right[N - 1] = arr[N - 1];
        for (int i = 1; i < N; i++) {
            left[i] = Math.max(left[i - 1], arr[i]);
            right[N - 1 - i] = Math.max(right[N - i], arr[N - 1 - i]);
        }

        int res = 0;
        for (int i = 0; i < N - 1; i++)
            res = Math.max(left[i], right[i + 1]);

        return res;
    }


    // 最优化的方法。先找出全局最大值max，结果就是：max - Math.min(arr[0], arr[N-1])
    // 为什么？首先找到max后，因为要将数组分成两段，所以max不是在左部分就是在右部分。
    //                  |
    // [..........max...|.......]      如果max在左部分，那么右部分最少都要包含一个N-1位置的元素，
    //                  |
    // 我们想让(max-右部分最大值)尽可能大，那就是让右部分最大值尽可能小，但是右部分怎么都绕不开N-1位置的值，所以
    // 只能让右部分尽可能少地包含元素，这样就能减小包含住比N-1位置更大值的可能。
    // 当max被分到右部分时的情况一样。

    public static int solutionV2(int[] arr) {
        if (arr == null || arr.length < 2)
            return 0;
        int N = arr.length;
        int max = Integer.MIN_VALUE;
        for (int j : arr) max = Math.max(max, j);
        return max - Math.min(arr[0], arr[N - 1]);
    }
}
