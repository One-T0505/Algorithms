package GreatOffer.TopInterviewQ;

// 给定一个字符串 (s) 和一个字符模式 (p) ，实现一个支持 '?' 和 '*' 的通配符匹配。
//  '?' 可以匹配任何单个字符。
//  '*' 可以匹配任意字符串（包括空字符串）。
// 两个字符串完全匹配才算匹配成功。
// s 可能为空，且只包含从 a-z 的小写字母。
// p 可能为空，且只包含从 a-z 的小写字母，以及字符 ? 和 *。

public class _0044_RegularMatch {

    // 根据官方给出的测试用例，又总结出了以下规定：
    //  1. * 可以单独使用，不需要依附别的字母；并且也可以作为开头
    //  2. * 可以当作 "" 使用

    public static boolean isMatch(String s, String p) {
        if (s == null || p == null)
            return false;
        char[] ori = s.toCharArray();
        char[] re = p.toCharArray();
        int N = ori.length;
        int M = re.length;
        // 0 表示没计算过  -1 表示 false   1 表示 false
        int[][] dp = new int[N][M];
        return f(ori, 0, re, 0, dp);
    }

    private static boolean f(char[] ori, int i, char[] re, int j, int[][] dp) {
        if (i == ori.length && j == re.length)
            return true;
        if (j == re.length) // 并且 i != ori.length
            return false;
        if (i == ori.length){ // 并且 j != re.length
            // 碰到这种情况，就是原始串已经用完了，匹配串还没用完，那我们就看从当前到最后是否全是 *
            // 如果全是，那还能匹配上，如果不是那就直接失败
            for (int k = j; k < re.length; k++) {
                if (re[k] != '*')
                    return false;
            }
            return true;
        }
        // 执行到这里说明两个都还没用完
        if (dp[i][j] != 0)
            return dp[i][j] == 1;
        int res = -1;
        if (re[j] == '*'){
            for (int k = i; k <= ori.length; k++) {
                if (f(ori, k, re, j + 1, dp)){
                    res = 1;
                    break;
                }
            }
        } else if ((re[j] == '?' || re[j] == ori[i]) && f(ori, i + 1, re, j + 1, dp))
            res = 1;
        // re[j]是字母，但是和 ori[i]不相等  那么直接返回 false，因为初始化 res 就是 -1，所以这种情况就不用写
        dp[i][j] = res;
        return res == 1;
    }
    // ----------------------------------------------------------------------------------------------




    // 改成动态规划
    public static boolean isMatch2(String s, String p) {
        if (s == null || p == null)
            return false;
        char[] ori = s.toCharArray();
        char[] re = p.toCharArray();
        int N = ori.length;
        int M = re.length;
        // dp[i][j] 表示 ori[i..] 和 re[j..] 能否匹配上
        boolean[][] dp = new boolean[N + 1][M + 1];
        // dp[0..N-1][M] == 0 最后一列全为0，除了最下面的 dp[N][M]
        dp[N][M] = true;
        // 再来处理最后一行
        boolean isAll = true;
        for (int j = M - 1; j >= 0; j--) {
            if (isAll)
                isAll = re[j] == '*';
            dp[N][j] = isAll;
        }
        // 再处理一般位置
        for (int i = N - 1; i >= 0; i--){
            for (int j = M - 1; j >= 0; j--){
                if (re[j] == '*'){
                    for (int k = i; k <= N; k++) {
                        if (dp[k][j + 1]){
                            dp[i][j] = true;
                            break;
                        }
                    }
                } else if ((re[j] == '?' || re[j] == ori[i]) && dp[i + 1][j + 1])
                    dp[i][j] = true;
            }
        }
        return dp[0][0];
    }
    // -----------------------------------------------------------------------------------------------





    // 第二种思路的动态规划。也是最优解
    public static boolean isMatch3(String s, String p) {
        if (s == null || p == null)
            return false;
        char[] ori = s.toCharArray();
        char[] re = p.toCharArray();
        int N = ori.length;
        int M = re.length;
        // dp[i][j] 表示 ori的前i个字符 能否和 re 的前 j 个字符匹配上
        boolean[][] dp = new boolean[N + 1][M + 1];
        // 第0列，除了[0,0]位置其余全部为false，非常容易判断出来
        dp[0][0] = true;
        // 但是第0行就不是那么容易了。dp[0][j] 表示 ori的前0个字符能否和 re 的前j个字符匹配上
        // 单反有一个不是 '*'  就不可能匹配上
        for (int j = 1; j <= M; j++){
            if (re[j - 1] != '*')
                break;
            dp[0][j] = true;
        }
        // 再处理一般位置
        for (int i = 1; i <= N; i++){
            for (int j = 1; j <= M; j++){
                if (re[j - 1] == '?' || re[j - 1] == ori[i - 1])
                    dp[i][j] = dp[i - 1][j - 1];
                else if (re[j - 1] == '*') {
                    // 如果我们不使用这个星号，那么就会从 dp[i][j−1] 转移而来；
                    // 如果我们使用这个星号，那么就会从 dp[i−1][j] 转移而来。
                    dp[i][j] = dp[i - 1][j] || dp[i][j - 1];

                }
            }
        }
        return dp[N][M];
    }





    public static void main(String[] args) {
        String s = "acdcb";
        String p = "?*b*";
        System.out.println(isMatch(s, p));
    }
}
