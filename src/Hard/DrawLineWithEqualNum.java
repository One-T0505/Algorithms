package Hard;

// leetCode1035
// 在两条独立的水平线上按给定的顺序写下 nums1 和 nums2 中的整数。
// 现在，可以绘制一些连接两个数字 nums1[i] 和 nums2[j] 的直线，这些直线需要同时满足：
//  1.nums1[i] == nums2[j]
//  2.且绘制的直线不与任何其他连线（非水平线）相交。
// 请注意，连线即使在端点也不能相交：每个数字只能属于一条连线。
// 以这种方法绘制线条，并返回可以绘制的最大连线数。

// 1 <= nums1.length, nums2.length <= 500
// 1 <= nums1[i], nums2[j] <= 2000


public class DrawLineWithEqualNum {

    //  1  2  4           1和1可以相连，2和2可以相连，但是来到nums1[2]==4时，虽然num2中也有4，但是如果
    //  |    \            相连了，就会和(2,2)的线相交，所以不可画。
    //  1  4  2

    // 1  7  2  5        最多可以连3条，如果7和7相连了，那么2和5都不能相连了，损失太大，所以选择7不连。
    // |    /  /
    // 1  2  5  7


    // 这个题的难点在于很难找到原型，这道题目其实就是求两个数组的最长公共子序列的长度。

    public static int maxUncrossedLines(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0 || nums2 == null || nums2.length == 0)
            return 0;
        int N = nums1.length;
        int M = nums2.length;
        // dp[i][j]表示 nums1[0..i] 和 nums2[0..j] 有多少个公共子序列
        int[][] dp = new int[N][M];
        dp[0][0] = nums1[0] == nums2[0] ? 1 : 0;
        // 先处理第1行
        for (int j = 1; j < M; j++)
            dp[0][j] = nums1[0] == nums2[j] ? 1 : dp[0][j - 1];
        // 再处理第1列
        for (int i = 1; i < N; i++)
            dp[i][0] = nums1[i] == nums2[0] ? 1 : dp[i - 1][0];
        // 剩余位置
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (nums1[i] == nums2[j])
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
            }
        }
        return dp[N - 1][M - 1];
    }
}
