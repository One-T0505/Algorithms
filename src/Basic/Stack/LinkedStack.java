package Basic.Stack;

import Basic.SystemDesign.DoubleEndsLinkedList;

// java底层是用双向链表来实现栈的，我们之前自己复现了双向链表，此时可以利用我们自己
// 写的双向链表来实现栈
public class LinkedStack {
    public DoubleEndsLinkedList stack;

    public LinkedStack() {
        this.stack = new DoubleEndsLinkedList();
    }

    // 入栈
    public void push(int val){
        stack.addToTail(val);
    }

    // 出栈
    public int pop(){
        return stack.pollFromTail();
    }
}
