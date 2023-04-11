package GreatOffer.class35;


// 给定一个长度len，表示一共有几位
// 所有字符都是小写(a~z)，可以生成长度为1，长度为2，长度为3...长度为len的所有字符串。
// 如果把这些长度<=len的所有字符串根据字典序排序，每个字符串都有所在的位置。
// 给定一个字符串str，给定len，请返回str是总序列中的第几个。
// 比如len = 4，字典序的前几个字符串为:
// a aa aaa aaaa aaab ... aaaz ... azzz b ba baa baaa ... bzzz c ...
// a是这个序列中的第1个，bzzz是这个序列中的第36558个

public class _01_Code {

    // 思路：
    // cdb，总共长度为7，请问cdb是第几个？
    // 第一位为c:     以a开头，剩下长度为(0~6)的所有可能性有几个 +
    //               以b开头，剩下长度为(0~6)的所有可能性有几个 +
    //               以c开头，剩下长度为(0)的所有可能性有几个   +
    //
    // 第二位为d : 以ca开头的情况下，剩下长度为(0~5)的所有可能性有几个 +
    //            以cb开头的情况下，剩下长度为(0~5)的所有可能性有几个 +
    //            以cc开头的情况下，剩下长度为(0~5)的所有可能性有几个 +
    //            以cd开头的情况下，剩下长度为(0)的所有可能性有几个   +
    //
    // 第三位为b ： 以cda开头的情况下，剩下长度为(0~4)的所有可能性有几个 +
    //            以cdb开头的情况下，剩下长度为(0)的所有可能性有几个

    public static int kth(String s, int len) {
        if (s == null || s.length() == 0 || s.length() > len)
            return -1;
        char[] chars = s.toCharArray();
        int N = chars.length;
        int res = 0;
        // 比如：dac len=3
        // res = 3 * f(2) + 1  这是循环第一次 表示的是：a__  b__  c__  d  这就是 3 * f(2) + 1 的组成
        //    += 0 * f(1) + 1  这是循环第二次 表示的是：da
        //    += 2 * f(0) + 1  这是循环第三次 表示的是：daa  dab   dac
        for (int i = 0, rest = len - 1; i < N; i++, rest--)
            res += (chars[i] - 'a') * f(rest) + 1;

        return res;
    }


    // 不管以什么开头，该方法返回剩下长度从0～len的字符一共有多少个
    // 比如f(2) 表示：""  "a"  "b" ... "z"  "aa" ... "az" ... "za" ... "zz"
    // 所以f(2)应该返回：1 + 26 + 26^2
    // 所以f(i)应该返回：1 + 26 + ... + 26^i
    private static int f(int len) {
        int res = 1;
        for (int i = 1, base = 26; i <= len; i++, base *= 26)
            res += base;
        return res;
    }
}
