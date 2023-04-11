package Basic.Parentheses;

// 面试题08.14布尔运算
// 给定一个字符串exp，其长度必然是奇数。偶数索引上不是0就是1；奇数索引上只会是 | & ^ 这三种逻辑运算符号。为什么字符串长度
// 一定是奇数？因为题目为了给你减少干扰，奇数个长度就说明头尾都是数字，说明给的字符串一定是有效的。可以在这个exp字符串的表达
// 式上任意加上合理的括号变成不同的表达式，请问最后有多少种使结果为true的添加括号方法数。

// 思路：不用实际添加括号。括号的作用只是为了更改计算的优先级，所以我们可以在不用添加括号的情况下达到添加括号的效果。

public class BoolExp {

    // 主方法：desired==1表示想要结果为true的方法数；desired==0表示想要结果为false的方法数
    public static int countEval(String s, int result) {
        if (s == null || (s.length() & 1) == 0)  // 如果exp长度为偶数，无效
            return 0;
        Info all = f(s.toCharArray(), 0, s.length() - 1);
        return result == 1 ? all.t : all.f;
    }

    // 返回Info信息
    // Info.t 表示exp[L..R]能让结果为true的添加括号的方法数
    // Info.f 表示exp[L..R]能让结果为false的添加括号的方法数
    // 核心思想是：以每个运算符号作为整个表达式最后去结合的部分，也就是说先算运算符左右两边的表达式，再将两部分结合在一起。
    private static Info f(char[] exp, int L, int R) {
        int t = 0;
        int f = 0;
        if (L == R) {
            t = exp[L] == '1' ? 1 : 0;
            f = exp[L] == '0' ? 1 : 0;
        } else {  // 那就说明L～R至少有3个字符
            for (int split = L + 1; split < R; split += 2) {  // L+1必是逻辑符号，每隔两个就是
                // 每次都以一个运算符号为界限，取收集前后两部分表达式的结果
                Info former = f(exp, L, split - 1);
                Info latter = f(exp, split + 1, R);
                int preT = former.t;
                int preF = former.f;
                int nextT = latter.t;
                int nextF = latter.f;
                switch (exp[split]) {
                    case '&' -> {    // 如果当前运算符号是&
                        t += preT * nextT;   // 那就只能是前后都为true才行
                        f += preT * nextF + preF * nextT + preF * nextF;
                    }
                    case '|' -> {    // 如果当前运算符号是|
                        t += preT * nextF + preF * nextT + preT * nextT;
                        f += preF * nextF;
                    }
                    case '^' -> {    // 如果当前运算符号是^
                        t += preT * nextF + preF * nextT;
                        f += preT * nextT + preF * nextF;
                    }
                }

            }
        }
        return new Info(t, f);
    }

    // 记忆化搜索
    public static int countEval2(String s, int result) {
        if (s == null || (s.length() & 1) == 0)  // 如果exp长度为偶数，无效
            return 0;
        int N = s.length();
        Info[][] dp = new Info[N][N];
        Info all = g(s.toCharArray(), 0, s.length() - 1, dp);
        return result == 1 ? all.t : all.f;
    }
    // ===============================================================================================

    // 可变参数只有L、R，并且范围是从0～exp.length-1
    private static Info g(char[] exp, int L, int R, Info[][] dp) {
        if (dp[L][R] != null)
            return dp[L][R];
        int t = 0;
        int f = 0;
        if (L == R) {
            t = exp[L] == '1' ? 1 : 0;
            f = exp[L] == '0' ? 1 : 0;
        } else {  // 那就说明L～R至少有3个字符
            for (int split = L + 1; split < R; split += 2) {  // L+1必是逻辑符号，每隔两个就是
                // 每次都以一个运算符号为界限，取收集前后两部分表达式的结果
                Info former = f(exp, L, split - 1);
                Info latter = f(exp, split + 1, R);
                int preT = former.t;
                int preF = former.f;
                int nextT = latter.t;
                int nextF = latter.f;
                switch (exp[split]) {
                    case '&' -> {    // 如果当前运算符号是&
                        t += preT * nextT;   // 那就只能是前后都为true才行
                        f += preT * nextF + preF * nextT + preF * nextF;
                    }
                    case '|' -> {    // 如果当前运算符号是|
                        t += preT * nextF + preF * nextT + preT * nextT;
                        f += preF * nextF;
                    }
                    case '^' -> {    // 如果当前运算符号是^
                        t += preT * nextF + preF * nextT;
                        f += preT * nextT + preF * nextF;
                    }
                }

            }
        }
        dp[L][R] = new Info(t, f);
        return dp[L][R];
    }

    // 动态规划
    public static int countEval3(String s, int result) {
        if (s == null || (s.length() & 1) == 0)  // 如果exp长度为偶数，无效
            return 0;
        char[] chs = s.toCharArray();
        int N = chs.length;
        // 第一个维度2分别对应true，false两张N*N的缓存表
        // 注意：N*N的表中，有些是不能用的，因为L、R都必须是数字位才行，不能以符号位作为开头或结尾
        // 行表示L，列表示R，所以对角线的左下区域不能用。
        int[][][] dp = new int[2][N][N];
        dp[1][0][0] = chs[0] == '1' ? 1 : 0;
        dp[0][0][0] = dp[1][0][0] ^ 1;
        for (int R = 2; R < N; R++) {
            dp[1][R][R] = chs[R] == '1' ? 1 : 0;
            dp[0][R][R] = dp[1][R][R] ^ 1;
            for (int L = R - 2; L >= 0; L -= 2) {
                for (int k = L; k < R; k += 2) {  // 寻找L～R中的每一个运算符号，每次k+1处就是运算符号
                    int preT = dp[1][L][k];
                    int preF = dp[0][L][k];
                    int nextT = dp[1][k + 2][R];
                    int nextF = dp[0][k + 2][R];
                    switch (chs[k + 1]) {
                        case '&' -> {    // 如果当前运算符号是&
                            dp[1][L][R] += preT * nextT;   // 那就只能是前后都为true才行
                            dp[0][L][R] += preT * nextF + preF * nextT + preF * nextF;
                        }
                        case '|' -> {    // 如果当前运算符号是|
                            dp[1][L][R] += preT * nextF + preF * nextT + preT * nextT;
                            dp[0][L][R] += preF * nextF;
                        }
                        case '^' -> {    // 如果当前运算符号是^
                            dp[1][L][R] += preT * nextF + preF * nextT;
                            dp[0][L][R] += preT * nextT + preF * nextF;
                        }
                    }
                }
            }
        }
        return dp[result][0][N - 1];
    }
    // ===============================================================================================

    public static class Info {
        public int t;   // 使结果为true的方法数
        public int f;   // 使结果为false的方法数

        public Info(int t, int f) {
            this.t = t;
            this.f = f;
        }
    }
}
