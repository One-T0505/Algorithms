package Hard;

import java.util.Arrays;

// leetCode377
// 给你一个由不同整数组成的数组 nums ，和一个目标整数 target 。请你从 nums 中找出并返回总和为 target 的
// 元素组合的个数。
// 题目数据保证答案符合 32 位整数范围。
// 输入：nums = [1,2,3], target = 4    输出：7
// 解释：
//  所有可能的组合为：(1, 1, 1, 1)   (1, 1, 2)   (1, 2, 1)   (1, 3)  (2, 1, 1)   (2, 2)  (3, 1)
//  请注意，顺序不同的序列被视作不同的组合。

// 数据规模
// 1 <= nums.length <= 200
// 1 <= nums[i] <= 1000
// nums 中的所有元素 互不相同
// 1 <= target <= 1000

public class FixedSum {

    // 因为这个题目说顺序不同也视为不同的序列，所以我们不能像正常背包那样尝试。
    // 我们应该以谁作为target的第一部分作为切入点，这样就能找到所有结果.

    public static int combinationSum4(int[] nums, int target) {
        if (nums == null || nums.length == 0 || target < 1)
            return 0;
        return ways(nums, target);
    }


    // 还需要用nums凑出rest，返回可行的方法数
    private static int ways(int[] nums, int rest) {
        if (rest < 0) // rest < 0 并且nums是正数数组，则不可能有方法
            return 0;
        if (rest == 0)
            return 1;
        int res = 0;
        for (int num : nums) {
            res += ways(nums, rest - num);
        }
        return res;
    }
    // ============================================================================================



    // 暴力递归改成记忆化搜索
    public static int combinationSum42(int[] nums, int target) {
        if (nums == null || nums.length == 0 || target < 1)
            return 0;
        int res = 0;
        int[] dp = new int[target + 1];
        Arrays.fill(dp, 0, target + 1, -1);
        return f(target, nums, dp);
    }


    private static int f(int rest, int[] nums, int[] dp) {
        if (rest < 0)
            return 0;
        if (dp[rest] != -1)
            return dp[rest];
        int res = 0;
        if (rest == 0)
            res = 1;
        else {
            for (int num : nums)
                res += f(rest - num, nums, dp);
        }
        dp[rest] = res;
        return res;
    }
}
