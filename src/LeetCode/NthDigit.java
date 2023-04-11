package LeetCode;

/**
 * ymy
 * 2023/4/3 - 11 : 09
 **/

// leetCode400
// 给你一个整数 n ，请你在无限的整数序列 [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...] 中找出并返回第 n 位上的数字。

public class NthDigit {

    // 看不懂题意的就去看官方给的例子.
    public static int findNthDigit(int n) {
        if (n < 1)
            return Integer.MAX_VALUE;
        return 0;
    }
}
