package GreatOffer.TopInterviewQ;


// 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数
// （也被称为汉明重量）。

public class _0191_HammingWeight {

    // 每次取出最右侧的1
    public int hammingWeight(int n) {
        int bits = 0;
        int mostRight = 0;
        while (n != 0) {
            mostRight = n & (-n);
            n ^= mostRight;
            bits++;
        }
        return bits;
    }
}
