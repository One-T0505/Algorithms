package GreatOffer.TopInterviewQ;


// 给你一个数组，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。

public class _0189_RotateArray {


    // 这道题目之前讲过，三次逆序即可完成
    public void rotate(int[] nums, int k) {
        int N = nums.length;
        k = k % N;
        reverse(nums, 0, N - k - 1);
        reverse(nums, N - k, N - 1);
        reverse(nums, 0, N - 1);
    }

    private void reverse(int[] nums, int L, int R) {
        while (L < R) {
            int tmp = nums[L];
            nums[L++] = nums[R];
            nums[R--] = tmp;
        }
    }
}
