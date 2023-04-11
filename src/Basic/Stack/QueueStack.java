package Basic.Stack;

import java.util.LinkedList;
import java.util.Queue;

// leetCode225
// 只用队列来实现栈

// 尾          头         尾            头
// --------------        ----------------
// 3  7  4  2  9   -->    7   4   2   9
// --------------        ----------------
//   queue                    help
//
// 用两个队列实现栈，当栈需要弹出一个元素时，就是说要把queue队列中的元素除了最后一个以外其余全部入队到help中，然后将最后一个元素出队
// 然后把queue，help互换一下就行了。所以每次加元素直接入队help，而弹出元素则将queue中的元素
// 当需要进入元素时，栈加入元素，因为该新元素是最先返回的，所以直接在help中入队即可
public class QueueStack {
    public Queue<Integer> queue;
    public Queue<Integer> help;

    public QueueStack() {
        queue = new LinkedList<>();
        help = new LinkedList<>();
    }

    public void push(int val){
        help.add(val);
    }

    public int pop(){
        while (help.size() > 1)
            queue.add(help.poll());
        int res = help.poll();
        // 交换队列位置
        Queue<Integer> tmp = help;
        help = queue;
        queue = tmp;
        return res;
    }

    public int peek(){
        while (help.size() > 1)
            queue.add(help.poll());
        int res = help.poll();
        // 交换队列位置
        queue.add(res);
        Queue<Integer> tmp = help;
        help = queue;
        queue = tmp;
        return res;
    }

    public static void main(String[] args) {
        QueueStack stack = new QueueStack();
        stack.push(3);
        stack.push(7);
        stack.push(5);
        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }
}
