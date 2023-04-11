package class11;

import java.util.ArrayList;
import java.util.List;

// 问题一：一个字符串至少需要添加多少个字符能整体变成回文串
// 问题二：返回问题一的其中一种添加结果
// 问题三：返回问题一的所有添加结果

public class AddToPalindrome {

    // leetCode1312
    // 问题一：该问题是典型的样本对应模型。构造一张二维缓存表，行表示L，列表示R。对角线左下区域无效。
    //        cache[i][j]表示字符串arr[i~j]这部分子串如果要成为回文串至少需要添加几个字符。主对角线上的格子全为0，
    //        因为对角线上的格子都仅表示一个字符，本身就是回文的，所以不用添加。主对角线上面的一条对角线也好填，次对角线
    //        表示j==i+1，所以仅表示相邻的两个字符，如果两个相等就填0，如果不等就填1，比如ab，那可以在左边加一个b，也可以
    //        在右边加一个a，不管怎样都是一个。对于剩下的一般位置[i,j]，有以下几种情况：
    //         1> [i,j-1]表示从i～j-1搞成回文串至少需要添加的字符数，所以只需要再把j处字符再对称侧加一个即可
    //         2> [i+1,j]表示从i+1～j搞成回文串至少需要添加的字符数，所以只需要再把i处字符再对称侧加一个即可
    //         3> 如果i和j处的字符相等，那么c[i][j] == c[i+1][j-1]

    public static int dpV1(String s) {
        if (s == null || s.length() < 2)
            return 0;
        if (s.length() == 2)
            return s.charAt(0) == s.charAt(1) ? 0 : 1;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int[][] dp = new int[N][N];
        // 主对角线全部设为0，但是默认初始化就是0，所以不用管了
        // 填写次对角线
        for (int i = 1; i < N; i++)
            dp[i - 1][i] = chs[i - 1] == chs[i] ? 0 : 1;
        // 填写剩下一般位置.因为最后返回的是最右上角的，所以填写顺序是：按列从左至右，从下至上
        for (int R = 2; R < N; R++) {
            for (int L = R - 2; L >= 0; L--) {
                dp[L][R] = Math.min(dp[L + 1][R], dp[L][R - 1]) + 1;
                if (chs[L] == chs[R])
                    dp[L][R] = Math.min(dp[L][R], dp[L + 1][R - 1]);
            }
        }
        return dp[0][N - 1];
    }
    // ====================================================================================================


    // 问题二
    // 该问题需要首先像问题一一样填写好缓存表之后，然后根据结果倒退填写新字符串。假设s长度为N，c[0][N - 1]==2，
    // 说明要在原来的基础上至少添加2个字符，所以新字符res长度为N+2。c[0][N - 1]的值可能是来自左边、下边，或者左下，但是
    // 问题二只说找出一种可能的解法，所以我们不需要管他是具体来自哪个依赖的。假如c[0][N-2]==1, c[1][N-1]==1，我们知道
    // 在问题一中我们正向推理c[0][N-1]时是c[0][N - 1] = Math.min(c[0][N-2], c[1][N-1]) + 1. 所以我们不得而知
    // c[0][N - 1]具体是依赖谁的。假设是来自c[0][N-2]==1，那么也就是说在0～N-2位置上至少添加1个字符具体怎么添加c[0][N-1]
    // 不用管，只用知道c[0][N-2]添加1个之后，c[0][N-1]是将自己的N-1位置的字符复制一份添加在最左侧的；所以
    // res[0] == res[N+1] == s[N-1]，剩下1～N就接着填。

    public static String dpV2(String s) {
        if (s == null || s.length() < 2)
            return s;
        char[] chs = s.toCharArray();
        int N = chs.length;
        int[][] dp = new int[N][N];
        // 主对角线全部设为0，但是默认初始化就是0，所以不用管了
        // 填写次对角线
        for (int i = 1; i < N; i++)
            dp[i - 1][i] = chs[i - 1] == chs[i] ? 0 : 1;
        // 填写剩下一般位置.因为最后返回的是最右上角的，所以填写顺序是：按列从左至右，从下至上
        for (int R = 2; R < N; R++) {
            for (int L = R - 2; L >= 0; L--) {
                dp[L][R] = Math.min(dp[L + 1][R], dp[L][R - 1]) + 1;
                if (chs[L] == chs[R])
                    dp[L][R] = Math.min(dp[L][R], dp[L + 1][R - 1]);
            }
        }
        // 上面的过程就是填写缓存表
        // =============================================================================================

        char[] res = new char[N + dp[0][N - 1]];
        int resL = 0, resR = res.length - 1;   // res字符串中的左右指针，记录要在哪里填写答案
        int L = 0, R = N - 1;     // 在cache中回溯时用到的左右指针
        while (L < R) {
            if (dp[L][R] == dp[L][R - 1] + 1) {
                res[resL++] = chs[R];
                res[resR--] = chs[R];
                R -= 1;
            } else if (dp[L][R] == dp[L + 1][R] + 1) {
                res[resL++] = chs[R];
                res[resR--] = chs[L];
                L += 1;
            } else {  // 依赖于左下侧，说明s[L] == s[R]，此时直接填写
                res[resL++] = chs[L++];
                res[resR--] = chs[R++];
            }
        }
        // 为什么要单独考虑下，L==R？  设想以下两种情况：
        // 1> 此时的L、R定义的范围字符串是：aba，然后L==R，所以直接将左右两侧的a分别拷贝至resL、resR处，然后L++，R--，
        //    此时L==R
        // 2> 此时的L、R定义的范围字符串是：aa，然后L==R，所以直接将左右两侧的a分别拷贝至resL、resR处，然后L++，R--，
        //    此时L > R
        // 为了统一循环的过程，我们取了两种情况的交集，只考虑L<R的情况，然后出循环时单独考虑L==R的情况
        if (L == R)
            res[resL] = chs[L];
        return String.valueOf(res);
    }
    // =====================================================================================================


    // 问题三
    // 有了题目二的基础，题目三处理起来就相对容易一点了。题目二中依赖关系只需要找到一种即可，所以只用if、else判断即可。
    // 但是题目三就要求将每一种可能的依赖关系都像题目二一样，找到一种可能的解。所以我们只需要将每种可能性都做题目二
    // 的推理即可。
    public static List<String> dpV3(String s) {
        List<String> res = new ArrayList<>();
        if (s == null || s.length() < 2)
            res.add(s);
        else {
            char[] chs = s.toCharArray();
            int N = chs.length;
            int[][] dp = new int[N][N];
            for (int i = 1; i < N; i++)
                dp[i - 1][i] = chs[i - 1] == chs[i] ? 0 : 1;
            for (int R = 2; R < N; R++) {
                for (int L = R - 2; L >= 0; L--) {
                    dp[L][R] = Math.min(dp[L + 1][R], dp[L][R - 1]) + 1;
                    if (chs[L] == chs[R])
                        dp[L][R] = Math.min(dp[L][R], dp[L + 1][R - 1]);
                }
            }
            int len = N + dp[0][N - 1];
            char[] path = new char[len];
            g(chs, dp, 0, N - 1, path, 0, len - 1, res);
        }
        return res;
    }


    // chs 原字符串数组   dp  之前填好的缓存表   L R  用于指示cache中的左右指针
    // pL pL 和题目二中的resL、resR作用一样，path就是待填的空字符串
    // 当前来到了cache[L][R]的格子，现在要填的位置是pL、pR
    private static void g(char[] chs, int[][] dp, int L, int R,
                          char[] path, int pL, int pR, List<String> res) {
        if (L >= R) {  // 这个base case 就是题目二中跳出循环还要单独判断L==R的情况
            if (L == R)
                path[pL] = chs[L];
            res.add(String.valueOf(path));
        } else {
            if (dp[L][R] == dp[L][R - 1] + 1) {  // 如果可以依赖左侧
                path[pL] = chs[R];
                path[pR] = chs[R];
                g(chs, dp, L, R - 1, path, pL + 1, pR - 1, res);
            }
            if (dp[L][R] == dp[L + 1][R] + 1) {
                path[pL] = chs[L];
                path[pR] = chs[L];
                g(chs, dp, L + 1, R, path, pL + 1, pR - 1, res);
            }
            // 1.首先 (L == R - 1) || dp[L + 1][R - 1] == dp[L][R]  想表达的是同一件事
            //  只是因为如果 L == R - 1，那么 dp[L + 1][R - 1] 就会错位 导致 L + 1 > R - 1，就会到
            //  缓存表的左下区域，这部分区域是无效的，我们一开始的缓存表就没设置过值。所以把 (L == R - 1) 单独拿出来
            //  因为写不成 dp[L + 1][R - 1] == dp[L][R] 这样的形式
            // 2.其次，为什么要两个条件成立才能说明当前格子依赖左下格子？ chs[L] == chs[R] && A（A就是后面一坨的表达式）
            //   当然了，光有 chs[L] == chs[R] 是不足以说明 dp[L][R]的值就是从cache[L+1][R-1]得来的。
            //   比如c[4][6] = 3，c[4][5]=2  c[5][6]=2  c[5][5]=4，所以根本不会依赖于左下角格子。
            if (chs[L] == chs[R] && ((L == R - 1) || dp[L + 1][R - 1] == dp[L][R])) {
                path[pL] = chs[L];
                path[pR] = chs[R];
                g(chs, dp, L + 1, R - 1, path, pL + 1, pR - 1, res);
            }
        }
    }
    // 该方法中在找寻每一种可能性的时候，是串联了三个if，所以每一种可能性都能找到。
    // 而题目二只需要找一种可能性，所以是用的if，else-if。
    // 这个递归是深度遍历，为什么分别三个if里面修改了path的值，为什么不用还原现场？ 因为这道题比较特殊，即便我没有恢复现场，
    // 走到下一个if的递归时，他们会在同样的位置上直接覆盖上新的值，上一个递归修改的值根本不会影响现在的决定。
    // ====================================================================================================


}
