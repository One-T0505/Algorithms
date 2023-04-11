package GreatOffer.class36;

// 来自网易
// 规定：L[1]对应a，L[2]对应b，L[3]对应c，...，L[25]对应y  z没有对应
// S1 = a
// S(i) = S(i-1) + L[i] + reverse(invert(S(i-1)));
// 解释invert操作：
// S1 = a
// S2 = aby
// 假设invert(S(2)) = 甲乙丙
// 要满足 a + 甲 = 26, 那么 甲 = 26 - 1 = 25 -> y
// 要满足 b + 乙 = 26, 那么 乙 = 26 - 2 = 24 -> x
// 要满足 y + 丙 = 26, 那么 丙 = 26 - 25 = 1 -> a
// 如上就是每一位的计算方式，所以invert(S2) = yxa  就是让S2的每一个字符都凑成26
// 所以S3 = S2 + L[3] + reverse(invert(S2)) = aby + c + axy = abycaxy
// invert(abycaxy) = yxawyba, 再reverse = abywaxy
// 所以S4 = abycaxy + d + abywaxy = abycaxydabywaxy
// 直到S25结束  因为 S26 == S25 + L[26] + reverse(invert(S(25)))  L[26]是没对应的
// 给定两个参数n和k，返回Sn的第k位是什么字符，n从1开始，k从1开始
// 比如n=4，k=2，表示S4的第2个字符是什么，返回b字符

public class _01_Code {

    // 其实我们可以发现一个规律： Si的长度 == S(i-1)的长度 + 1 + S(i-1)的长度
    // 不管第三部分经过了invert和reverse操作，其长度不会改变。所以，S1～S25的长度都是固定的可以求出来
    // S1 = 1  S2 = 1 + 1 + 1 = 3  S3 = 3 + 1 + 3 = 7  S4 = 7 + 1 + 7 = 15.....
    // 可以发现 Si的长度 == 2^i - 1
    public static int[] lens = null;

    // 将lens做成一个类变量，让其只用完成一次之后，接下来就可以不断使用
    public static void fillLens() {
        lens = new int[26];
        lens[1] = 1;
        for (int i = 2, base = 4; i < 26; i++, base <<= 1)
            lens[i] = base - 1;
    }


    // 主方法
    // 问题是要求 Sn中的第k个字符是什么  那我们就可以根据已经事先填好的lens数组中查询到Sn的长度，然后看k是属于
    // 哪个部分的。比如：要求S5中的第k个字符是什么，我们可以知道S5的长度由三部分组成：15 + 1 + 15
    // 如果k<=15，那其实就是求S4中第k个字符是多少，就变成递归了；如果k==16，那么直接返回L5，如果k>16，
    // 那么其实就是求第三部分的第k-16个字符是什么，第三部分是reverse过的，所以就是求第三部分reverse前的第
    // 15-(k-16)+1 == 32-k 个字符，也就是求S4 invert后的第32-k个字符。
    public static char kth(int n, int k) {
        if (lens == null)
            fillLens();
        if (n == 1)
            return 'a';
        int part = lens[n - 1];  // 找出Sn的第一部分和第三部分的长度，即S(n-1)的长度
        if (k <= part)
            return kth(n - 1, k);
        else if (k == part + 1)
            return (char) ('a' + n - 1);
        else {
            // 我需要第三部分，从左往右的第a个
            // 需要找到，s(n-1)从右往左的第a个
            // 当拿到字符之后，invert一下，就可以返回了！
            return invert(kth(n - 1, ((part + 1) << 1) - k));
        }

    }

    private static char invert(char ori) {
        return (char) ('a' + (24 - (ori - 'a')));
    }
}
