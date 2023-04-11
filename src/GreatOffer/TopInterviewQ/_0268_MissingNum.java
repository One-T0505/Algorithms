package GreatOffer.TopInterviewQ;

// 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
// n == nums.length
// 1 <= n <= 10^4
// 0 <= nums[i] <= n
// nums 中的所有数字都独一无二

public class _0268_MissingNum {

    public static int missingNumber(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        int N = nums.length;
        // 按理说 0～N 的累加和是 sum，我们遍历一遍数组，求出数组的累加和 tmp，tmp 和 sum 的差就是缺的那个数
        int sum = (N * (N + 1)) >> 1;
        int tmp = 0;
        for (int e : nums)
            tmp += e;
        return sum - tmp;
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }


    public static void main(String[] args) {
        int[] arr = {3, 0, 1};
        System.out.println(missingNumber(arr));
    }

}
