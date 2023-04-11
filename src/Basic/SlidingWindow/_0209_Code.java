package Basic.SlidingWindow;

// 给定一个含有 n 个正整数的数组和一个正整数 target 。
// 找出该数组中满足其和 ≥ target 的长度最小的连续子数组 [numsl, numsl+1, ..., numsr-1, numsr] ，
// 并返回其长度。如果不存在符合条件的子数组，返回 0。

public class _0209_Code {

    public int minSubArrayLen(int target, int[] nums) {
        int N = nums.length;
        int res = Integer.MAX_VALUE;
        int sum = 0;
        int L = 0, R = 0;
        while (R < N){
            while (R < N && sum < target){
                sum += nums[R++];
            }
            // 出来后，R就来到了当前L为左边界时，第一个>=target的位置
            // 也有可能整个数组的累加和都<target 然后R出界了
            if (R == N && sum < target)
                break;
            // 如果能执行到这里，说明只有可能是
            //  1. R < N && sum < target
            //  2. R == N && sum >= target
            //  3. R < N && sum >= target
            // 因为有上面的while把关，所以不可能是情况1，只有可能是情况2和3，那也就是说此时sum必然>=target
            while (sum >= target)
                sum -= nums[L++];
            res = Math.min(res, R - L + 1);
        }
        return res == Integer.MAX_VALUE ? 0 : res;
    }
}
