package Sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import utils.arrays;

// 以大根堆为例
public class HeapSort {

    public int[] arr;
    public int size;    //表示堆中存放了多少个元素
    public int limit;       //数组最大容量

    public HeapSort(int limit) {
        this.limit = limit;
        size = 0;
        arr = new int[limit];
    }


    // 向已排好的堆中插入一个新的元素
    public void insert(int val){
        if (size == limit)
            throw new RuntimeException("heap is full");
        else if (size < limit){
            int index = size;
            arr[size++] = val;
            // 可以简化成：while (heap.heap[index] > heap.heap[(index - 1)/2]) 因为index=0时，来到根时，
            // (index - 1)/2 = 0，所以 heap.heap[index] > heap.heap[(index - 1)/2] 不成立，效果一样，
            // 来到根时也会跳出循环
            while ((index - 1) / 2 >= 0 && arr[index] > arr[(index - 1) / 2]){
                arrays.swap(arr, index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }
    }

    // 堆的弹出操作
    public int pop(){
        int res = arr[0];
        arrays.swap(arr, 0, --size);
        heapify(arr, 0, size);
        return res;
    }

    // 从index处的元素往下调整
    private void heapify(int[] arr, int index, int capacity) {
        int left = index << 1 | 1; // 左孩子下标
        while (left < capacity){
            // 将index的左右孩子中较大的那个元素的下标记住，前提是index有右孩子
            int greater = left + 1 < capacity && arr[left + 1] > arr[left] ?
                    left + 1 : left;
            greater = arr[greater] > arr[index] ? greater : index;
            if (greater == index)
                break;
            arrays.swap(arr, greater, index);
            index = greater;
            left = index << 1 | 1;
        }
    }

    // 有了上面的两个基本的算法 pop、heapify 和 insert ，就可以实现堆排序了
    public void heapSort(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        // 第一步    建堆
        for (int i : arr) insert(i);
        // 第二步    迭代
        while (size > 0){
            arrays.swap(this.arr, 0, --size);
            heapify(this.arr, 0, size);
        }
    }
    // 堆排序分成了两步：第一步建堆时间复杂度为O(NlogN)   第二步迭代时间复杂度为O(NlogN)
    // 这里有一个更优化的堆排序，只是优化了第一步，使建堆的时间复杂度为O(N)  第二步还是一样的，
    // 虽然总体上时间复杂度依然是O(NlogN)   但确实是更快了
    // 下面就是优化版的建堆方法


    // 有一种优化的方法让堆排序更快。一开始给了一个无序的数组，假设数组的长度为N，把该数组想象成一棵完全二叉树。
    // 那么叶结点的数量必为 ⌊N/2⌋ + 1，并且是N个元素最后的 ⌊N/2⌋ + 1 个元素是叶结点，它们在数组中都是连续的。
    // 非叶结点的数量为 ⌊N/2⌋ 个，也就是数组下标从0～⌊N/2⌋-1的元素为非叶结点。
    // 我们只需要从 ⌊N/2⌋-1~0 依次向下调整即可。这样就可以让该数组成为一个大根堆，但是让整个数组有序还需要继续调整
    // 这种优化方法必须是一次性给出一堆数据，不能每次插入一个数据。这种建堆的方法只需要 O(N) 就可以完成。
    public void heapSortV2(int[] arr){
        if (arr == null || arr.length < 2)
            return;
        int size = arr.length;
        int bound = arr.length / 2 - 1;   // 第一个非叶结点
        // 优化后的建堆
        while (bound >= 0){
            heapify(arr, bound--, size);
        }
        // 第二步还是一样
        while (size > 0){
            arrays.swap(arr, 0, --size);
            heapify(arr, 0, size);
        }

    }


    // 与堆相关的题目：
    // 已知一个几乎有序的数组。几乎有序是指：如果把数组排好顺序的话，每个元素移动的距离一定不超过k，并且k相对于数组长度来说
    // 是比较小的。请选择一个合适的排序策略，对这个数组进行排序。
    // 思路：整个数组最小的数一定在索引0-5之间，如果不爱这个范围内就不符合题意。所以，可以先把0-5索引处的值依次加入
    // 小根堆。
    // 补充：Java中堆的实现是用 PriorityQueue ，并且默认是小根堆
    public static void distanceLessK(int[] arr, int k){
        if (k >= arr.length)
            return;
        // 默认小根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        // 如果想生成大根堆
        PriorityQueue<Integer> heapMax = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        int index = 0, i = 0;
        for (; index <= Math.min(arr.length - 1, k); index++)
            heap.add(arr[index]);
        for (; index < arr.length; i++, index++){
            heap.add(arr[index]);
            arr[i] = heap.poll();
        }
        while (!heap.isEmpty())
            arr[i++] = heap.poll();
    }

    // 最大线段重合问题(用堆的实现)  给定很多线段，每个线段都有两个数[start, end],表示线段开始位置和结束位置，左右都是闭区间
    // 规定: 1)线段的开始和结束位置一定都是整数值  2)线段重合区域的长度必须>=1
    // 返回线段最多重合区域中，包含了几条线段
    //
    // 思路：先把所有线段按起点从小到大排序，再申请一个小根堆，里面存放的是线段的终点，每次一条线段来，
    // 就从堆里删除所有终点<=当前线段起点 的线段，因为这些线段肯定比当前线段先来进行判断，所以他们的起点一定 <= 当前线段，并且
    // 堆里存放的是他们的终点，依然不大于当前结点的起点，那么这些线段必然和当前线段没有重合区域。

    // 矩阵m的形状为：N ✖ 2
    public static int lineOverlap(int[][] m){
        // 先将矩阵转化为线段类
        Line[] lines = new Line[m.length];
        for (int row = 0; row < m.length; row++)
            lines[row] = new Line(m[row][0], m[row][1]);
        Arrays.sort(lines, (a, b) -> a.start - b.start);

        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int max = Integer.MIN_VALUE;
        for (int cur = 0; cur < lines.length; cur++) {
            while (!heap.isEmpty() && heap.peek() <= lines[cur].start)
                heap.poll();
            heap.add(lines[cur].end);
            max = Math.max(max, heap.size());
        }
        return max;
    }


    public static class Line {
        public int start;
        public int end;

        public Line(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
    public static void main(String[] args) {
        int[] arr= {10, 1, 5, 2, 6, 8, 3, 7, 13, 2};
        HeapSort heapSort = new HeapSort(arr.length);
        heapSort.heapSortV2(arr);
        arrays.printArray(arr);
    }
}
