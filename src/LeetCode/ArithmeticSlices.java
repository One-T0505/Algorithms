package LeetCode;

/**
 * ymy
 * 2023/3/28 - 10 : 47
 **/

// leetCode413
// 如果一个数列至少有三个元素 ，并且任意两个相邻元素之差相同，则称该数列为等差数列。
// 例如，[1,3,5,7,9]、[7,7,7,7] 和 [3,-1,-5,-9] 都是等差数列。
// 给你一个整数数组 nums ，返回数组 nums 中所有为等差数组的子数组个数。

public class ArithmeticSlices {

    public static int numberOfArithmeticSlices(int[] nums) {
        if (nums == null || nums.length < 3)
            return 0;
        int N = nums.length;
        int ways = 0;
        int preNum = 0;
        // 枚举每个等差数列的最后两项。i  i+1  就是这两项。
        for (int i = 1; i < N - 1; i++) {
            // res 用于记录在当前以 i 和 i+1 作为数列最后两项，能向前找到多少个长度 >=3 的等差数列
            int res = 0;
            // 由这两个数确定的公差
            int diff = nums[i] - nums[i + 1];
            // 如果公差和上一组的公差相等，那就直接可以利用上一组的结果。preNum 就是以 i-1 和 i  作为数列
            // 的最后两项向前能找到的符合要求的数列个数。  比如 ..... 3 5 7
            // 以(3,5)作为最后两项向前找到了5个，那么以(5,7)作为结尾能找到的一共就是 5 + 1 个，因为多了一个 {3,5,7}
            // 这个组合。如果5后面跟的不是 7，那么就可以直接将 preNum = 0
            if (diff == nums[i - 1] - nums[i]){
                res = preNum + 1;
                ways += res;
            }
            // 如果不相等，那么就将 preNum = 0，因为不相等，所以 res还是0，这里这样赋值就没问题
            preNum = res;
        }
        return ways;
    }


    public static void main(String[] args) {
        int[] arr = {1, 2};
        System.out.println(numberOfArithmeticSlices(arr));
    }
}
