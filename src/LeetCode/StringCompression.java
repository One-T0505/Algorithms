package LeetCode;

/**
 * ymy
 * 2023/4/1 - 09 : 33
 **/

// leetCode443
// 给你一个字符数组 chars ，请使用下述算法压缩：
// 从一个空字符串 s 开始。对于 chars 中的每组 连续重复字符 ：
//  1.如果这一组长度为 1 ，则将字符追加到 s 中。
//  2.否则，需要向 s 追加字符，后跟这一组的长度。
// 压缩后得到的字符串 s 不应该直接返回 ，需要转储到字符数组 chars 中。需要注意的是，如果组长度为 10 或 10 以上，
// 则在 chars 数组中会被拆分为多个字符。
// 请在 修改完输入数组后 ，返回该数组的新长度。
// 你必须设计并实现一个只使用常量额外空间的算法来解决此问题。

// 1 <= chars.length <= 2000
// chars[i] 可以是小写英文字母、大写英文字母、数字或符号


public class StringCompression {


    // 有没有发现一个规律：如果一个字符只出现了一次，那么直接拷贝。如果出现次数>=2，那么压缩后占用的长度必然不会超过
    // 原始长度。 比如 a a -> a 2   a a a a -> a 4

    public static int compress(char[] chars) {
        if (chars == null)
            return 0;
        if (chars.length < 2)
            return chars.length;
        int times = 1;
        int p = 0;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] != chars[i - 1]){
                int n = digits(times);
                if (times > 1){
                    for (int j = p + n - 1; j >= p; j--) {
                        chars[j] = (char) (times % 10 + '0');
                        times /= 10;
                    }
                }
                p += n;
                chars[p] = chars[i];
                times = 1;
            } else {
                if (times++ < 2)
                    p++;
            }
        }
        int n = digits(times);
        if (times > 1){
            for (int j = p + n - 1; j >= p; j--) {
                chars[j] = (char) (times % 10 + '0');
                times /= 10;
            }
        }
        return p + n;
    }

    private static int digits(int num) {
        int len = 0;
        while (num != 0){
            num /= 10;
            len++;
        }
        return len;
    }


    public static void main(String[] args) {
        char[] a = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'g', 'g', 'g', 'g',
                'g', 'g', 'g', 'g', 'g', 'g', 'g', 'a', 'b', 'c'};
        System.out.println(compress(a));
    }
}
