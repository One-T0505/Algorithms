package Recurse;

import java.util.Stack;

public class StackReverse {
    // 给一个栈，不申请别的数据结构，只使用递归，如何实现一个栈的逆序
    public static void reverse(Stack<Integer> stack){
        if (stack.isEmpty())
            return;
        int i = f(stack);
        reverse(stack);
        stack.push(i);
    }

    // f函数的作用是返回栈底元素，剩下的元素依次下落
    private static int f(Stack<Integer> stack) {
        int res = stack.pop();
        if (stack.isEmpty())
            return res;
        else {
            int last = f(stack);
            stack.push(res);
            return last;
        }
    }

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();
        stack.push(4);
        stack.push(9);
        stack.push(2);
        stack.push(7);
        reverse(stack);
        while (!stack.isEmpty())
            System.out.print(stack.pop() + "\t");
        System.out.println();
    }
}
