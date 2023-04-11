package GreatOffer.TopInterviewQ;

/**
 * ymy
 * 2023/3/15 - 13 : 31
 **/

// 给你两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和取余运算。
// 整数除法应该向零截断，也就是截去（truncate）其小数部分。例如，8.345 将被截断为 8 ，-2.7335 将被截断至 -2 。
// 返回被除数 dividend 除以除数 divisor 得到的商 。
// 注意：假设我们的环境只能存储 32 位有符号整数，其数值范围是 [−2^31,  2^31 − 1] 。本题中，如果商严格
// 大于 2^31 − 1 ，则返回 2^31 − 1 ；如果商严格小于 -2^31 ，则返回 -2^31 。

// -2^31 <= dividend, divisor <= 2^31 - 1
// divisor != 0

public class _0029_Code {

    public static int divide(int dividend, int divisor) {
        if (dividend == 0)
            return 0;
        // 执行到这里说明两者都不为0
        // 相等直接返回1
        if (divisor == dividend)
            return 1;
        // 先过滤分子的特殊情况
        if (dividend == Integer.MIN_VALUE){
            if (divisor == 1)
                return Integer.MIN_VALUE;
            if (divisor == -1)
                return Integer.MAX_VALUE;
        }
        // 再过滤分母的特殊情况
        if (divisor == Integer.MIN_VALUE)
            return 0;  // 因为上面已经过滤了相等的情况，所以这里分子必然不是最小值

        boolean isNeg = (dividend > 0) ^ (divisor > 0);
        // 全部变成负数，因为负数可表示的范围大
        dividend *= dividend < 0 ? 1 : -1;
        divisor *= divisor < 0 ? 1 : -1;

        // 我们记被除数为 X，除数为 Y，并且 X 和 Y 都是负数。我们需要找出 X/Y 的结果 Z。Z 一定是正数或 0。
        // 根据除法以及余数的定义，我们可以将其改成乘法的等价形式，即：
        // Z * Y ≥ X > (Z+1) * Y
        // 因此，我们可以使用二分查找的方法得到 Z，即找出最大的 Z 使得 Z * Y ≥ X 成立
        int L = 1, R = Integer.MAX_VALUE;
        int res = 0;
        while (L <= R){
            // 注意溢出，并且不能使用除法
            int mid = L + ((R - L) >> 1);
            boolean check = quickAdd(divisor, mid, dividend);
            if (check){
                res = mid;
                if (mid == Integer.MAX_VALUE)
                    break;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        return res * (isNeg ? -1 : 1);
    }



    // 快速乘。divisor 和 dividend 都是负数  rate 是正数
    // 我们要判断 divisor * rate >= dividend 是否成立
    // 记住，不能用乘除法。
    // 该方法之前在矩阵快速幂和数的快速幂讲过的。
    private static boolean quickAdd(int divisor, int rate, int dividend) {
        int result = 0, add = divisor; // add就是最基本的加法单位
        // 我们按照rate的二进制形式来完成加法。
        while (rate != 0){
            if ((rate & 1) != 0){ // 说明当前位有效
                // 需要保证 result + add >= dividend  下面的写法是为了防止溢出
                if (result < dividend - add) // result + add 两个负数相加有可能溢出
                    return false;
                result += add;
            }
            if (rate != 1){  // 每一轮 add 都要翻倍，但是不能超过范围
                // 需要保证 add + add >= dividend
                if (add < dividend - add)
                    return false;
                add += add;
            }
            // 不能使用除法
            rate >>= 1;
        }
        return true;
    }


    public static void main(String[] args) {
        int dividend = -2147483648;
        int divisor = 3;
        System.out.println(divide(dividend, divisor));
    }
}
