package GreatOffer.TopInterviewQ;


// 给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
// 如果反转后整数超过 32 位的有符号整数的范围 [−2^31,  2^31 − 1] ，就返回 0。
// 假设环境不允许存储 64 位整数（有符号或无符号）。

public class _0007_ReverseInteger {


    // 版本1
    public static int try1(int x) {
        int cur = 1;  // 每次取出一个数位上的数
        int radix = 10;
        int res = 0;
        while (x != 0) {
            cur = x % radix;
            res = res * radix + cur;
            x /= radix;
        }
        return res;
    }


    // 上面的方法已经是正确的思路了，但是忽略了边界问题
    // 唯一一个边界问题：给一个1235801004，一开始是合理范围的，但是逆序后就超出了int能表示的范围了。题目要求
    // 只能用32位的数据长度来判断是否越界。

    // 版本2
    public static int try2(int x) {
        int cur = 1;  // 每次取出一个数位上的数
        int radix = 10;
        int res = 0;
        // 这两个常量是固定值，用于提前一位做判断. MAX==2147483647
        // 所以，M==214748364   T==7
        final int M = Integer.MAX_VALUE / 10;
        final int T = Integer.MAX_VALUE % 10;
        while (x != 0) {
            // 情况1：如果此时res > M了，那么在下面的res*10必然越界
            // 情况2：如果res==M，但是要单独加的个位数>T，那么最终结果也是越界
            // 两种情况中1都直接返回0， res<M的时候，必然不会越界
            // 所以M、O就是在最后一步的时候检查是否将要越界，而不能等到最后了再去判断是否越界，因为越界后真实数值
            // 已经发生变化了
            if (res > M || (res == M && x % radix > T))
                return 0;
            cur = x % radix;
            res = res * radix + cur;
            x /= radix;
        }
        return res;
    }


    // 版本3，最终版本
    // 上面的检查边界已经几乎完成了，但是还剩一点小遗漏，就是int型数据负数的下限比正数多1，
    // -2147483648 <= int <= 2147483647
    // 上面的方法是用正数边界来考虑的，如果给的x==-2147483648，按理说应该返回0，因为逆序后越界了，但是上面的
    // 方法会返回一个正数值；
    // 这里用到的常用技巧是：先将x转为负数，再最后按照正负返回实际值，因为负数的范围更大，能表示下所有正数
    public static int reverse(int x) {
        boolean neg = (x >> 31 & 1) == 1;
        x = neg ? x : -x;
        int cur = 1;  // 每次取出一个数位上的数
        int radix = 10;
        int res = 0;
        // 这两个常量是固定值，用于提前一位做判断. MAX==2147483647
        // 所以，M==214748364   T==7
        final int M = Integer.MIN_VALUE / 10;
        final int T = Integer.MIN_VALUE % 10;
        while (x != 0) {
            // 此时变成负数了，所以关系就和方法2中的相反
            if (res < M || (res == M && x % radix < T))
                return 0;
            cur = x % radix;
            res = res * radix + cur;
            x /= radix;
        }
        return neg ? res : Math.abs(res);
    }
    // 这道题的常用技巧是：如果需要把一个数逆序时，往往先把其转换成负数去处理，最后再返回正确的值
    // 因为在计算机里负数的范围比正数大


    public static void main(String[] args) {
        System.out.println(try2(-2147483648));
    }
}
