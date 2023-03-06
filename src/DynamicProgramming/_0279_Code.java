package DynamicProgramming;

// 给你一个整数 n ，返回和为 n 的完全平方数的最少数量 。

public class _0279_Code {


    // dp[i]就可以表示和为i的完全平方数的最少数量 那怎么写状态转移呢？
    // 我们只能枚举，组成i的最后一个完全平方数是多少，1、4。。。 j  j必须<=i
    public static int numSquares(int n) {
        int[] dp = new int[n + 1];
        // dp[0]=0
        for (int i = 1; i <= n; i++) {
            int res = i;
            for (int j = (int) Math.sqrt(i); j >= 1; j--){
                res = Math.min(res, dp[i - j * j] + 1);
                if (res == 1)
                    break;
            }
            dp[i] = res;
        }
        return dp[n];
    }


    public static void main(String[] args) {
        System.out.println(numSquares(12));
    }
}
