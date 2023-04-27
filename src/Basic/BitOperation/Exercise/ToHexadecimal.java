package Basic.BitOperation.Exercise;

// leetCode405
// 给定一个整数，编写一个算法将这个数转换为十六进制数。对于负整数，我们通常使用补码运算方法。

// 注意
//  1.十六进制中所有字母(a-f)都必须是小写。
//  2.十六进制字符串中不能包含多余的前导零。如果要转化的数为0，那么以单个字符'0'来表示；对于其他情况，十六进制字符串
//    中的第一个字符将不会是0字符。
//  3.给定的数确保在32位有符号整数范围内。
//  4.不能使用任何由库提供的将数字直接转换或格式化为十六进制的方法。

public class ToHexadecimal {

    public static String toHex(int num) {
        if (num == 0)
            return "0";
        // 二进制中每4位对应16进制中的1位
        StringBuilder sb = new StringBuilder();
        // 我们的转换顺序是从高位往低位转换，每次转换4位
        for (int i = 7; i >= 0; i--) {
            // i << 2 == i * 4   左移4的倍数位
            int val = (num >> (i << 2)) & 0xf;
            // 有可能这一组4个二进制位都是0；如果sb.length() > 0  说明高位有不是0的，也就是说开头不是0了，所以
            // 当现在的4位转换出0也不用担心会是开头了。
            // 如果sb.length==0 说明之前二进制转换的数字都是0，那么当我们此时继续转换出来0了，是不能加进去的，因为
            // 加进去就变成了开头是0了
            if (sb.length() > 0 || val > 0)
                sb.append(val <= 9 ? (char) ('0' + val) : (char) ('a' + val - 10));
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        int val = 3;
        System.out.println((char) ('a' + val));
        System.out.println("a" + 3);
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.append(val).append('3'));
        System.out.println(toHex(26));
    }
}
