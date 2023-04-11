package class11;

public class IsPalindrome {

    public static boolean isPalindrome(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                sb.append(c);
        }
        char[] std = sb.toString().toLowerCase().toCharArray();
        int N = std.length;
        boolean[][] pa = new boolean[N][N];
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i; j < N; j++) {
                if (i == j)
                    pa[i][j] = true;
                else if (i == j - 1)
                    pa[i][j] = std[i] == std[j];
                else {
                    pa[i][j] = std[i] == std[j] && pa[i + 1][j - 1];
                }
            }
        }
        return pa[0][N - 1];
    }
    // -------------------------------------------------------------------------------------------------


    // 上面的方法已经没问题了，但是超出题目的内存限制了，所以要继续优化空间。
    public static boolean isPalindrome2(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                sb.append(c);
        }
        char[] std = sb.toString().toLowerCase().toCharArray();
        int N = std.length;
        if (N == 0)
            return true;
        // 此时pa代表的是N-1行，所以此时pa[i]表示N-1～i是否为回文串  一定要画图看
        boolean[] pa = new boolean[N];
        pa[N - 1] = true;

        for (int i = N - 2; i >= 0; i--) {
            pa[i] = true;
            boolean pre = pa[i + 1];
            for (int j = i + 1; j < N; j++) {
                if (j == i + 1)
                    pa[j] = std[i] == std[j];
                else {
                    boolean tmp = pa[j];
                    pa[j] = std[i] == std[j] && pre;
                    pre = tmp;
                }
            }
        }
        return pa[N - 1];
    }
}
