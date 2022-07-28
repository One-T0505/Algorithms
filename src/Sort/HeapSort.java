package Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

// 以大根堆为例
public class HeapSort {

    // 向已排好的堆中插入一个新的元素
    public static void insert(Heap heap, int val){
        if (heap.capacity == heap.limit)
            throw new RuntimeException("heap is full");
        else if (heap.capacity < heap.limit){
            int index = heap.capacity;
            heap.heap[heap.capacity++] = val;
            while ((index - 1)/2 >= 0 && heap.heap[index] > heap.heap[(index - 1)/2]){
                swap(heap.heap, index, (index - 1)/2);
                index = (index - 1)/2;
            }
        }
    }

    // 堆的弹出操作
    public static int pop(Heap heap){
        int res = heap.heap[0];
        swap(heap.heap, 0, --heap.capacity);
        heapify(heap, 0);
        return res;
    }

    // 从index处的元素往下调整
    public static void heapify(Heap heap, int index) {
        int left = index * 2 + 1; // 左孩子下标
        while (left < heap.capacity){
            // 将index的左右孩子中较大的那个元素的下标记住，前提是index有右孩子
            int greater = left + 1 < heap.capacity && heap.heap[left + 1] > heap.heap[left] ?
                    left + 1 : left;
            greater = heap.heap[greater] > heap.heap[index] ? greater : index;
            if (greater == index)
                break;
            swap(heap.heap, greater, index);
            index = greater;
            left = index * 2 + 1;
        }
    }

    public static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 有了上面的两个基本的算法 pop、heapify 和 insert ，就可以实现堆排序了
    public static Heap heapSort(int[] arr){
        if (arr == null || arr.length < 2)
            return null;
        Heap heap = new Heap(arr.length);
        for (int i : arr) insert(heap, i);
        while (heap.capacity > 0){
            swap(heap.heap, 0, --heap.capacity);
            heapify(heap, 0);
        }
        return heap;
    }

    public static void display(int[] arr){
        for(int item : arr) System.out.print(item + "\t");
        System.out.println();
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
        PriorityQueue<Integer> heapMax = new PriorityQueue<>(new Comparator<Integer>() {
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

    // 当元素放进堆排好序后，如果修改了堆上元素的值，就不能保证结果依然是对的。如果已经生成了一个小根堆：
    // 4， 7， 9， 11， 13， 10， 12。如果手动修改7->14，那么堆就不对了，但是系统可以自己优化，就是遍历每个元素，就当是
    // 当前元素新插入堆，跟上面insert方法思路一致。但是这样的效率为O(NlogN)，这里我们自己实现O(logN)的优化.
    public static class MyHeap<T> {
        private ArrayList<T> heap;
        private HashMap<T, Integer> indexMap; // 记录元素在堆上的位置
        private int capacity;
        private Comparator<? super T> comparator;

        public MyHeap(Comparator<? super T> comparator) {
            this.heap = new ArrayList<>();
            this.indexMap = new HashMap<>();
            this.capacity = 0;
            this.comparator = comparator;
        }

        public boolean isEmpty(){
            return this.capacity == 0;
        }

        public int size(){
            return this.capacity;
        }

        public boolean contains(T key){
            return this.indexMap.containsKey(key);
        }

        public void push(T val){
            this.heap.add(val);
            this.indexMap.put(val, this.capacity);
            heapInsert(this.capacity++);
        }

        private void heapInsert(int index) {
            while ((index - 1)/2 >= 0 &&
                    this.comparator.compare(this.heap.get(index), this.heap.get((index - 1)/2)) < 0){
                swap(index, (index - 1)/2);
                index = (index - 1)/2;
            }
        }

        private void swap(int i, int j){
            T o1 = this.heap.get(i);
            T o2 = this.heap.get(j);
            this.heap.set(i, o2);
            this.heap.set(j, o1);
            this.indexMap.put(o1, j);
            this.indexMap.put(o2, i);
        }

        public T pop(){
            T res = this.heap.get(0);
            int end = this.capacity - 1;
            swap(0, end);
            this.heap.remove(end);
            this.indexMap.remove(res);
            heapify(0, --this.capacity);
            return res;
        }

        // 从index处heapify
        private void heapify(int index, int capacity) {
            int left = 2 * index + 1;
            while (left < capacity){
                int greater = left + 1 < capacity &&
                        (this.comparator.compare(this.heap.get(left), this.heap.get(left + 1)) < 0) ?
                        left : left + 1;
                greater = this.comparator.compare(this.heap.get(greater), this.heap.get(index)) < 0 ?
                        greater : index;
                if (greater == index)
                    break;
                swap(greater, index);
                index = greater;
                left = 2 * index + 1;
            }
        }

        // 调用resign的时候是：已经手动将堆上的某个元素的值改为val了 比如：this.heap.set(某个index, val);
        // 所以此时堆的规律已经被打破了，因此再调用resign来重新让堆变有序，并且是O(logN)的复杂度.
        public void resign(T val){
            int valIndex= this.indexMap.get(val);
            // 重新修改的值，不是向上调整heapInsert，就是向下调整heapify，两者必中其一.
            heapInsert(valIndex);
            heapify(valIndex, this.capacity);
        }
    }

    public static void main(String[] args) {
        int[] arr= {10, 5, 2, 6, 8, 3, 7};
        Heap heap = heapSort(arr);
        HeapSort.display(heap.heap);
    }
}

class Heap{
    public int[] heap;
    public int capacity;    //表示堆中存放了多少个元素
    public int limit;       //数组最大容量

    public Heap(int limit) {
        this.limit = limit;
        this.capacity = 0;
        this.heap = new int[limit];
    }
}

