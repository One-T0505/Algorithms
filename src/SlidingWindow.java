import java.util.LinkedList;

public class SlidingWindow {
    private int L;
    private int R;
    private int[] arr;
    LinkedList<Integer> queen;  // 这里存的是元素下标

    public SlidingWindow(){}

    public SlidingWindow(int[] arr) {
        this.L = -1;
        this.R = 0;
        this.arr = arr;
        this.queen = new LinkedList<>();
    }

    // R 向右移动
    public void addItemFromRight(){
        if (this.R == arr.length)
            return;
        while (!queen.isEmpty() && arr[queen.peekLast()] <= arr[R])
            queen.pollLast();
        queen.addLast(R++);
    }

    // L 向右移动
    public void delItemFromLeft(){
        L++;
        if (L == arr.length)
            return;
        if (L == queen.peekFirst())
            queen.pollFirst();

    }

    // 获取当前窗口的最大值
    public Integer getCurMax(){
        if (!queen.isEmpty())
            return arr[queen.peekFirst()];
        return null;
    }
}
