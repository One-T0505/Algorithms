package LeetCode;

/**
 * ymy
 * 2023/3/27 - 09 : 56
 **/

// leetCode392
// 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
// 进阶：
//  如果有大量输入的 S，称作 S1, S2, ... , Sk 其中 k >= 10亿，你需要依次检查它们是否为 T 的子序列。
//  在这种情况下，你会怎样改变代码？

public class IsSubSeq {

    // 基础问题  进阶问题加上记忆化搜索即可
    public static boolean isSubsequence(String s, String t) {
        if (s == null || t == null || s.length() > t.length())
            return false;
        if (s.length() == t.length())
            return s.equals(t);
        // 执行到这里可以确定 s.len < t.len
        if (s.equals(""))
            return true;
        // 执行到这里可以确定 0 < s.len < t.len
        char[] a = s.toCharArray();
        char[] b = t.toCharArray();
        int[][] dp = new int[a.length][b.length];
        return isSubSeq(a, 0, b, 0, dp);
    }

    private static boolean isSubSeq(char[] a, int i, char[] b, int j, int[][] dp) {
        if (i == a.length)
            return true;
        if (j == b.length)
            return false;
        if (dp[i][j] != 0)
            return dp[i][j] == 1;
        boolean res = isSubSeq(a, i, b, j + 1, dp);
        if (!res && a[i] == b[j])
            res |= isSubSeq(a, i + 1, b, j + 1, dp);
        dp[i][j] = res ? 1 : -1;
        return res;
    }


    public static void main(String[] args) {
        String s = "abc";
        String t = "ahbgd";
        System.out.println(isSubsequence(s, t));
    }
}
