package GreatOffer.TopInterviewQ;

// 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
// 整数除法仅保留整数部分。
// 你可以假设给定的表达式总是有效的。所有中间结果将在 [-231, 231 - 1] 的范围内。
// 注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。

// 1 <= s.length <= 3 * 10^5
// s 由整数和算符 ('+', '-', '*', '/') 组成，中间由一些空格隔开
// s 表示一个 有效表达式
// 表达式中的所有整数都是非负整数，且在范围 [0, 2^31 - 1] 内
// 题目数据保证答案是一个 32-bit 整数

public class _0227_CalculatorII {


    // 注意：这道题是有空格的
    public static int calculate(String s) {
        if (s == null)
            return Integer.MAX_VALUE;
        if (s.length() < 2)
            return Integer.parseInt(s);
        char[] chs = s.toCharArray();
        int N = chs.length;
        int[] stack = new int[N];
        int top = -1;
        int num = 0;
        char preSign = '+';
        for (int i = 0; i < N; i++) {
            if (Character.isDigit(chs[i]))
                num = num * 10 + chs[i] - '0';
            if ((!Character.isDigit(chs[i]) && chs[i] != ' ') || i == N - 1){
                switch (preSign){
                    case '+' -> stack[++top] = num;
                    case '-' -> stack[++top] = -num;
                    case '*' -> {
                        int former = stack[top--];
                        stack[++top] = former * num;
                    }
                    case '/' -> {
                        int former = stack[top--];
                        stack[++top] = former / num;
                    }
                }
                num = 0;
                preSign = chs[i];
            }
        }

        int res = 0;
        while (top != -1)
            res += stack[top--];
        return res;
    }


    public static void main(String[] args) {
        String s = "3+2*2";
        System.out.println(calculate(s));
    }
}
