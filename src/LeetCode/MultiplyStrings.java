package LeetCode;

/**
 * ymy
 * 2023/3/25 - 15 : 07
 **/


// leetCode48
// 给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。
// 注意：不能使用任何内置的 BigInteger 库或直接将输入转换为整数。

public class MultiplyStrings {

    // 这里采取的思路是：用我们小时候用过的竖式乘法来完成计算。比如：123 * 456
    //
    //         4  5  6
    //         1  2  3                先用一个数把所有数乘一遍，填入答案数组中，这里面就包含了进位。
    //--------------------            当下一轮开始时，我们应该向上一次开始的位置往前移一位再开始。
    //      1  3  6  8
    //      9  1  2
    //   4  5  6
    //---------------------
    //   5  6  0  8  8
    //
    public static String multiply(String num1, String num2) {
        if (num1 == null || num2 == null || num1.length() == 0 || num2.length() == 0 ||
                num1.equals("0") || num2.equals("0"))
            return "0";
        char[] c1 = num1.toCharArray();
        char[] c2 = num2.toCharArray();
        int N = c1.length;
        int M = c2.length;
        int[] res = new int[N + M];
        // 这里有一个小常识：N位数和M位数相乘，得到的结果最多就是 N + M 位数，所以 N+M 的空间一定够用
        int pre = N + M - 1;
        int p = 0;
        int carry = 0;
        for (int i = N - 1; i >= 0; i--){
            p = pre;
            carry = 0;
            int f = c1[i] - '0';
            for (int j = M - 1; j >= 0; j--){
                int l = c2[j] - '0';
                int cur = l * f + carry + res[p];
                res[p--] = cur % 10;
                carry = cur / 10;

            }
            res[p] += carry;
            pre--;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(res[p] == 0 ? "" : res[p]);
        for (int i = p + 1; i < N + M; i++) {
            sb.append(res[i]);
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        String n1 = "123";
        String n2 = "456";
        System.out.println(multiply(n1, n2));
    }
}
