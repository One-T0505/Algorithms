package LeetCode;

/**
 * ymy
 * 2023/3/25 - 10 : 09
 **/

// leetCode214
// 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。找到并返回可以用这种方式转换的最短回文串。


public class ShortestPalindrome {


    // 用manacher算法
    public static String shortestPalindrome(String s) {
        if (s == null || s.length() < 2)
            return s;
        char[] p = expand(s);
        int N = p.length;
        int[] radius = new int[N];
        int C = -1, R = -1;
        int max = 0;
        for (int i = 0; i < N; i++) {
            radius[i] = i > R ? 1 : Math.min(R - i + 1, radius[2 * C - i]);

            while (i - radius[i] > -1 && i + radius[i] < N){
                if (p[i - radius[i]] != p[i + radius[i]])
                    break;
                radius[i]++;
            }

            if (i + radius[i] - 1 > R){
                R = i + radius[i] - 1;
                C = i;
            }

            if (i - radius[i] == -1)
                max = Math.max(max, radius[i]);
        }
        String sb = new StringBuilder(s.substring(max - 1)).reverse().toString();
        return sb + s;
    }

    private static char[] expand(String s) {
        char[] chs = s.toCharArray();
        int N = chs.length;
        char[] res = new char[N << 1 | 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chs[i >> 1];
        }
        return res;
    }


    public static void main(String[] args) {
        System.out.println(shortestPalindrome("abb"));
    }
}
