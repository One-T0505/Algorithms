package GreatOffer.TopHotQ;

import java.util.TreeMap;

/**
 * ymy
 * 2023/3/16 - 16 : 31
 **/

// 给你一个整数数组 nums 和一个整数 k ，请你统计并返回该数组中和为 k 的连续子数组的个数。

public class _0560_FixedSumSubArr {

    public int subarraySum(int[] nums, int k) {
        if (nums == null || nums.length == 0)
            return 0;
        int N = nums.length;
        TreeMap<Integer, Integer> dp = new TreeMap<>();
        dp.put(0, 1);
        int sum = 0;
        int res = 0;
        for (int i = 0; i < N; i++) {
            sum += nums[i];
            res += dp.getOrDefault(sum - k, 0);
            dp.put(sum, dp.getOrDefault(sum, 0) + 1);
        }
        return res;
    }
}
