package class11;

import java.util.ArrayList;
import java.util.List;

// 问题一：一个字符串至少要切几刀能让切出来的子串都是回文串
// 问题二：返回问题一的其中一种划分结果
// 问题三：返回问题一的所有划分结果.

public class SplitToPalindrome {

    // 问题一
    public static int minCutP1V1(String s) {
        if (s == null || s.length() < 2)
            return 0;
        if (s.length() == 2)
            return s.charAt(0) == s.charAt(1) ? 0 : 1;
        char[] chs = s.toCharArray();
        boolean[][] pa = isPalindrome(chs);
        // 因为f返回的是部分数，题目要求的是切的次数，所以N个部分只需要N-1刀，所以减1
        return f(chs, 0, pa) - 1;
    }


    // 定义一个递归函数f(str, i) ，其作用是从i出发及其后面所有的部分，能分成至少几个回文部分
    private static int f(char[] chs, int i, boolean[][] pa) {
        if (i == chs.length)
            return 0;
        int res = Integer.MAX_VALUE;
        for (int end = i; end < chs.length; end++) {
            if (pa[i][end]) // chs[i..end] 是回文串
                res = Math.min(res, f(chs, end + 1, pa) + 1);
        }
        return res;
    }
    // 先分析下该递归函数的时间复杂度，可以发现可变参数只有一个i，对于每个i，都需要遍历到结尾。所以时间复杂度至少是O(N^2).
    // if中的条件表达式用于判断chs[i..end]是不是回文串，如果这个的复杂度是O(N)，那么总的时间复杂度就是O(N^3).
    // 问题一其实包含了两个动态规划。我们可以提前对chs这个字符数组来个动态规划，做一张二维dp表，行表示L，列表示R，
    // dp[i][j]就表示chs[i..j]是不是回文串，这样在f中判断是否为回文时就能达到O(1).


    private static boolean[][] isPalindrome(char[] chs) { // 在该方法中就不对chs的有效性检查了，在上游做检查
        int N = chs.length;
        boolean[][] dp = new boolean[N][N];  // 左下区域都没用
        // 填主对角线
        for (int i = 0; i < N; i++)
            dp[i][i] = true;
        for (int i = 0; i < N - 1; i++)
            dp[i][i + 1] = chs[i] == chs[i + 1];

        // 填剩余元素. 转移方程很简单：i..j是否为回文：只需要：chs[i]==chs[j] && dp[i+1][j-1]是回文即可
        // 看这个依赖关系，所以填写顺序也是逐一按照对角线顺序来写的。
        for (int L = N - 3; L >= 0; L--) {
            for (int R = L + 2; R < N; R++)
                dp[L][R] = (chs[L] == chs[R] && dp[L + 1][R - 1]);
        }
        return dp;
    }
    // --------------------------------------------------------------------------------------------------


    // 问题一的递归版f只有一个可变参数，于是也可以很容易改成一维动态规划.dp[i]的含义就是：从i～N-1至少能切出几个
    // 回文部分。
    public static int minCutP1V2(String s) {
        if (s == null || s.length() < 2)
            return 0;
        if (s.length() == 2)
            return s.charAt(0) == s.charAt(1) ? 0 : 1;
        int N = s.length();
        char[] chs = s.toCharArray();
        boolean[][] pa = isPalindrome(chs);
        int[] dp = new int[N + 1];
        dp[N] = 0;
        for (int i = N - 1; i >= 0; i--) {
            if (pa[i][N - 1])
                dp[i] = 1;
            else {
                int next = Integer.MAX_VALUE;
                for (int j = i; j < N; j++) {
                    if (pa[i][j])
                        next = Math.min(next, dp[j + 1] + 1);
                }
                dp[i] = next;
            }
        }
        return dp[0] - 1;
    }
    // ====================================================================================================


    // 问题二
    // 思路和 AddToPalindrome 根据动态规划表来回溯结果时的思路一样。同样地，像上面的方法一样，构造出一个一维缓存表。
    // dp[i]表示从i到最后这部分字符串切分成全是回文串至少能分成几个部分。举个例子，str="abacfckx".
    //
    //      a  b  a  c  f  c  k  x
    // dp   4  5  4  3  4  3  2  1  0
    //      i        j
    // 利用预处理结构提前对一个字符串的任意子串都提前做好缓存，这样就能直接读取该部分是否为回文串。如果i～j-1是回文
    // 并且dp[i] == dp[j] + 1，那么i～j-1就是切出的一个回文串

    public static List<String> minCutV2(String s) {
        ArrayList<String> res = new ArrayList<>();
        if (s == null || s.length() < 2)
            res.add(s);
        else {
            int N = s.length();
            char[] chs = s.toCharArray();
            boolean[][] pa = isPalindrome(chs);
            int[] dp = new int[N + 1];
            dp[N] = 0;
            for (int i = N - 1; i >= 0; i--) {
                if (pa[i][N - 1])
                    dp[i] = 1;
                else {
                    int next = Integer.MAX_VALUE;
                    for (int j = i; j < N; j++) {
                        if (pa[i][j])
                            next = Math.min(next, dp[j + 1] + 1);
                    }
                    dp[i] = next;
                }
            }

            for (int i = 0, j = 1; j <= N; j++) {
                if (pa[i][j - 1] && dp[i] == dp[j] + 1) {
                    res.add(s.substring(i, j));
                    i = j;
                }
            }
        }
        return res;
    }
    // ==================================================================================================


    // 问题三
    // 问题三也是在问题二的基础上，在回溯可能性的时候递归所有的可能性即可。
    public static List<List<String>> minCutV3(String s) {
        List<List<String>> res = new ArrayList<>();
        if (s == null || s.length() < 2) {
            List<String> cur = new ArrayList<>();
            cur.add(s);
            res.add(cur);
        } else {
            char[] chs = s.toCharArray();
            int N = chs.length;
            boolean[][] pa = isPalindrome(chs);
            int[] dp = new int[N + 1];
            dp[N] = 0;
            for (int i = N - 1; i >= 0; i--) {
                if (pa[i][N - 1])
                    dp[i] = 1;
                else {
                    int next = Integer.MAX_VALUE;
                    for (int j = i; j < N; j++) {
                        if (pa[i][j])
                            next = Math.min(next, dp[j + 1] + 1);
                    }
                    dp[i] = next;
                }
            }
            h(s, 0, 1, pa, dp, new ArrayList<>(), res);
        }

        return res;
    }


    // dp就是一维缓存表，dp[i]表示将字符串s从i到最后最少能分成几个回文部分
    // s[0..i-1]已经按回文部分切分好了，并逐一添加到了path中，所以不用再管0～i-1。
    // 现在要考察的是s[i..j-1]这部分
    private static void h(String s, int i, int j, boolean[][] pa, int[] dp, List<String> path,
                          List<List<String>> res) {
        if (j == s.length()) {
            if (pa[i][j - 1] && dp[i] == dp[j - 1] + 1) {
                path.add(s.substring(i, j));
                // 将path中切好的回文部分，复制到一个新的数组中，并添加至最终的结果res里
                // 为什么不能直接res.add(path)； 如果直接传入path这个内存地址，path后续的内容还会变化的
                res.add(new ArrayList<>(path));
                path.remove(path.size() - 1);  // 还原现场
            }
        } else {
            if (pa[i][j - 1] && dp[i] == dp[j - 1] + 1) {
                path.add(s.substring(i, j));
                h(s, j, j + 1, pa, dp, path, res);
                path.remove(path.size() - 1);
            }
            h(s, i, j + 1, pa, dp, path, res);
        }
    }
    // =================================================================================================


}
