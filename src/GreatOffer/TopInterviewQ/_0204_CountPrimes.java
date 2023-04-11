package GreatOffer.TopInterviewQ;


// 给定整数 n ，返回 所有小于非负整数 n 的质数的数量。

public class _0204_CountPrimes {

    public int countPrimes(int n) {
        if (n < 3)
            return 0;
        // dp[i]==true，说明i已经不是质数了
        boolean[] dp = new boolean[n];
        int count = n >> 1;   // 所有的偶数不可能是质数，只有剩下的一半有可能是质数.

        for (int i = 3; i * i < n; i += 2) { // 仅从奇数开始尝试
            if (dp[i])
                continue;
            // 每次从 3*3  3*5 3*7 3*9....尝试
            for (int j = i * i; j < n; j += i << 1) {
                if (!dp[j]) {
                    count--;
                    dp[j] = true;
                }
            }
        }
        return count;
    }
}
