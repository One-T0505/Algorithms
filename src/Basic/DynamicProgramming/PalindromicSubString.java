package Basic.DynamicProgramming;

// leetCode0005
// 给你一个字符串 s，找到 s 中最长的回文子串。

public class PalindromicSubString {

    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0)
            return null;
        if (s.length() == 1)
            return s;
        // 记录最终结果的左边界  为什没有右边界？ 因为左边界 + 长度 就可以推算出右边界
        int resL = 0;  // 此时这里可以随便填，因为整个字符串就算都不回文，那至少也可以有一个
        int maxLen = 1;
        char[] chs = s.toCharArray();
        int N = chs.length;
        // dp[i][j]表示s[i..j]是否为回文串  我们只用包含主对角线的右上部分
        boolean[][] dp = new boolean[N][N];
        // 先填主对角线和次对角线
        for (int i = 0; i < N - 1; i++) {
            dp[i][i] = true;
            dp[i][i + 1] = chs[i] == chs[i + 1];
            if (dp[i][i + 1]){
                resL = i;
                maxLen = 2;
            }
        }
        dp[N - 1][N - 1] = true;


        // 这种方式是遍历对角线的写法，要熟练掌握
        for (int startCol = 2; startCol < N; startCol++) {
            int L = 0;
            int R = startCol;
            while (R < N){
                // 当只有3个字符的时候  因为处理了主对角线和次对角线，所以只有for循环的第一轮R-L==3，之后都是>3
                // 当两边的字符不等的时候可以直接false
                if (chs[L] != chs[R]){
                    dp[L][R] = false;
                } else {
                    if (R - L < 3)
                        dp[L][R] = true;
                    else
                        dp[L][R] = dp[L + 1][R - 1];
                }
                if (dp[L][R] && R - L + 1 > maxLen){
                    maxLen = R - L + 1;
                    resL = L;
                }
                L++;
                R++;
            }
        }
        return s.substring(resL, resL + maxLen);
    }





    public static void main(String[] args) {
        System.out.println(longestPalindrome("aaaa"));
    }

}
