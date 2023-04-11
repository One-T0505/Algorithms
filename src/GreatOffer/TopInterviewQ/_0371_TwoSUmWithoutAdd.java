package GreatOffer.TopInterviewQ;

/**
 * ymy
 * 2023/3/14 - 15 : 02
 **/

// 给你两个整数 a 和 b ，不使用运算符 + 和 - ，计算并返回两整数之和。

public class _0371_TwoSUmWithoutAdd {


    // 很明显，这道题目是要用到异或运算。

    public static int getSum(int a, int b) {
        int carry = 0;
        int res = 0;
        for (int i = 0; i < 32; i++) {
            // 提取出a和b第0位上异或后的结果。
            int one = (a >> i) & 1;
            int other = (b >> i) & 1;
            int pre = one ^ other;
            int now = pre ^ carry;
            res |= now << i;
            carry = (one == 1 && other == 1) || (pre == 1 && carry == 1) ? 1 : 0;
        }
        return res;
    }


    public static void main(String[] args) {
        int a = -3;
        int b = -7;
        System.out.println(getSum(a, b));
    }
}
