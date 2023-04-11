package GreatOffer.class22;

// 接雨水问题一   leetCode42
// 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
// 假如arr=[1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]  那么如下图所示，每一条都是柱子，●表示能存住的雨水
//
//  |
//  |
//  |
//  |                            ___
//  |            ___            |   |___     ___
//  |    ___    |   |_●_  ●  _●_|   |   |_●_|   |___
//  |___|___|_●_|___|___|_●_|___|___|___|___|___|___|____
//

public class RainWaterI {

    // 思路：我们要以每一根柱子上能存多少个雨水为基点，最后的累加和就是结果。
    // 首先两端的柱子是没法存住水的；剩下的每个柱子，我们需要找到其左边的最大值left和右边的最大值right，然后
    // Math.min(left, right)然后求出较小值min，这个min就是当前这个柱子的瓶颈；如果min > height[i]，那么
    // min - height[i] 就是i这个柱子上能存住的水；如果 min <= height[i] 那么i这个柱子存不住水

    public static int droppingWaterV1(int[] height) {
        if (height == null || height.length < 3)
            return 0;
        int N = height.length;
        // left[i]  表示i左边最大值    right[i]  表示i右边最大值
        int[] left = new int[N];
        int[] right = new int[N];
        // 先完成left数组
        left[1] = height[0];
        for (int i = 2; i < N; i++)
            left[i] = Math.max(left[i - 1], height[i - 1]);
        // 再完成right数组
        right[N - 2] = height[N - 1];
        for (int i = N - 3; i >= 0; i--)
            right[i] = Math.max(right[i + 1], height[i + 1]);
        int res = 0;
        for (int i = 1; i < N - 1; i++) {
            int bottleNeck = Math.min(left[i], right[i]);
            res += Math.max(bottleNeck - height[i], 0);
        }
        return res;
    }
    // ===============================================================================================


    // 优化版的方法可以将空间复杂度优化为：o(1)
    // 用L、R分别指示一个柱子，L==1，R==N-2  因为0和N-1这两个位置没办法存雨水。
    // 初始时，L左边的最大值和R右边的最大值是确定的，L、R谁移动取决于L左侧的最大值和R右侧的最大值谁比较小；
    // 如果L、R的瓶颈一样大，那么可以同时结算L和R。
    // 比如：[12, 7, 20, ... , 13, 9, 18]   一开始L指向7，R指向9；
    //           L                R
    // L左侧最大值是确定的12，R右侧的最大值是18，所以12比较小，那么现在需要结算L这个柱子的水量，并让其向右移动

    public static int droppingWaterV2(int[] height) {
        if (height == null || height.length < 3)
            return 0;
        int N = height.length;
        int res = 0;
        int L = 1, R = N - 2;
        // left表示：从0..L-1位置中的最大值，就是说L已经划过的位置里的最大值
        int left = height[0];
        // right表示：从R+1..N-1位置中的最大值，就是说R已经划过的位置里的最大值
        int right = height[N - 1];
        while (L <= R) {
            // 上面说相等时可以一起结算，但是这里为了写代码方便我就选择相等时让L结算就可以了
            if (left <= right) {
                res += Math.max(left - height[L], 0);
                left = Math.max(left, height[L++]);
            } else {
                res += Math.max(right - height[R], 0);
                right = Math.max(right, height[R--]);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] height = {2, 0, 2};
        System.out.println(droppingWaterV2(height));
    }
}
