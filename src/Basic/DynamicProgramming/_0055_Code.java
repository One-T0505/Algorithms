package Basic.DynamicProgramming;

// 跳跃游戏
// 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 判断你是否能够到达最后一个下标。

public class _0055_Code {


    // 暴力递归
    public static boolean canJump(int[] nums) {
        if (nums.length == 1)
            return true;
        if (nums[0] == 0)
            return false;
        return f(nums, 0);
    }

    private static boolean f(int[] nums, int i) {
        if (i == nums.length - 1)
            return true;
        if (nums[i] == 0)
            return false;
        for (int j = 1; j <= nums[i]; j++) {
            if (f(nums, i + j))
                return true;
        }
        return false;
    }




    // 动态规划+剪枝
    public static boolean canJump2(int[] nums) {
        if (nums.length == 1)
            return true;
        if (nums[0] == 0)
            return false;
        int N = nums.length;
        boolean[] dp = new boolean[N];
        dp[N - 1] = true;
        for (int i = N - 2; i >= 0; i--){
            // nums[i] == 0 直接不可能从i到终点，所以直接设为false，因为默认值就是false，所以只用管不为0的情况
            if (nums[i] != 0){
                // 不为0，那nums[i] >= 1 是必然的，所以保底就有相邻元素的值
                dp[i] = dp[i + 1];
                if (!dp[i]){ // 如果从i+1能到，那直接就不用在做后续了
                    // 如果 1 <= nums[i] <= nums[i + 1] + 1  这个范围内，其实就是dp[i+1]的范围
                    if (nums[i] > nums[i + 1] + 1){
                        int s = i + nums[i + 1] + 2;
                        int e = i + nums[i];
                        while (s <= e){
                            dp[i] |= dp[s];
                            if (dp[i])
                                break;
                            s += nums[s] + 1;
                        }
                    }
                }
            }
        }
        return dp[0];
    }
}
