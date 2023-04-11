package GreatOffer.TopInterviewQ;

import java.util.Arrays;

// 1~A 2~B... 编码消息中可能包含 '*' 字符，可以表示从 '1' 到 '9' 的任一数字（不包括 '0'。例如，
// 编码字符串 "1*" 可以表示 "11"、"12"、"13"、"14"、"15"、"16"、"17"、"18" 或 "19" 中的任意一条消息。
// 对 "1*" 进行解码，相当于解码该字符串可以表示的任何编码消息。
// 给你一个字符串 s ，由数字和 '*' 字符组成，返回解码该字符串的方法数目。
// 由于答案数目可能非常大，返回 109 + 7 的 模 。


public class _0639_NumDecodingsII {

    public static long mod = 1000000007;

    public static int numDecodings(String s) {
        if (s == null || s.length() == 0)
            return 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        long[] dp = new long[N + 1];
        Arrays.fill(dp, -1);
        return (int) f(chs, 0, dp);
    }

    private static long f(char[] chars, int i, long[] dp) {
        if (dp[i] != -1)
            return dp[i];
        long ways = 0;
        if (i == chars.length)
            ways = 1;
        else if (chars[i] == '0')
            ways = 0;
        else if (chars[i] != '*') {  // 1 <= chars[i] <= 9
            ways = f(chars, i + 1, dp);  // 单独转
            if (i + 1 != chars.length) { // 有下一个字符
                if (chars[i + 1] != '*') {  // 下一个字符不是 '*'
                    int num = (chars[i] - '0') * 10 + (chars[i + 1] - '0');
                    if (num < 27)
                        ways += f(chars, i + 2, dp);
                } else { // chars[i + 1] == '*'
                    long p2 = f(chars, i + 2, dp);
                    if (chars[i] == '1')
                        ways += 9 * p2;   // 11～19
                    else if (chars[i] == '2')  // 21～26
                        ways += 6 * p2;
                }
            }
        } else { // chars[i] == '*'
            ways = f(chars, i + 1, dp) * 9;
            if (i + 1 != chars.length) {  // 有下一个字符
                if (chars[i + 1] != '*') { // 下一个字符是数字0～9
                    // 如果下一个字符在0~6之间，那么前一个位置的*都可以有两种匹配：1和2
                    ways += (chars[i + 1] < '7' ? 2 : 1) * f(chars, i + 2, dp);
                } else { // 下一个字符也是 '*'
                    // ** 的组合方式：11～19 + 21～26 == 15
                    ways += f(chars, i + 2, dp) * 15;
                }
            }
        }
        ways %= mod;
        dp[i] = ways;
        return ways;
    }
    // ===========================================================================================


    // 上面的记忆化搜索已经是完全可以通过了，但是为了后续改写成动态规划，我们需要将代码的逻辑整合得更加精简
    private static long g(char[] chars, int i, long[] dp) {
        if (dp[i] != -1)
            return dp[i];
        long ways = 0;
        if (i == chars.length)
            ways = 1;
        else if (chars[i] == '0')
            ways = 0;
        else {
            boolean flag = chars[i] == '*';
            ways = f(chars, i + 1, dp) * (flag ? 9 : 1);
            if (i + 1 != chars.length) {  // 有下一个字符
                long p = f(chars, i + 2, dp);
                if (chars[i + 1] == '*') { // 下一个字符也是*
                    ways += p * (flag ? 15 : (chars[i] == '1' ? 9 : (chars[i] == '2' ? 6 : 0)));
                } else { // 下一个字符不是* 而是数字
                    if (chars[i] != '*') {
                        int num = (chars[i] - '0') * 10 + (chars[i + 1] - '0');
                        ways += num < 27 ? p : 0;
                    } else
                        ways += (chars[i + 1] < '7' ? 2 : 1) * p;
                }
            }
        }
        ways %= mod;
        dp[i] = ways;
        return ways;
    }
    // ===========================================================================================


    // 动态规划
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0)
            return 0;
        char[] chars = s.toCharArray();
        int N = chars.length;
        long[] dp = new long[N + 1];
        dp[N] = 1L;
        for (int i = N - 1; i >= 0; i--) {
            long ways = 0L;
            if (chars[i] == '0')
                ways = 0;
            else {
                boolean flag = chars[i] == '*';
                ways = dp[i + 1] * (flag ? 9 : 1);
                if (i + 1 != N) {  // 有下一个字符
                    long p = dp[i + 2];
                    if (chars[i + 1] == '*') { // 下一个字符也是*
                        ways += p * (flag ? 15 : (chars[i] == '1' ? 9 : (chars[i] == '2' ? 6 : 0)));
                    } else { // 下一个字符不是* 而是数字
                        if (chars[i] != '*') {
                            int num = (chars[i] - '0') * 10 + (chars[i + 1] - '0');
                            ways += num < 27 ? p : 0;
                        } else
                            ways += (chars[i + 1] < '7' ? 2 : 1) * p;
                    }
                }
            }
            ways %= mod;
            dp[i] = ways;
        }
        return (int) dp[0];
    }
    // =========================================================================================


    // 继续状态压缩，因为发现一个状态仅仅依靠i+1的状态和i+2的状态
    public static int numDecodings3(String s) {
        if (s == null || s.length() == 0)
            return 0;
        char[] chars = s.toCharArray();
        int N = chars.length;
        long next = 1L;
        long nextNext = 0L;
        for (int i = N - 1; i >= 0; i--) {
            long ways = 0L;
            if (chars[i] == '0')
                ways = 0;
            else {
                boolean flag = chars[i] == '*';
                ways = next * (flag ? 9 : 1);
                if (i + 1 != N) {  // 有下一个字符
                    long p = nextNext;
                    if (chars[i + 1] == '*') { // 下一个字符也是*
                        ways += p * (flag ? 15 : (chars[i] == '1' ? 9 : (chars[i] == '2' ? 6 : 0)));
                    } else { // 下一个字符不是* 而是数字
                        if (chars[i] != '*') {
                            int num = (chars[i] - '0') * 10 + (chars[i + 1] - '0');
                            ways += num < 27 ? p : 0;
                        } else
                            ways += (chars[i + 1] < '7' ? 2 : 1) * p;
                    }
                }
            }
            ways %= mod;
            nextNext = next;
            next = ways;
        }
        return (int) next;
    }
}
