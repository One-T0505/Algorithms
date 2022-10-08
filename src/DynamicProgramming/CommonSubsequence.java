package DynamicProgramming;

// 求两个字符串的最长公共子序列，注意是子序列不是子串。
// eg：str1 = "abc1de23f4gh5"  str2 = "ijk123lm4no5" 的最长公共子序列为12345
public class CommonSubsequence {

    // 暴力递归方法
    public static int longestCommonSubsequenceV1(String str1, String str2){
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0)
            return 0;
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        return process1(chars1, chars1.length - 1, chars2, chars2.length - 1);
    }

    // 该方法表示：chars1字符数组从0～end1这个范围上，和chars2字符数组从0～end2这个范围上的最长公共子序列长度
    private static int process1(char[] chars1, int end1, char[] chars2, int end2) {
        if (end1 == 0 && end2 == 0)
            return chars1[end1] == chars2[end2] ? 1 : 0;
        // end1 == 0 && end2 != 0
        else if (end1 == 0)
            return chars1[0] == chars2[end2] ? 1 : process1(chars1, end1, chars2, end2 - 1);
        // end1 != 0 && end2 == 0
        else if (end2 == 0)
            return chars1[end1] == chars2[0] ? 1 : process1(chars1, end1 - 1, chars2, end2);
        // end1 != 0 && end2 != 0
        else {
            // 1.str1[0~i] 和 str2[0~j] 的最后一位不相同，eg：agt1w2ff3r  xv123l  123是最长公共子序列
            //   所以此时两个的最后一位去除也不影响结果;
            //   str1[0~i] 和 str2[0~j] 的最长公共子序列既包含str1[i]，又包含str2[j]，也就是说str1[i]==str2[j]
            int p1 = chars1[end1] == chars2[end2] ?
                    (1 + process1(chars1, end1 - 1, chars2, end2 - 1)) : 0;
            // 2.str1[0~i] 和 str2[0~j] 的最长公共子序列包含str1[i]，不包含str2[j], eg：12awc3   1t23ajh
            int p2 = process1(chars1, end1 - 1, chars2, end2);
            // 3.str1[0~i] 和 str2[0~j] 的最长公共子序列不包含str1[i]，包含str2[j], eg：12awc3m   1t23
            int p3 = process1(chars1, end1, chars2, end2 - 1);

            return Math.max(p1, Math.max(p2, p3));
        }
    }
    // =================================================================================================


    // 动态规划法：发现可变参数只有两个，并且范围是从：0~str1.length-1  0~str2.length-1
    // 所以申请一个矩阵即可表示缓存
    public static int longestCommonSubsequenceV3(String str1, String str2){
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int rows = chars1.length;
        int cols = chars2.length;
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
        String s1 = "avc12gr3oie4h5fgkw";
        String s2 = "1frffo23tyu4mlv5in";
        long start1 = System.currentTimeMillis();
        int res1 = longestCommonSubsequenceV1(s1, s2);
        long end1 = System.currentTimeMillis();
        System.out.println(res1 + "\t" + (end1 - start1) + "ms");
        long start2 = System.currentTimeMillis();
        int res2 = longestCommonSubsequenceV3(s1, s2);
        long end2 = System.currentTimeMillis();
        System.out.println(res2 + "\t" + (end2 - start2) + "ms");

    }
}
