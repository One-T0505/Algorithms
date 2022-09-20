package DynamicProgramming;

public class CommonSubsequence {
    // 求两个字符串的最长公共子序列，注意是子序列不是子串。
    // eg：str1 = "abc1de23f4gh5"  str2 = "ijk123lm4no5" 的最长公共子序列为12345
    public static int longestCommonSubsequence(String str1, String str2){
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int rows = chars1.length, cols = chars2.length;
        // 建立一张dp表，dp[i][j] 表示str1从0～i，str2从0～j上的最长公共子序列长度
        int[][] dp = new int[rows][cols];

        dp[0][0] = chars1[0] == chars2[0] ? 1 : 0;

        for (int row = 1; row < rows; row++)
            dp[row][0] = Math.max(dp[row - 1][0], chars1[row] == chars2[0] ? 1 : 0);

        for (int col = 1; col < cols; col++)
            dp[0][col] = Math.max(dp[0][col - 1], chars1[0] == chars2[col] ? 1 : 0);

        // 最普通的dp[i][j]的值有4种情况：
        // 1.str1[0~i] 和 str2[0~j] 的最后一位不相同，eg：agt1w2ff3r  xv123l  123是最长公共子序列
        //   所以此时两个的最后一位去除也不影响结果，所以 dp[i][j] == dp[i-1][j-1]
        // 2.str1[0~i] 和 str2[0~j] 的最长公共子序列包含str1[i]，不包含str2[j], eg：12awc3   1t23ajh
        //   此时  dp[i][j] == dp[i][j-1]
        // 3.str1[0~i] 和 str2[0~j] 的最长公共子序列不包含str1[i]，包含str2[j], eg：12awc3m   1t23
        //   此时  dp[i][j] == dp[i-1][j]
        // 4.str1[0~i] 和 str2[0~j] 的最长公共子序列既包含str1[i]，又包含str2[j]，也就是说str1[i]==str2[j]
        //   eg：1e2ghj3   1olp2vb3   此时dp[i][j] == dp[i-1][j-1] + 1
        for (int row = 1; row < rows; row++) {
            for (int col = 1; col < cols; col++) {
                dp[row][col] = Math.max(dp[row - 1][col], dp[row][col - 1]);
                if (chars1[row] == chars2[col])
                    dp[row][col] = Math.max(dp[row][col], dp[row - 1][col - 1] + 1);
            }
        }
        return dp[chars1.length - 1][chars2.length - 1];
    }


    public static void main(String[] args) {
        String s1 = "avc12gr3oie4h5";
        String s2 = "1frffo23tyu4mlv5";
        System.out.println(longestCommonSubsequence(s1, s2));
    }
}
