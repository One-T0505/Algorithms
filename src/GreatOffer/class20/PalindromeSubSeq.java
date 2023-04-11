package GreatOffer.class20;

// 该题目和 leetCode730 很像，只不过本题相同字面值也算，而 730题相同字面值不算
// 给定一个字符串str， 当然可以生成很多子序列
// 返回有多少个子序列是回文子序列，空序列不算回文
// 比如, str = 'aba'  回文子序列: {a}、{a}、 {aa}、 {b}、 {a,b,a}
// 返回5   即便字面值一样，但是位置不同，也算不同的结果

public class PalindromeSubSeq {

    // 动态规划。定义一张二维dp表，dp[i][j] 表示 s[i..j]这一段有多少个回文子序列
    // 最后返回dp[0][N-1]
    public static int solution(String s) {
        if (s == null)
            return 0;
        if (s.length() < 2) // 长度为0就没有，长度为1就一个
            return s.length();
        if (s.length() == 2)
            return s.charAt(0) == s.charAt(1) ? 3 : 2;
        char[] chs = s.toCharArray();
        int N = chs.length;
        // 左下三角区无效
        int[][] dp = new int[N][N];
        // 主对角线上都为1
        for (int i = 0; i < N; i++)
            dp[i][i] = 1;
        // 次对角线，由于只有两个字符，比较好讨论，所以可以提前填好
        for (int i = 0; i < N; i++)
            dp[i][i + 1] = chs[i] == chs[i + 1] ? 3 : 2;
        // 填剩余元素 对于一般位置来说我们需要分成4种情况来讨论dp[i][j]
        //  1.必须不选i，必须不选j，在str[i+1..j-1]这段能有多少个回文子序列  假设结果为a
        //  2.必须不选i，必须选j，在str[i+1..j]这段能有多少个回文子序列  假设结果为b
        //  3.必须选i，必须不选j，在str[i..j-1]这段能有多少个回文子序列  假设结果为c
        //  4.必须选i，必须选j，在str[i..j]这段能有多少个回文子序列  假设结果为d
        // 对于每个一般位置dp[i][j]，我们需要依赖dp[i+1][j]  dp[i][j-1]  dp[i+1][j-1] 这三个格子
        // 也就是说要利用这三个格子的值拼凑出 a+b+c+d
        // 如果str[i] != s[j] 那么这种情况下就没有第4种情况，只需要搞定a+b+c即可
        //  dp[i+1][j]的含义是str[i+1..j]有多少个回文子序列，也就是说i+1..j每个字符都可选可不选，所以
        //  dp[i+1][j] == a + b  同样地，dp[i][j-1] == a + c   dp[i+1][j-1] == a
        //  所以 a + b + c == dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1]
        //
        // 如果str[i] == s[j]  那么第4种情况d也存在 所以要拼凑出 a+b+c+d  a+b+c 上面已经搞定了，只需要搞定d即可
        // 因为str[i] == s[j]，所以dp[i+1][j-1]的每一种回文子序列的左侧加一个str[i]，右侧加一个str[j]，都是
        // 一个新的回文子序列，所以 d == dp[i+1][j-1]
        // 这还没完，因为该题目中空串是不算回文串的，所以dp[i+1][j-1]肯定不包含选空串的情况，
        // 但是在str[i..j]一定要选i，j的同时，s[i]==s[j]，所以str[i+1..j-1]选空串，左右各加一个str[i]和
        // s[j]也是一个新的回文子序列，所以最终 d == dp[i+1][j-1] + 1

        for (int j = 2; j < N; j++) {
            for (int i = 0; i < N - j; i++) {
                // 不管str[i]是否和str[j]相等，dp[i][j]的值都至少是a+b+c
                dp[i][j] = dp[i + 1][j] + dp[i][j - 1] - dp[i + 1][j - 1];
                if (chs[i] == chs[j])
                    dp[i][j] += dp[i + 1][j - 1] + 1;
            }
        }
        return dp[0][N - 1];
    }
}
