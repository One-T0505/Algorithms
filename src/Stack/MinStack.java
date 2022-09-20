package Stack;

import java.util.Stack;

// 设计一个栈的数据结构，除了 pop、push外再加一个 getMin 方法，获取当前栈中所有元素的最小值，要求时间复杂度为O(1).
// 设计的栈类型可以使用现成的栈结构.
// 思路：用两个栈data和min；data用于正常存放元素，min用于同步记录当前栈中最小元素
//      有一个元素入栈，正常加入data，如果该元素比min中栈顶元素小，则入栈，否则min重复进入栈顶元素。
//      不管何时，data中的元素数量和min中的数量都是相同的。
public class MinStack {
    public Stack<Integer> data;
    public Stack<Integer> min;

    public MinStack() {
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
        MinStack stack1 = new MinStack();
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
