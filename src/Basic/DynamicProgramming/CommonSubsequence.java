package Basic.DynamicProgramming;

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
    // str1[0...i]和str2[0...j]，这个范围上最长公共子序列长度是多少？
    // 可能性分类:
    // a) 最长公共子序列，一定不以str1[i]字符结尾、也一定不以str2[j]字符结尾
    // b) 最长公共子序列，可能以str1[i]字符结尾、但是一定不以str2[j]字符结尾
    // c) 最长公共子序列，一定不以str1[i]字符结尾、但是可能以str2[j]字符结尾
    // d) 最长公共子序列，必须以str1[i]字符结尾、也必须以str2[j]字符结尾
    // 注意：a)、b)、c)、d)并不是完全互斥的，他们可能会有重叠的情况
    // 但是可以肯定，答案不会超过这四种可能性的范围
    // 那么我们分别来看一下，这几种可能性怎么调用后续的递归。
    // a) 最长公共子序列，一定不以str1[i]字符结尾、也一定不以str2[j]字符结尾
    //    如果是这种情况，那么有没有str1[i]和str2[j]就根本不重要了，因为这两个字符一定没用啊
    //    所以砍掉这两个字符，最长公共子序列 = str1[0...i-1]与str2[0...j-1]的最长公共子序列长度(后续递归)
    // b) 最长公共子序列，可能以str1[i]字符结尾、但是一定不以str2[j]字符结尾
    //    如果是这种情况，那么我们可以确定str2[j]一定没有用，要砍掉；但是str1[i]可能有用，所以要保留
    //    所以，最长公共子序列 = str1[0...i]与str2[0...j-1]的最长公共子序列长度(后续递归)
    // c) 最长公共子序列，一定不以str1[i]字符结尾、但是可能以str2[j]字符结尾
    //    跟上面分析过程类似，最长公共子序列 = str1[0...i-1]与str2[0...j]的最长公共子序列长度(后续递归)
    // d) 最长公共子序列，必须以str1[i]字符结尾、也必须以str2[j]字符结尾
    //    同时可以看到，可能性d)存在的条件，一定是在str1[i] == str2[j]的情况下，才成立的
    //    所以，最长公共子序列总长度 = str1[0...i-1]与str2[0...j-1]的最长公共子序列长度(后续递归) + 1(共同的结尾)
    // 综上，四种情况已经穷尽了所有可能性。四种情况中取最大即可
    // 其中b)、c)一定参与最大值的比较，
    // 当str1[i] == str2[j]时，a)一定比d)小，所以d)参与
    // 当str1[i] != str2[j]时，d)压根不存在，所以a)参与
    // 但是再次注意了！
    // a)是：str1[0...i-1]与str2[0...j-1]的最长公共子序列长度
    // b)是：str1[0...i]与str2[0...j-1]的最长公共子序列长度
    // c)是：str1[0...i-1]与str2[0...j]的最长公共子序列长度
    // a)中str1的范围 < b)中str1的范围，a)中str2的范围 == b)中str2的范围
    // 所以a)不用求也知道，它比不过b)啊，因为有一个样本的范围比b)小啊！
    // a)中str1的范围 == c)中str1的范围，a)中str2的范围 < c)中str2的范围
    // 所以a)不用求也知道，它比不过c)啊，因为有一个样本的范围比c)小啊！
    // 至此，可以知道，a)就是个垃圾，有它没它，都不影响最大值的决策
    // 所以，当str1[i] == str2[j]时，b)、c)、d)中选出最大值
    // 当str1[i] != str2[j]时，b)、c)中选出最大值
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
            // 这就是a和d，如果不相等就设为0，因为就是个垃圾
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

        for (int i = 1; i < rows; i++)
            dp[i][0] = Math.max(dp[i - 1][0], chars1[i] == chars2[0] ? 1 : 0);

        for (int j = 1; j < cols; j++)
            dp[0][j] = Math.max(dp[0][j - 1], chars1[0] == chars2[j] ? 1 : 0);

        // 最普通的dp[i][j]的值有4种情况：
        // 1.str1[0~i] 和 str2[0~j] 的最后一位不相同，eg：agt1w2ff3r  xv123l  123是最长公共子序列
        //   所以此时两个的最后一位去除也不影响结果，所以 dp[i][j] == dp[i-1][j-1]
        // 2.str1[0~i] 和 str2[0~j] 的最长公共子序列包含str1[i]，不包含str2[j], eg：12awc3   1t23ajh
        //   此时  dp[i][j] == dp[i][j-1]
        // 3.str1[0~i] 和 str2[0~j] 的最长公共子序列不包含str1[i]，包含str2[j], eg：12awc3m   1t23
        //   此时  dp[i][j] == dp[i-1][j]
        // 4.str1[0~i] 和 str2[0~j] 的最长公共子序列既包含str1[i]，又包含str2[j]，也就是说str1[i]==str2[j]
        //   eg：1e2ghj3   1olp2vb3   此时dp[i][j] == dp[i-1][j-1] + 1
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (chars1[i] == chars2[j])
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
            }
        }
        return dp[rows - 1][cols - 1];
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
