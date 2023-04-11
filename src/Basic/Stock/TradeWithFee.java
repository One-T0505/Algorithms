package class15__Stock;

// leetCode714
// 给定一个整数数组 prices，其中 prices[i]表示第 i 天的股票价格 ；整数 fee 代表了交易股票的手续费用。
// 你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。
// 返回获得利润的最大值。
// 注意：这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。

public class TradeWithFee {


    // 这个和题目4非常类似，只是取消了冷却限制。
    public int maxProfit(int[] prices, int fee) {
        if (prices == null || prices.length < 2)
            return 0;
        // 我们将每笔费用规定在买入时结算
        int N = prices.length;
        // 0..0   0 -[0] - fee
        int bestBuy = -prices[0] - fee;
        int bestSell = 0;
        for (int i = 1; i < N; i++) {
            // 来到i位置了！
            // 如果在i必须买  收入 - 批发价 - fee
            int curBuy = bestSell - prices[i] - fee;
            // 如果在i必须卖  整体最优（收入 - 良好批发价 - fee）
            int curSell = bestBuy + prices[i];
            bestBuy = Math.max(bestBuy, curBuy);
            bestSell = Math.max(bestSell, curSell);
        }
        return bestSell;
    }
}
