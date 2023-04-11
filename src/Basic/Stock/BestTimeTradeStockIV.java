package class15__Stock;

// leetCode188
// 给定一个整数数组 prices ，它的第 i 个元素 prices[i] 是一支给定的股票在第 i 天的价格。
// 设计一个算法来计算你所能获取的最大利润。你最多可以完成 k 笔交易。
// 注意：你不能同时参与多笔交易，你必须在再次购买前出售掉之前的股票。

public class BestTimeTradeStockIV {

    // 思路：假设数组长度为N，则最多有N/2个上升波段。所以如果给的k>=N/2，其实就相当于可以交易无限次，那和第二题
    //      完全一样。如果k<N/2，就得用动态规划来做。构建一张二维dp表，dp[i][j]表示，只在0..i上交易，并且交易次数
    //      不超过j次，能获得的最大收益。

    public static int maxProfit(int[] prices, int k) {
        if (prices == null || prices.length == 0 || k < 1)
            return 0;
        int N = prices.length;
        if (k >= (N >> 1))
            return class15__Stock.BestTimeTradeStockII.maxProfit(prices);
        // k < N/2
        int[][] dp = new int[N][k + 1];
        // dp[0..N-1][0] = 0  第0列永远都是0    dp[0][0..N-1] = 0  第0行也都是0
        for (int j = 1; j <= k; j++) {
            for (int i = 1; i < N; i++) {
                int p1 = dp[i - 1][j]; // arr[r]不参与交易
                // arr[r]参与交易，只能是最后一次交易的卖出日期，此时就需要枚举
                int p2 = 0;
                for (int t = i; t >= 0; t--)
                    p2 = Math.max(p2, dp[t][j - 1] + prices[i] - prices[t]);
                dp[i][j] = Math.max(p1, p2);
            }
        }
        return dp[N - 1][k];
    }


    // 斜率优化
    // 按照上面的转移方程，dp[5][2]的值是怎么得到的？
    //
    //                |---情况1  dp[4][2]
    //    dp[5][2] ---|           ｜--- dp[5][1] + arr[5] - arr[5]
    //                |---情况2 ---| --- dp[4][1] + arr[5] - arr[4]
    //                            | --- dp[3][1] + arr[5] - arr[3]
    //                            | --- dp[2][1] + arr[5] - arr[2]
    //                            | --- dp[1][1] + arr[5] - arr[1]
    //                            | --- dp[0][1] + arr[5] - arr[0]
    //
    //   先从情况2中找出最大值，然后再和情况1选出最大值。再看下 dp[6][2] 的推导过程？
    //
    //                |---情况1  dp[5][2]
    //                |           | --- dp[6][1] + arr[6] - arr[6]
    //    dp[6][2] ---|           ｜--- dp[5][1] + arr[6] - arr[5]
    //                |---情况2 ---| --- dp[4][1] + arr[6] - arr[4]
    //                            | --- dp[3][1] + arr[6] - arr[3]
    //                            | --- dp[2][1] + arr[6] - arr[2]
    //                            | --- dp[1][1] + arr[6] - arr[1]
    //                            | --- dp[0][1] + arr[6] - arr[0]
    //
    // 先从情况2中找出最大值，然后再和情况1选出最大值。于是可以从dp[5][2]的推导过程中简化dp[6][2]的推导。
    // 可以先将dp[5][2]的情况2写成这样的形式：
    // dp[5][1] - arr[5]   dp[5][1] - arr[4]   dp[5][1] - arr[3]   dp[5][1] - arr[2]
    // dp[5][1] - arr[1]   dp[5][1] - arr[0]
    // 先从这里面选出最大值用变量cache记录，然后再加上arr[5]，结果和上面的dp[5][2]情况2计算的结果是一样的，为什么要
    // 这样写，当然是为了记录下重复的计算过程，因为dp[6][2]也可以用到cache,只需要让cache再和 dp[6][1] - arr[6]
    // 比较下大小即可，然后再把结果加上arr[6]就是dp[6][2]的情况2的所有情况，于是就省去了枚举行为。

    public static int maxProfitV2(int[] prices, int k) {
        if (prices == null || prices.length == 0 || k < 1)
            return 0;
        int N = prices.length;
        if (k >= (N >> 1))
            return class15__Stock.BestTimeTradeStockII.maxProfit(prices);
        // k < N/2
        int[][] dp = new int[N][k + 1];
        for (int j = 1; j <= k; j++) {
            int cache = Math.max(dp[1][j - 1] - prices[1], dp[0][j - 1] - prices[0]);
            dp[1][j] = Math.max(cache + prices[1], dp[0][j]);
            for (int i = 2; i < N; i++) {
                cache = Math.max(cache, dp[i][j - 1] - prices[i]);
                dp[i][j] = Math.max(dp[i - 1][j], cache + prices[i]);
            }
        }
        return dp[N - 1][k];
    }


    // leetcode上188题的原题。该题很重要，因为123题就是直接利用上面的解题模型解决的。123题就是让k==2
}
