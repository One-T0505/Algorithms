package Basic.Queue;

import java.util.Stack;

// leetCode232
// 只用栈来实现队列
// 申请两个栈，一个push，一个pop。一个元素入队时，先加入push栈，等到要出队时，将push栈中所有元素逐一出栈并进入pop栈，
// 再将pop栈中所有元素逐一出栈，就得到相同的入队顺序。
// 注意：1.pop栈中还有元素时，千万不能让push中的元素进入pop栈  2.push栈向pop栈倒数据时必须一次倒完，不可以有剩余
public class StackQueue {
    public Stack<Integer> push;
    public Stack<Integer> pop;

    public StackQueue() {
        push = new Stack<>();
        pop = new Stack<>();
    }


    // 元素入队
    public void add(int val){
        push.push(val);
        pushToPop();
    }


    // 元素出队列
    public int poll(){
        if (pop.isEmpty() && push.isEmpty())
            throw new RuntimeException("queue is empty");
        pushToPop();
        return pop.pop();
    }


    // push栈向pop栈倒数据
    private void pushToPop(){
        if (pop.isEmpty()){
            while (!push.isEmpty())
                pop.push(push.pop());
        }
    }
}
