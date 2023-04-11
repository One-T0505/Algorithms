package Basic.SlidingWindow;

import java.util.LinkedList;

public class SlidingWindow {
    private int L;
    private int R;
    private int[] arr;
    LinkedList<Integer> queue;  // 这里存的是元素下标

    public SlidingWindow(int[] arr) {
        L = -1;
        R = 0;
        this.arr = arr;
        queue = new LinkedList<>();
    }

    // R 向右移动
    public void addItemFromRight(){
        if (R == arr.length)
            return;
        while (!queue.isEmpty() && arr[queue.peekLast()] <= arr[R])
            queue.pollLast();
        queue.addLast(R++);
    }

    // L 向右移动
    public void delItemFromLeft(){
        L++;
        if (L == arr.length)
            return;
        if (!queue.isEmpty() && L == queue.peekFirst())
            queue.pollFirst();

    }

    // 获取当前窗口的最大值
    public Integer getCurMax(){
        if (!queue.isEmpty())
            return arr[queue.peekFirst()];
        return null;
    }
}
