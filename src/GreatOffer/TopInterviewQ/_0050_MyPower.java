package GreatOffer.TopInterviewQ;


// 实现 pow(x, n) ，即计算 x 的整数 n 次幂函数.

public class _0050_MyPower {


    // 计算一个数的n次方问题，有个很快的解决方法，希望能回忆起来。

    public static double myPow(double x, int n) {
        if (n == 0 || x == 1)
            return 1D;
        // pow表示目标次方的绝对值，如果目标次方是系统最小值，那就先让它+1，因为系统最小值没办法转成绝对值
        // 总体思路就是如果n<0，就先把它当成正数对待，到最后得到的结果res，根据n的正负，决定返回 1/res 还是 res
        // 如果n==-2147483648，那就先算2147483647次方，最后再单乘一次x

        int pow = Math.abs(n == Integer.MIN_VALUE ? n + 1 : n);
        double res = 1D;
        double t = x;
        while (pow != 0) {
            if ((pow & 1) != 0)
                res *= t;
            t *= t;
            pow >>= 1;
        }
        res = n == Integer.MIN_VALUE ? res * x : res;
        return n < 0 ? 1D / res : res;
    }


    public static void main(String[] args) {
        System.out.println(Integer.MIN_VALUE);
    }
}
