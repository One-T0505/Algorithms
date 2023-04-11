package GreatOffer.TopHotQ;


// 给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。

public class _0416_PartitionEqualSubset {

    // 背包问题
    public boolean canPartition(int[] nums) {
        if (nums.length == 1)
            return false;
        int N = nums.length;
        int sum = 0;
        for (int e : nums)
            sum += e;
        if ((sum & 1) == 1) // 如果总和为奇数，那肯定分不出两个和相等的集合
            return false;
        sum >>= 1;  // sum现在变成了数组累加和的一半，也就是每个集合的目标值
        // dp[i][j]表示使用0..i这些数组元素，能否挑选出一些元素组成和为j的集合
        boolean[][] dp = new boolean[N][sum + 1];

        // 先处理第0列
        for (int i = 0; i < N; i++)
            dp[i][0] = true;
        // 再处理第0行
        if (nums[0] <= sum)
            dp[0][nums[0]] = true;

        for (int i = 1; i < N; i++) {
            for (int j = 1; j <= sum; j++) {
                // 先让[i][j]的值和[i-1][j]保持一致
                // 如果[i-1][j]==true  说明0..i-1就可以完成任务，那么0..i必然也可以，此时[i][j]的值就是
                // [i-1][j]
                // 如果[i-1][j]==false  说明0..i-1不可以完成任务，那么希望就全寄托在了新来的这个i元素
                // 所以要对nums[i]检查
                dp[i][j] = dp[i - 1][j];
                if (j - nums[i] >= 0) // nums[i]没超过当前要完成的任务j，那就还有希望继续检查
                    // 如果0..i-1能搞定j-nums[i]，那么0..i必然能搞定j
                    dp[i][j] |= dp[i - 1][j - nums[i]];
            }
            // 每一行做完就检查一下，保证使用尽量少的元素完成任务
            if (dp[i][sum])
                return true;
        }
        return false;
    }
}
