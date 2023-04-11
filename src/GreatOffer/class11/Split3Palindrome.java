package class11;

// leetCode1745
// 给你一个字符串 s ，如果可以将它分割成三个 非空 回文子字符串，那么返回 true ，否则返回 false 。

public class Split3Palindrome {

    public static boolean checkPartitioning(String s) {
        char[] chs = s.toCharArray();
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

        for (int i = 0; i < N - 2; i++) {
            for (int j = i + 1; j < N - 1; j++) {
                if (dp[0][i] && dp[i + 1][j] && dp[j + 1][N - 1])
                    return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        String s = "abcbdd";
        System.out.println(checkPartitioning(s));
    }
}
