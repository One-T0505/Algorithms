package GreatOffer.TopInterviewQ;

import java.util.HashMap;

// 给定两个整数，分别表示分数的分子 numerator 和分母 denominator，以字符串形式返回小数。
// 如果小数部分为循环小数，则将循环的部分括在括号内。
// 如果存在多个答案，只需返回任意一个 。
// 对于所有给定的输入，保证答案字符串的长度小于 10^4 。
// 比如： numerator == 1   denominator == 2  结果为 0.5
//       numerator == 2   denominator == 6  结果为 0.(3)

public class _0166_FractionToDecimal {


    // 首先要明白，两个整数相除，要么就是可以用有限位小数除尽，要么就是无限循环小数，不可能是无限不循环小数.
    public static String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0)
            return "0";
        StringBuilder sb = new StringBuilder();
        // 判断符号，若分子分母同号，就不用加负号
        sb.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");

        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);

        // 整数部分
        sb.append(num / den);
        num %= den;

        if (num == 0) // 说明可以除尽
            return sb.toString();

        // 小数部分  这里有一个技巧
        // 比如要算：1 / 3 的小数部分，整数部分算完了，此时num==1，我们只需要让num*10，然后再除den就能得到
        // 第一位小数的值，然后让num变为其余数。
        sb.append(".");
        HashMap<Long, Integer> map = new HashMap<>();  // 记录何时重复出现循环部分
        map.put(num, sb.length());

        while (num != 0) {
            num *= 10;
            sb.append(num / den);
            num %= den;
            if (map.containsKey(num)) {
                int index = map.get(num);
                sb.insert(index, "(");
                sb.append(")");
                break;
            } else {
                map.put(num, sb.length());
            }
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println(fractionToDecimal(2, 6));
    }
}
