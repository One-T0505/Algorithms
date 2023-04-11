package GreatOffer.TopInterviewQ;


// 请你来实现一个 myAtoi(string s) 函数，使其能将字符串转换成一个 32 位有符号整数。
// 函数 myAtoi(string s) 的算法如下：
//  1.读入字符串并丢弃无用的前导空格
//  2.检查下一个字符（假设还未到字符末尾)为正还是负号，读取该字符（如果有。 确定最终结果是负数还是正数。
//    如果两者都不存在，则假定结果为正。
//  3.读入下一个字符，直到到达下一个非数字字符或到达输入的结尾。字符串的其余部分将被忽略。
// 将前面步骤读入的这些数字转换为整数（即，"123" -> 123， "0032" -> 32）。如果没有读入数字，则整数为0。必要时
// 更改符号（从步骤 2 开始）。如果整数数超过 32 位有符号整数范围 [−2^31,  2^31 − 1] ，需要截断这个整数，使其
// 保持在这个范围内。具体来说，小于 −2^31 的整数应该被固定为 −2^31 ，大于 2^31 − 1 的整数应该被固定为 2^31 − 1。
// 返回整数作为最终结果。
//
// 注意：
//  本题中的空白字符只包括空格字符 ' ' 。
//  除前导空格或数字后的其余字符串外，请勿忽略任何其他字符。

public class _0008_StringToInteger {


    // 这个题目需要清洗给定的字符串让它符合一个科学规范的可以转换成数字的字符串，因为过滤不涉及到核心逻辑，所以不重要
    // 我们现在就当给的是一个合乎规范的可以转换成数字的字符串，来分析核心逻辑。
    // 核心逻辑和0007题一样，就是要把数全部先按负数处理，因为这样范围更大，更安全一些。

    private static int transfer(char[] chars) {
        boolean neg = chars[0] == '-';  // 先定义正负
        int N = chars.length;
        int res = 0;
        int cur = 0;
        final int M = Integer.MIN_VALUE / 10;
        final int O = Integer.MIN_VALUE % 10;
        for (int i = (chars[0] == '-' || chars[0] == '+') ? 1 : 0; i < N; i++) {
            // 如果 chars[i]=='3'  那么cur==-3，始终让cur表示当前位的负数
            cur = '0' - chars[i];
            // 提前判断是否越界，思路和0007一样
            if (res < M || (res == M && cur < O))
                return neg ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            res = res * 10 + cur;
        }
        // for循环里已经将超过负数边界的数过滤掉了，但不代表过滤干净了，如果给的字符串是正数且为："2147483648"，
        // 虽然我们把所有数当成负数来处理，且-2147483648刚好不越界，但是现在要返回真实值2147483648了，这个数
        // 在正数范围内是越界的，所以这个数是唯一没过滤掉的，需要单独判断

        if (!neg && res == Integer.MIN_VALUE)
            return Integer.MAX_VALUE;

        return neg ? res : -res;
    }


    // 主方法
    public static int myAtoi(String s) {
        if (s == null || s.equals(""))
            return 0;
        s = removeHeadZero(s.trim());  // trim()只能去除首尾的空格，中间的空格无法去除
        if (s == null || s.equals(""))
            return 0;
        char[] str = s.toCharArray();
        if (!isValid(str))
            return 0;
        // 至此，str已是符合日常书写的，正经整数形式
        return transfer(str);
    }


    // 去掉开头若干个0，并删除结尾无效部分，返回的是有效的子串
    private static String removeHeadZero(String str) {
        boolean r = (str.startsWith("+") || str.startsWith("-"));
        int s = r ? 1 : 0;
        for (; s < str.length(); s++) {
            if (str.charAt(s) != '0') {
                break;
            }
        }
        // s 到了第一个不是'0'字符的位置
        int e = -1;
        // 左<-右
        for (int i = str.length() - 1; i >= (r ? 1 : 0); i--) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                e = i;
            }
        }
        // e 到了最左的 不是数字字符的位置
        return (r ? String.valueOf(str.charAt(0)) : "") +
                str.substring(s, e == -1 ? str.length() : e);
    }


    //
    private static boolean isValid(char[] str) {
        if (str[0] != '-' && str[0] != '+' && (str[0] < '0' || str[0] > '9')) {
            return false;
        }
        if ((str[0] == '-' || str[0] == '+') && str.length == 1) {
            return false;
        }
        // 0 +... -... num
        for (int i = 1; i < str.length; i++) {
            if (str[i] < '0' || str[i] > '9') {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        System.out.println("  adwds adwgrfde ".trim().length());
    }
}
