package GreatOffer.TopInterviewQ;


// 给你一个整数数组 nums，返回数组 answer，其中 answer[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。
// 题目数据保证数组 nums 之中任意元素的全部前缀元素和后缀的乘积都在 32 位整数范围内。
// 请不要使用除法，且在 O(n) 时间复杂度内完成此题。

public class _0238_ProductExceptSelf {

    // 该题目要求返回一个数组，所以在返回该数组前可以使用该数组的空间临时作辅助数组使用，这样就不算作额外空间复杂度
    public int[] productExceptSelf(int[] nums) {
        int N = nums.length;
        // dp[i]表示：从i..N-1这些元素的累乘积
        int[] res = new int[N];
        res[N - 1] = nums[N - 1];
        for (int i = N - 2; i >= 0; i--)
            res[i] = nums[i] * res[i + 1];
        // 如果arr=[2, 3, 1, 4, 2]  那么res=[48, 24, 8, 8, 2] 如何让res的每个元素正确更新呢？
        // 这里需要使用一个left变量，初始为1，表示左边的元素的累乘。
        // res[0]的正确结果就是：res[1] * left, res[1]表示0右边所有元素的乘积，left表示0左边所有的元素乘积，
        // 这样就凑出了所有元素除了0位置元素的积；left每次都要乘上当前元素。
        int left = 1;
        for (int i = 0; i < N; i++) {
            res[i] = (i + 1 >= N ? 1 : res[i + 1]) * left;
            left *= nums[i];
        }
        return res;
    }
    // ========================================================================================


    // 扩展。如果只能在原数组上变出结果并返回呢？不能申请额外空间作为输出了；并且要求不能使用除号

}
