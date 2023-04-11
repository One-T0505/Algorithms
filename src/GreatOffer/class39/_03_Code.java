package GreatOffer.class39;

// 来自百度
// 给定一个字符串s和一个正整数k
// s的子序列的字符种数必须是k种，返回有多少子序列满足这个条件  s中全部都是小写字母

public class _03_Code {

    // 如果给的字符串是：[a a b b c c]  k==3  那么选a的方式有：[0, 1, (0,1)] 3种
    // 同样选b的方式有3种，选c的方式有3种，那一共有3^3==27种。
    // 可以得出一个结论，如果给的字符串字符种类确定了，每种字符的数量也确定了，那么不管这个字符串以哪种排列顺序，
    // 其答案都是一样的。比如给你：[c b b a c a] 答案也是27种。既然和顺序无关了，那么给定的字符串我们可以
    // 以词频表的形式给出，原先的字符串s就无用了。
    // 如果字符种类数d>k了，可以定义一个从左至右的递归尝试模型，到当前字符种类了，选不选用该字符类。

    public static int subSequence(String s, int k) {
        if (s == null || s.length() < k)
            return 0;
        char[] chs = s.toCharArray();
        int[] count = new int[26];
        for (char c : chs)
            count[c - 'a']++;
        // 该方法表示：从0号位置开始做决策到结束位置，凑出k种字符的子序列有多少种
        return ways(count, 0, k);
    }


    // rest表示还需要几种字符
    private static int ways(int[] count, int i, int rest) {
        if (rest == 0)
            return 1;
        if (i == count.length) // rest != 0 但是已经没字符可选了
            return 0;
        // 不选用当前的字符种类
        int p1 = ways(count, i + 1, rest);
        // 选用当前的字符种类 如果选用当前种类的字符，当前字符一共有count[i]==n个，那么选择这个字符的方式有：
        // C(n,1) + C(n,2) + C(n,3) + ... + C(n,n) == 2^n - 1
        //  选1个    选2个     选3个
        int p2 = 0;
        if (count[i] > 0)
            p2 = ((1 << count[i]) - 1) * ways(count, i + 1, rest - 1);
        return p1 + p2;
    }
    // 两个可变参数的递归改写成动态规划
    // ==========================================================================================


    public static int subSequence2(String s, int k) {
        if (s == null || s.length() < k)
            return 0;
        char[] chars = s.toCharArray();
        int[] count = new int[26];
        for (char c : chars)
            count[c - 'a']++;
        int[][] dp = new int[27][k + 1];
        for (int i = 0; i < 27; i++)
            dp[i][0] = 1;
        for (int i = 25; i >= 0; i--) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = dp[i + 1][j];
                dp[i][j] += count[i] > 0 ? ((1 << count[i]) - 1) * dp[i + 1][j - 1] : 0;
            }
        }
        return dp[0][k];
    }


    public static void main(String[] args) {
        String s = "cbawsjacwwjbbac";
        int k = 4;
        System.out.println(subSequence(s, k));
        System.out.println(subSequence2(s, k));
    }

}
