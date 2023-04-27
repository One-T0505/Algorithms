package Basic.BitOperation.Exercise;

// leetCode231
// 给你一个整数 n，请你判断该整数是否是 2 的幂次方。如果是，返回 true ；否则，返回 false 。
// 如果存在一个整数 x 使得 n == 2^x ，则认为 n 是 2 的幂次方。
public class IsPowerOfTwo {

    // n == 2^x  说明n>=1  负数的情况就不用考虑了
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

}
