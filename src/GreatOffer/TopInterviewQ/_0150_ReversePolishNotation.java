package GreatOffer.TopInterviewQ;

import java.util.Stack;

// 给你一个字符串数组 tokens ，表示一个根据逆波兰表示法表示的算术表达式。
// 请你计算该表达式。返回一个表示表达式值的整数。
// 注意：
//  1.有效的算符为 '+'、'-'、'*' 和 '/' 。
//  2.每个操作数（运算对象）都可以是一个整数或者另一个表达式。
//  3.两个整数之间的除法总是向零截断 。
//  4.表达式中不含除零运算。
//  5.输入是一个根据逆波兰表示法表示的算术表达式。
//  6.答案及所有中间计算结果可以用 32 位 整数表示。

public class _0150_ReversePolishNotation {

    // 其实逆波兰表达式就是将有效的算术表达式改写成了后缀表达式
    // 只使用栈即可
    public int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for (String s : tokens) {
            if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/"))
                calculate(stack, s);
            else
                stack.push(Integer.parseInt(s));
        }
        return stack.peek();
    }

    private void calculate(Stack<Integer> stack, String operator) {
        Integer latter = stack.pop();
        Integer former = stack.pop();
        int res = 0;
        switch (operator) {
            case "+" -> res = former + latter;
            case "-" -> res = former - latter;
            case "*" -> res = former * latter;
            case "/" -> res = former / latter;
        }
        stack.push(res);
    }
}
