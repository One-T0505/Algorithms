package class15__Stock;

// leetCode121
// 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
// 你只能选择某一天买入这只股票，并选择在未来的某一个不同的日子卖出该股票。设计一个算法来计算你所能获取的最大利润。
// 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回0。

public class BestTimeTradeStock {

    // 遍历数组，让每个元素作为卖出的日期，去寻找何时买入能使利润最大化，这样结果就在其中。
    // 也就是说遍历每个元素时，只需要向前找出最小值即可。
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0)
            return 0;
        int min = prices[0];
        int res = 0;
        for (int i = 1; i < prices.length; i++) {
            res = Math.max(res, prices[i] - min);
            min = Math.min(min, prices[i]);
        }
        return res;
    }
}
