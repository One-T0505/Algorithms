package class15__Stock;

// leetCode122
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
// 在每一天，你可以决定是否购买或出售股票。你在任何时候最多只能持有一股股票。你也可以先购买，然后在同一天出售。
// 不限交易次数。只有手里没有股票时才可购买，即：可以在一天内先卖出再买。
// 返回你能获得的最大利润。

public class BestTimeTradeStockII {

    // 可以将整个数组画成一个波形图，把所有的(波峰 - 波谷)累加就是结果。
    // 即：每一次只在波谷买进，然后再下一个最高点卖出。
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0)
            return 0;
        int res = 0;
        for (int i = 1; i < prices.length; i++) {
            // 如果是在上升就累加，如果不是，就加0
            res += Math.max(prices[i] - prices[i - 1], 0);
        }
        return res;
    }
}
