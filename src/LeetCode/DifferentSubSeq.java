package LeetCode;

/**
 * ymy
 * 2023/3/20 - 19 : 00
 **/


// 给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。

public class DifferentSubSeq {

    public static int numDistinct(String s, String t) {
        if (s == null || t == null || s.length() < t.length())
            return 0;
        // 执行到这里说明 s.length() >= t.length()
        if (t.equals(""))
            return 1;
        // 执行到这里，两个字符串的长度都不为0了
        char[] ori = s.toCharArray();
        char[] des = t.toCharArray();
        return dfs(ori, 0, des, 0);
    }

    private static int dfs(char[] ori, int i, char[] des, int j) {
        if (j == des.length)
            return 1;
        if (i == ori.length)
            return 0;
        int res = dfs(ori, i + 1, des, j);
        if (ori[i] == des[j])
            res += dfs(ori, i + 1, des, j + 1);
        return res;
    }



    // 动态规划
    public static int numDistinct2(String s, String t) {
        if (s == null || t == null || s.length() < t.length())
            return 0;
        // 执行到这里说明 s.length() >= t.length()
        if (t.equals(""))
            return 1;
        // 执行到这里，两个字符串的长度都不为0了
        char[] ori = s.toCharArray();
        char[] des = t.toCharArray();
        int N = ori.length;
        int M = des.length;
        // dp[i][j]表示：s[i..]这一段上有几个子串和t[j..]相等
        int[][] dp = new int[N + 1][M + 1];
        for (int i = 0; i <= N; i++) {
            dp[i][M] = 1;
        }
        for (int j = M - 1; j >= 0; j--){
            for (int i = N - M + j; i >= 0; i--){
                dp[i][j] = dp[i + 1][j] + (ori[i] == des[j] ? dp[i + 1][j + 1] : 0);
            }
        }
        return dp[0][0];
    }



    public static void main(String[] args) {
        String s = "tittztz";
        String t = "tzt";
        System.out.println(numDistinct(s, t));
    }
}
