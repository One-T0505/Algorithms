package LeetCode;

/**
 * ymy
 * 2023/4/3 - 21 : 07
 **/


// leetCode263
// 丑数 就是只包含质因数 2、3 和 5 的正整数。
// 给你一个整数 n ，请你判断 n 是否为 丑数 。如果是，返回 true ；否则，返回 false 。

public class UglyNum {

    public static boolean isUgly(int n) {
        if (n <= 0)
            return false;
        if (n == 1)
            return true;
        int[] factors = {2, 3, 5};
        for (int factor : factors){
            while (n % factor == 0)
                n /= factor;
        }
        return n == 1;
    }
}
