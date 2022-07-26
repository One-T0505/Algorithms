package Stack;

import java.util.Stack;

// 设计一个栈的数据结构，除了 pop、push外再加一个 getMin 方法，获取当前栈中所有元素的最小值，要求时间复杂度为O(1).
// 设计的栈类型可以使用现成的栈结构.
public class MyStack1 {
    public Stack<Integer> data;
    public Stack<Integer> min;

    public MyStack1() {
        this.data = new Stack<>();
        this.min = new Stack<>();
    }

    public void push(int val) {
        if (this.data.isEmpty() || val < this.min.peek())
            this.min.push(val);
        else
            this.min.push(this.min.peek());
        this.data.push(val);
    }

    public int pop() {
        if (this.data.isEmpty())
            throw new RuntimeException("栈已经为空，无法弹出元素");
        this.min.pop();
        return this.data.pop();
    }

    public int getMin() {
        if (this.min.isEmpty())
            throw new RuntimeException("栈已经为空，无法弹出元素");
        return this.min.peek();
    }

    public static void main(String[] args) {
        MyStack1 stack1 = new MyStack1();
        stack1.push(3);
        System.out.println(stack1.getMin());
        stack1.push(4);
        System.out.println(stack1.getMin());
        stack1.push(1);
        System.out.println(stack1.getMin());
        System.out.println(stack1.pop());
        System.out.println(stack1.getMin());
    }
}
