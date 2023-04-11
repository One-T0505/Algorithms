package LeetCode;

/**
 * ymy
 * 2023/4/3 - 21 : 38
 **/

// leetCode306 累加数
// 累加数 是一个字符串，组成它的数字可以形成累加序列。
// 一个有效的 累加序列 必须 至少 包含 3 个数。除了最开始的两个数以外，序列中的每个后续数字必须是它之前两个数字之和。
// 给你一个只包含数字 '0'-'9' 的字符串，编写一个算法来判断给定输入是否是 累加数 。如果是，返回 true ；否则，
// 返回 false 。
// 说明：累加序列里的数，除数字 0 之外，不会 以 0 开头，所以不会出现 1, 2, 03 或者 1, 02, 3 的情况。

// 1 <= num.length <= 35
// num 仅由数字（0 - 9）组成

public class AdditiveNum {

    // 建议去看下给的例子
    /**
    public static boolean isAdditiveNumber(String num) {
        if (num == null || num.length() < 3)
            return false;
        char[] chs = num.toCharArray();
        int N = chs.length;
        // 枚举将整个字符串分成几部分
        for (int part = 3; part <= N; part++) {
            if (dfs(chs, 0, part))
                return true;
        }
        return false;
    }


    private static boolean dfs(char[] chs, int i, int part) {
        int N = chs.length;
        if (N - i < part)
            return false;
        if (i == N)
            return part == 0;
        if (part == 0)
            return false;
        if (chs[i] == '0')
            return false;
        int first = 0;
        // 枚举第一部分结束的位置
        for (int e = i; e < N - part - 1; i++) {
            first += chs[i] - '0';
            if (dfs(chs, e + 1, part - 1))
        }
    }
    **/
}
