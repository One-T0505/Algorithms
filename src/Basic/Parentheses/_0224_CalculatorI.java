package Basic.Parentheses;

// 先去学习 CalculatorIII

// 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
// 注意:不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。

// 1 <= s.length <= 3 * 10^5
// s 由数字、'+'、'-'、'('、')'、和 ' ' 组成
// s 表示一个有效的表达式
// '+' 不能用作一元运算(例如， "+1" 和 "+(2 + 3)" 无效)
// '-' 可以用作一元运算(即 "-1" 和 "-(2 + 3)" 是有效的)
// 输入中不存在两个连续的操作符  每个数字和运行的计算将适合于一个有符号的 32 位整数


import java.util.LinkedList;

public class _0224_CalculatorI {


    public static int calculate(String s) {
        if (s == null || s.length() < 1)
            return 0;
        return f(s.toCharArray(), 0)[0];
    }

    private static int[] f(char[] exp, int i) {
        LinkedList<String> queue = new LinkedList<>();
        int cur = 0;
        while (i < exp.length && exp[i] != ')') {
            if (exp[i] >= '0' && exp[i] <= '9')
                cur = cur * 10 + exp[i++] - '0';
            else if (exp[i] == ' ') {
                i++;
            } else if (exp[i] != '(') { // 碰到了运算符号
                queue.add(String.valueOf(cur));
                queue.addLast(String.valueOf(exp[i++]));
                cur = 0;
            } else { // 碰到了左括号
                int[] info = f(exp, i + 1);
                cur = info[0];
                i = info[1] + 1;
            }
        }
        queue.addLast(String.valueOf(cur));
        return new int[]{getNum(queue), i};
    }

    private static int getNum(LinkedList<String> queue) {
        boolean isPos = true; // 判断最开始的符号是正还是负
        int res = 0;
        String cur = null;
        while (!queue.isEmpty()) {
            cur = queue.pollFirst();
            if (cur.equals("+"))
                isPos = true;
            else if (cur.equals("-")) {
                isPos = false;
            } else { // 碰到了运算数
                int num = Integer.parseInt(cur);
                res += isPos ? num : -num;
            }
        }
        return res;
    }
}
