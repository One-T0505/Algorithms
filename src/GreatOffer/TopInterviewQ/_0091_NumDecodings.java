package GreatOffer.TopInterviewQ;

import java.util.Arrays;

// 一条包含字母 A-Z 的消息通过以下映射进行了编码：
//'A' -> "1"
//'B' -> "2"
//...
//'Z' -> "26"
// 要解码已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。例如，"11106" 可以映射为：
// "AAJF" ，将消息分组为 (1 1 10 6)
// "KJF" ，将消息分组为 (11 10 6)
// 注意，消息不能分组为 (1 11 06) ，因为 "06" 不能映射为 "F" ，这是由于 "6" 和 "06" 在映射中并不等价。
// 给你一个只含数字的非空字符串 s ，请计算并返回解码方法的总数 。
// 题目数据保证答案肯定是一个 32 位的整数。

public class _0091_NumDecodings {

    public static int numDecodings(String s) {
        if (s == null || s.length() == 0)
            return 0;
        char[] chars = s.toCharArray();
        int N = chars.length;
        int[] dp = new int[N + 1];
        Arrays.fill(dp, -1);
        return f(chars, 0, dp);
    }

    private static int f(char[] chars, int i, int[] dp) {
        if (dp[i] != -1)
            return dp[i];
        int res = -1;
        if (i == chars.length)
            res = 1;
        else if (chars[i] == '0')
            res = 0;
        else {
            int ways = f(chars, i + 1, dp);
            if (i + 1 == chars.length)
                res = ways;
            else {
                int num = (chars[i] - '0') * 10 + (chars[i + 1] - '0');
                if (num <= 26)
                    ways += f(chars, i + 2, dp);
                res = ways;
            }
        }
        dp[i] = res;
        return res;
    }
    // ============================================================================================


    // 动态规划
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0)
            return 0;
        char[] chars = s.toCharArray();
        int N = chars.length;
        int[] dp = new int[N + 1];
        dp[N] = 1;
        for (int i = N - 1; i >= 0; i--) {
            if (chars[i] == '0')
                dp[i] = 0;
            else {
                int ways = dp[i + 1];
                if (i + 1 == N)
                    dp[i] = ways;
                else {
                    int num = (chars[i] - '0') * 10 + (chars[i + 1] - '0');
                    if (num <= 26)
                        ways += dp[i + 2];
                    dp[i] = ways;
                }
            }
        }
        return dp[0];
    }

}
