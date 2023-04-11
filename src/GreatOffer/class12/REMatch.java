package class12;

// leetCode10
// 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
// '.' 匹配任意单个字符     '*' 匹配零个或多个前面的那一个元素
// 所谓匹配，是要涵盖整个字符串s的，而不是部分字符串。

public class REMatch {

    // 这个很明显是样本对应模型。申请一张二维缓存表即可。用行表示字符串s，用列表示字符规律p，dp[i][j] 表示
    // 从s[i...] 能否和 p[j..] 匹配上。

    public static boolean isMatchV1(String str, String exp) {
        if (str == null || exp == null)
            return false;
        char[] s = str.toCharArray();
        char[] p = exp.toCharArray();
        return isValid(s, p) && f(s, 0, p, 0);
    }


    // 要先对字符串的有效性进行检查，因为代码量不小，所以单独封装成一个方法
    private static boolean isValid(char[] s, char[] p) {
        // 首先，s中不能有 . *
        for (char c : s)
            if (c == '.' || c == '*')
                return false;
        // 匹配串中，开头的不能是'*'，并且不能有相邻的'*'
        for (int i = 0; i < p.length; i++) {
            if (p[i] == '*' && (i == 0 || p[i - 1] == '*'))
                return false;
        }
        return true;
    }


    // 从s[i...]能否和p[j...]匹配上.  我们不是以j位置来做决策，而是以j+1位置来做决策。因为后面可能会有'*'的
    // 存在改变当前的结果。我们的可能性划分。假如j+1不是'*'，那么 p[j] == '.' 和 p[j] == s[i] 至少要成立一个。
    // 如果j+1位置是'*'，情况有很多，下面逐一分析：
    //  1> 如果 s[i] != p[j] 那么j+1处的'*'的作用只能是将j处字符变为0个。让j->j+2 i不变继续匹配
    //  2> 如果 s[i] == p[j] 比如：
    //     s[i..] = [a, a, a, a, b]
    //     p[j..] = [a, *, a, b]
    //     那我可以让a*变成0个a，然后调f(s, i, p, j + 2)
    //     也可以让a*变成1个a，然后调f(s, i+1, p, j + 2)
    //     也可以让a*变成2个a，然后调f(s, i+2, p, j + 2)
    //     也可以让a*变成3个a，然后调f(s, i+3, p, j + 2)
    //     也可以让a*变成4个a，然后调f(s, i+4, p, j + 2)
    //   因为s中最多只有4个连续的a，所以只能调用这些分支，这些分支有一个能走通就返回true。所以调用的分支有哪些，取决于
    //   s串的情况。

    private static boolean f(char[] s, int i, char[] p, int j) {
        if (j == p.length)  // 如果匹配串用完了，那么如果原始串还剩任何东西，那就不可能再匹配成了
            return i == s.length;
        // 这个if想表达的就是p[j+1]不是'*'
        if (j + 1 == p.length || p[j + 1] != '*')
            return i != s.length && (p[j] == s[i] || p[j] == '.') && f(s, i + 1, p, j + 1);

        while (i != s.length && (p[j] == s[i] || p[j] == '.')) {
            if (f(s, i, p, j + 2))
                return true;
            i++;
        }
        // 跳出while后，i==s.length || p[j]与s[i]匹配不上
        // 也有可能是一开始 s[i] 就和 p[j] 匹配不上  所以直接跳过了 while 循环
        return f(s, i, p, j + 2);
    }
    // ===============================================================================================


    // 记忆化搜索
    // 这里不可以直接用boolean型的缓存表，因为没有办法用值表示这个格子是否算过没。因为boolean型初始化为false==0，
    // 我们无法判断是没算过还是算过了之后返回的结果就是false。所以可以用一个int型缓存表，用0表示没算过，-1表示false，
    // 1表示true。
    public static boolean isMatchV2(String str, String exp) {
        if (str == null || exp == null)
            return false;
        char[] s = str.toCharArray();
        char[] p = exp.toCharArray();
        if (!isValid(s, p))
            return false;
        int[][] dp = new int[s.length + 1][p.length + 1];

        return g(s, 0, p, 0, dp);
    }

    private static boolean g(char[] s, int i, char[] p, int j, int[][] dp) {
        if (dp[i][j] != 0)
            return dp[i][j] == 1;
        boolean res = false;
        if (j == p.length) {  // 如果匹配串用完了，那么如果原始串还剩任何东西，那就不可能再匹配成了
            res = i == s.length;
        } else {
            // 这个if想表达的就是p[j+1]不是'*'
            if (j + 1 == p.length || p[j + 1] != '*') {
                res = i != s.length && (p[j] == s[i] || p[j] == '.') && g(s, i + 1, p, j + 1, dp);
            } else {
                while (i != s.length && (p[j] == s[i] || p[j] == '.')) {
                    if (g(s, i, p, j + 2, dp)) {
                        res = true;
                        break;
                    }
                    i++;
                }
                // 如果上面的while已经有机会把它变成了true，那么此时的或运算，不管 g 是什么答案都不影响最终结果。
                // 如果整个while都没有将res变成true，那么 g 就是必须要走的。
                // 所以，不管怎么样，或上 g 的结果都不影响最终结果。
                res |= g(s, i, p, j + 2, dp);
            }
        }
        dp[i][j] = res ? 1 : -1;
        return res;
    }
    // 这里讲一个问题。就是说采用记忆化搜索的时候，我们的策略是对一个结果变量赋值，考虑所有情况下该值的结果，
    // 最后将其记录下来才返回，也就是说不能像暴力递归时，只要找到答案了，就直接返回了，记忆化搜索就是记录值，方便
    // 节省重复计算。所以，记忆化搜索是不可以中途返回的，那么所有情况都要过一遍。g方法中为啥用了break，因为想实现
    // 和f中一样的逻辑，只要找到了一个答案就中断，但是g中是不能直接return的，所以g中才要用这样的方式来写：
    // res |= g(s, i, p, j + 2, cache);  这样的方式就保证了res==false才会执行该递归。 因为f中，最后一个
    // 的return语句的递归，只有是前面的while循环都不中，才能执行到这里，所以在g中，我们也要保持一样的逻辑，不能改
    // 变某些语句可执行的时机。
    // =============================================================================================


    // 记忆化搜索+斜率优化
    // 这种有枚举行为的，要很敏感地发现是否可以继续斜率优化省去枚举行为。假设：
    // s = [a, a, a, a, b] 第一个位置为12   p = [a, *, ]  第一个位置为14。
    // 所以，当我们计算dp[12][14]时，我们依赖的是：
    // dp[12][14] = dp[12][16] || dp[13][16] || dp[14][16] || dp[15][16] || dp[16][16]
    // 当我们计算dp[13][14]时，我们依赖的是：
    // dp[13][14] = dp[13][16] || dp[14][16] || dp[15][16] || dp[16][16]
    //
    // 所以，可以将其优化为：dp[12][14] = dp[13][14] || dp[12][16]
    // 只需要g函数即可，主函数一模一样
    private static boolean h(char[] s, int i, char[] p, int j, int[][] dp) {
        if (dp[i][j] != 0)
            return dp[i][j] == 1;
        boolean res = false;
        if (j == p.length) {  // 如果匹配串用完了，那么如果原始串还剩任何东西，那就不可能再匹配成了
            res = i == s.length;
        } else { // 说明此时匹配串还有字符
            // 这个if想表达的就是p[j+1]不是'*'
            if (j + 1 == p.length || p[j + 1] != '*') {
                res = i != s.length && (p[j] == s[i] || p[j] == '.') && h(s, i + 1, p, j + 1, dp);
            } else { // p[j+1]=='*' && j不是最后一个位置
                if (i == s.length)
                    res = h(s, i, p, j + 2, dp);
                else { // s字符串还没结束
                    if (s[i] != p[j] && p[j] != '.')
                        res = h(s, i, p, j + 2, dp);
                    else
                        // i与i+1位置是否相同，都可以用下面的公式。上面举例时是用的相等的情况。
                        // 假设 s = [a, b] 开头位置是12，p = [a, *] 开头位置是14
                        // 按照上面的优化公式，dp[12][14] == dp[13][14] || dp[12][16]
                        // 看下dp[13][14] 按理说应该是：dp[13][14] == dp[13][16]
                        // dp[12][14]依赖的是：dp[12][16] || dp[13][16]
                        // 所以，即便i与i+1不相等的情况下，依然可以用下面的一条语句
                        res = h(s, i + 1, p, j, dp) || h(s, i, p, j + 2, dp);
                }

            }
        }
        dp[i][j] = res ? 1 : -1;
        return res;
    }
    // =============================================================================================


    // 动态规划+斜率优化
    public static boolean isMatchV3(String str, String exp) {
        if (str == null || exp == null)
            return false;
        char[] s = str.toCharArray();
        char[] p = exp.toCharArray();
        if (!isValid(s, p))
            return false;
        int N = s.length;
        int M = p.length;
        boolean[][] dp = new boolean[N + 1][M + 1];
        dp[N][M] = true;

        for (int j = M - 1; j >= 0; j--) {
            for (int i = N; i >= 0; i--) {
                // 这个if想表达的就是p[j+1]不是'*'
                if (j + 1 == M || p[j + 1] != '*')
                    dp[i][j] = i != N && (p[j] == s[i] || p[j] == '.') && dp[i + 1][j + 1];
                else { // p[j+1]=='*' && j不是最后一个位置
                    if (i == N)
                        dp[i][j] = dp[i][j + 2];
                    else { // s字符串还没结束
                        if (s[i] != p[j] && p[j] != '.')
                            dp[i][j] = dp[i][j + 2];
                        else
                            dp[i][j] = dp[i + 1][j] || dp[i][j + 2];
                    }
                }
            }
        }
        return dp[0][0];
    }

}
