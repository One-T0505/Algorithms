package GreatOffer.class39;

// 一个子序列的消除规则如下:
//  1) 在某一个子序列中，如果'1'的左边有'0',那么这两个字符->"01"可以消除
//  2) 在某一个子序列中，如果'3'的左边有'2',那么这两个字符->" 23"可以消除
//  3) 当这个子序列的某个部分消除之后，认为其他字符会自动贴在一起，可以继续寻找消除的机会
// 比如，某个子序列"0231",先消除掉"23",那么剩下的字符贴在一起变成"01", 继续消除就没有字符了
// 如果某个子序列通过最优良的方式，可以都消掉，那么这样的子序列叫做“全消子序列”
// 一个只由'0'、'1'、 '2'、 '3' 四种字符组成的字符串str,可以生成很多子序列，返回“全消子序列”的最大长度
// 字符串str长度<= 200

public class _05_Code {

    public static int maxDisappear(String s) {
        if (s == null || s.length() == 0)
            return 0;
        return f(s.toCharArray(), 0, s.length() - 1);
    }


    // f(L,R)表示：只考虑str[L..R]的字符，返回这段区域上能消除的最长子序列的长度
    private static int f(char[] str, int L, int R) {
        if (L >= R) // L==R 说明只有一个字符，没东西可消，返回0  L>R，非法位置，返回0，表示没东西可消
            return 0;
        if (L == R - 1) // 有两个字符
            // 两个条件中1个就能消除  否则没东西可消
            return (str[L] == '0' && str[R] == '1') || (str[L] == '2' && str[R] == '3') ? 2 : 0;
        // 如果执行到了这里，说明 L <= R - 2  区间里至少有3个字符
        // 可能性1：能消掉的子序列完全不考虑str[L]，最长是多少
        int p1 = f(str, L + 1, R);
        // 可能性2：能消掉的子序列考虑str[L]，最长是多少
        // 如果str[L]==1 或 3  那就没有可能性2了，因为L根本不可能在[L..R]上被消除
        if (str[L] == '1' || str[L] == '3')
            return p1;
        // 此时说明 str[L] == 0 或 2
        int p2 = 0;
        char aim = str[L] == '0' ? '1' : '3'; // 确定要找的字符
        // 枚举所有能和L位置消的位置
        for (int i = L + 1; i <= R; i++) {
            // 如果找到了位置i可以和L消除，那么 f(L+1, i-1)就是L和i之间的那部分最长能消除多少
            // 2 就是L和i互消   f(i+1, R) 就是剩余部分最长能消除多少
            if (str[i] == aim)
                p2 = Math.max(p2, f(str, L + 1, i - 1) + 2 + f(str, i + 1, R));
        }
        return Math.max(p1, p2);
    }



    // 改成动态规划
    public static int maxDisappear2(String s) {
        if (s == null || s.length() == 0)
            return 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int[][] dp = new int[N][N];
        // 左下角去区域全部弃用，并且主对角线上全部填0，因为只有1个字符
        // 先处理次对角线
        for (int i = 0; i < N - 1; i++)
            dp[i][i + 1] =
                ((chs[i] == '0' && chs[i + 1] == '1') || (chs[i] == '2' && chs[i + 1] == '3')) ? 2 : 0;

        // 再处理剩余位置
        for (int i = N - 3; i >= 0; i--){
            for (int j = i + 2; j < N; j++){
                dp[i][j] = dp[i + 1][j];
                if (chs[i] == '0' || chs[i] == '2'){
                    int p2 = 0;
                    char aim = chs[i] == '0' ? '1' : '3'; // 确定要找的字符
                    // 枚举所有能和L位置消的位置
                    for (int k = i + 1; i <= j; i++) {
                        if (chs[k] == aim)
                            p2 = Math.max(p2, dp[i + 1][k - 1] + 2 + dp[k + 1][j]);
                    }
                    dp[i][j] = Math.max(dp[i][j], p2);
                }
            }
        }
        return dp[0][N - 1];
    }
}
