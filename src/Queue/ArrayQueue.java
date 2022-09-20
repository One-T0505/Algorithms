package Queue;

// 该结构中使用了两个新成员变量：size表示队列中当前有多少元素；limit表示队列中最多放多少元素
// 用这两个元素可以避免传统的数组实现队列时需要判断的追尾问题
public class ArrayQueue {
    public int[] data;
    public int front;
    public int rear;
    public int size;
    public  final int limit;

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

    public int nextIndex(int index) {
        return index < this.limit - 1 ? index + 1 : 0;
    }
}
