package Basic.SlidingWindow;

// leetCode295 题是这道题目的初级版
// 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。
// 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。

import java.util.Comparator;
import java.util.PriorityQueue;

public class _0480_Code {

    public static double[] medianSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length)
            return null;
        int N = nums.length;
        double[] res = new double[N - k + 1];
        int p = 0;
        MyHeap heap = new MyHeap(k);
        for (int i = 0; i < k - 1; i++) {
            heap.add(nums[i]);
        }
        for (int i = k - 1; i < N; i++) {
            heap.add(nums[i]);
            res[p++] = heap.getMedian();
            heap.remove(nums[i - k + 1]);
        }
        return res;
    }


    // 我们需要一个增强堆，来快速定位堆中的元素并将其删除
    public static class MyHeap {

        // former用于记录目前已知元素的较小半个部分的值  采用大根堆形式
        public PriorityQueue<Integer> former;


        // latter用于记录目前已知元素的较大半个部分的值  采用小根堆形式
        public PriorityQueue<Integer> latter;


        public int k;

        public MyHeap(int k){
            this.k = k;
            // 这里发现了一个很奇怪的现象：如果用lambda表达式创建former大根堆时，比如 (a, b) -> b - a
            // 会有bug，就是在大根堆中，会认为系统最小值 > 系统最大值，其余情况正常，这可能是补码的问题，
            // 如果用下面的方式的话，就不会有这个奇怪的错误
            former = new PriorityQueue<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o2.compareTo(o1);
                }
            }); // 大根堆
            latter = new PriorityQueue<>(); // 小根堆
        }


        public double getMedian() {
            return (k & 1) == 1 ? former.peek() : ((double) former.peek() + (double) latter.peek()) / 2;
        }


        public void add(int num) {
            if (former.isEmpty() || num <= former.peek())
                former.add(num);
            else
                latter.add(num);
            // 插入完元素后，可能会使两个堆的元素数量不平衡  需要调整
            keepBalanced();
        }



        public void remove(int num) {
            if (num <= former.peek())
                former.remove(num);
            else
                latter.remove(num);
            // 删除之后也可能会让两个堆的元素数量不平衡，同样也需要调整
            keepBalanced();
        }

        private void keepBalanced() {
            if (former.size() > latter.size() + 1)
                latter.add(former.poll());
            else if (former.size() < latter.size()) {
                former.add(latter.poll());
            }
        }
    }


    public static void main(String[] args) {
//        int[] arr = {-2147483648,-2147483648,2147483647,-2147483648,-2147483648,-2147483648,
//                2147483647,2147483647,2147483647,2147483647,-2147483648,2147483647,-2147483648};
//        int k = 3;
//        double[] res = medianSlidingWindow(arr, k);
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> b - a);
        heap.add(Integer.MAX_VALUE);
        heap.add(5);
        System.out.println(heap.peek());
    }
}
