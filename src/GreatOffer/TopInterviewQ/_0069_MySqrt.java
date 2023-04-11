package GreatOffer.TopInterviewQ;


// 给你一个非负整数 x，计算并返回 x 的算术平方根。
// 由于返回类型是整数，结果只保留整数部分，小数部分将被舍去。
// 注意：不允许使用任何内置指数函数和算符，例如 pow(x, 0.5) 或者 x ** 0.5 。

public class _0069_MySqrt {

    // x的开平方的结果一定在1..x这个范围内，我们只需要在这个范围内不断二分即可

    public static int mySqrt(int x) {
        if (x == 0)
            return 0;
        if (x <= 3)
            return 1;
        long L = 1;
        long R = x;
        long res = 1;
        while (L <= R) {
            long mid = L + ((R - L) >> 1);
            if (mid * mid <= x) {
                res = mid;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        return (int) res;
    }
}
