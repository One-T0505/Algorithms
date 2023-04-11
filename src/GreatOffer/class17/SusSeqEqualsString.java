package class17;


// 给定两个字符串S和T，返回S的所有子序列中有多少个子序列的字面值等于T。

public class SusSeqEqualsString {


    // 很明显的动态规划中的样本对应模型。定义一个二维缓存表，用行表示s，列表示t。
    // dp[i][j]的含义：s[0..i]的子序列中有多少个和t[0..j]的字面值相等。那么，对角线上的值很容易填，并且
    // 该缓存表的上半部分没用，因为s的长度<t的长度

    public static int equalSubSequence(String s, String t) {
        if (s == null || t == null || s.length() < t.length())
            return 0;
        if (t.length() == 0)
            return 1;
        // 执行到这里说明，s.length >= t.length && t.length >= 1
        char[] sChar = s.toCharArray();
        char[] tChar = t.toCharArray();
        int N = sChar.length;
        int M = tChar.length;
        int[][] dp = new int[N][M];
        dp[0][0] = sChar[0] == tChar[0] ? 1 : 0;
        // 先填第0列
        for (int i = 1; i < N; i++)
            dp[i][0] = dp[i - 1][0] + (sChar[i] == tChar[0] ? 1 : 0);
        // 再填主对角线
        for (int i = 1; i < M; i++)
            dp[i][i] = sChar[i] == tChar[i] ? 1 : 0;
        // 最后填其他部分
        for (int j = 1; j < M; j++) {
            for (int i = j + 1; i < N; i++) {
                dp[i][j] = dp[i - 1][j];
                dp[i][j] += sChar[i] == tChar[j] ? dp[i - 1][j - 1] : 0;
            }
        }
        return dp[N - 1][M - 1];

    }

    public static void main(String[] args) {
        String t = "13";
        String s = "1212311112121132";
        System.out.println(equalSubSequence(s, t));

    }
}
