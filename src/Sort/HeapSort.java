package Sort;

// 以大根堆为例
public class HeapSort {

    public static void insert(Heap heap, int val){
        if (heap.capacity < heap.limit){
            int index = heap.capacity;
            heap.heap[heap.capacity++] = val;
            while ((index - 1)/2 >= 0 && heap.heap[index] > heap.heap[(index - 1)/2]){
                swap(heap.heap, index, (index - 1)/2);
                index = (index - 1)/2;
            }
        }
    }

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


    public static void main(String[] args) {
        Heap heap = new Heap(11);
        for (int i = 0; i < 8; i++) {
            HeapSort.insert(heap, i + 2);
        }
        for (int i = 0; i < heap.capacity; i++) {
            System.out.print(heap.heap[i] + "\t");
        }
        System.out.println();
        int res = HeapSort.pop(heap);
        System.out.println(res);
        for (int i = 0; i < heap.capacity; i++) {
            System.out.print(heap.heap[i] + "\t");
        }
        System.out.println();
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
