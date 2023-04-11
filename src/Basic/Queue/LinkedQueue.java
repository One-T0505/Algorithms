package Basic.Queue;

import Basic.SystemDesign.DoubleEndsLinkedList;

public class LinkedQueue {
    public DoubleEndsLinkedList queue;

    public LinkedQueue() {
        this.queue = new DoubleEndsLinkedList();
    }

    // 入队
    public void add(int val){
        queue.addToTail(val);
    }

    // 出队
    public int poll(){
        return queue.pollFromHead();
    }
}
