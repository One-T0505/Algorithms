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
        data = new int[limit];
        front = 0;
        rear = 0;
        size = 0;
        this.limit = limit;
    }

    public void add(int val){
        if (size == limit)
            throw new RuntimeException("队列满了，不能再加了");
        size++;
        data[rear] = val;
        rear = nextIndex(rear);
    }

    public int poll() {
        if (size == 0)
            throw new RuntimeException("队列已空，无法再弹出元素");
        int e = data[front];
        size--;
        front = nextIndex(front);
        return e;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int nextIndex(int index) {
        return index < limit - 1 ? index + 1 : 0;
    }
}
