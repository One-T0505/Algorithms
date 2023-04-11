package Basic.DynamicProgramming;

// 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。

import java.util.ArrayList;
import java.util.List;

public class _0131_Code {



    public static List<List<String>> partition(String s) {
        ArrayList<List<String>> res = new ArrayList<>();
        ArrayList<String> parts = new ArrayList<>();
        if (s.length() == 1){
            parts.add(s);
            res.add(parts);
            return res;
        }
        char[] chs = s.toCharArray();
        int N = chs.length;
        // 预处理数组，dp[i][j]表示s[i..j]是否为回文串
        boolean[][] dp = new boolean[N][N];
        for (int i = 0; i < N - 1; i++) {
            dp[i][i] = true;
            dp[i][i + 1] = chs[i] == chs[i + 1];
        }
        dp[N - 1][N - 1] = true;
        for (int i = N - 3; i >= 0; i--) {
            for (int j = i + 2; j < N; j++) {
                dp[i][j] = chs[i] == chs[j] && dp[i + 1][j - 1];
            }
        }
        f(chs, 0, dp, parts, res);
        return res;
    }

    private static void f(char[] chs, int i, boolean[][] dp, ArrayList<String> parts,
                   ArrayList<List<String>> res) {
        if (i == chs.length){
            res.add(new ArrayList<>(parts));
        } else {
            for (int e = i; e < chs.length; e++) {
                if (dp[i][e]){
                    parts.add(String.valueOf(chs, i, e - i + 1));
                    f(chs, e + 1, dp, parts, res);
                    parts.remove(parts.size() - 1);
                }
            }
        }
    }


    public static void main(String[] args) {
        List<List<String>> res = partition("aab");
    }
}
