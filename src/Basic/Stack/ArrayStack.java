package Basic.Stack;

public class ArrayStack {

    private int maxsize;
    private int[] stack;
    private int top = -1;

    public ArrayStack(int maxsize){
        this.maxsize = maxsize;
        stack = new int[maxsize];
    }

    public boolean isEmpty(){
        return this.top == -1;
    }

    public boolean isFull(){
        return this.top == this.maxsize - 1;
    }

    public void push(int elem){
        if (!isFull())
            stack[++this.top] = elem;
        else
            throw new RuntimeException("=====栈已满=====");
    }

    public int pop(int elem){
        if (!isEmpty()){
            elem = stack[this.top--];
            return elem;
        }
        else
            throw new RuntimeException("=====栈已空=====");
    }

}
