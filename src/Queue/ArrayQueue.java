package Queue;

public class ArrayQueue {
    public int[] data;
    public int front;
    public int rear;
    public int size;
    public int limit;

    public ArrayQueue(int limit) {
        this.data = new int[limit];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
        this.limit = limit;
    }

    public void add(int val){
        if (this.size == this.limit)
            throw new RuntimeException("队列满了，不能再加了");
        this.size++;
        this.data[this.rear] = val;
        this.rear = nextIndex(this.rear);
    }

    public int poll() {
        if (this.size == 0)
            throw new RuntimeException("队列已空，无法再弹出元素");
        int e = this.data[this.front];
        this.size--;
        this.front = nextIndex(this.front);
        return e;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int nextIndex(int rear) {
        return rear < this.limit - 1 ? rear + 1 : 0;
    }
}
