package Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DynamicHeap<T> {
// 当元素放进堆排好序后，如果修改了堆上元素的值，就不能保证结果依然是对的。如果已经生成了一个小根堆：
// 4， 7， 9， 11， 13， 10， 12。如果手动修改7->14，那么堆就不对了，但是系统可以自己优化，就是遍历每个元素，就当是
// 当前元素新插入堆，跟上面insert方法思路一致。但是这样的效率为O(NlogN)，这里我们自己实现O(logN)的优化.
    private ArrayList<T> heap;
    private HashMap<T, Integer> indexMap; // 记录元素在堆上的位置
    private int size;
    private Comparator<? super T> comparator;

    public DynamicHeap(Comparator<? super T> comparator) {
        heap = new ArrayList<>();
        indexMap = new HashMap<>();
        size = 0;
        comparator = comparator;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public boolean contains(T key){
        return indexMap.containsKey(key);
    }

    public void push(T val){
        heap.add(val);
        indexMap.put(val, size);
        heapInsert(size++);
    }

    private void heapInsert(int index) {
        // 需要通过比较器来告诉我怎么比大小
        while ((index - 1)/2 >= 0 && comparator.compare(heap.get(index), heap.get((index - 1)/2)) < 0){
            swap(index, (index - 1)/2);
            index = (index - 1)/2;
        }
    }

    private void swap(int i, int j){
        T o1 = heap.get(i);
        T o2 = heap.get(j);
        heap.set(i, o2);
        heap.set(j, o1);
        indexMap.put(o1, j);
        indexMap.put(o2, i);
    }

    public T pop(){
        T res = heap.get(0);
        int end = size - 1;
        swap(0, end);
        heap.remove(end);
        indexMap.remove(res);
        heapify(0, --size);
        return res;
    }

    // 从index处heapify
    private void heapify(int index, int size) {
        int left = 2 * index + 1;
        while (left < size){
            int greater = left + 1 < size &&
                    (comparator.compare(heap.get(left), heap.get(left + 1)) < 0) ?
                    left : left + 1;
            greater = comparator.compare(heap.get(greater), heap.get(index)) < 0 ?
                    greater : index;
            if (greater == index)
                break;
            swap(greater, index);
            index = greater;
            left = 2 * index + 1;
        }
    }

    // 调用resign的时候是：已经手动将堆上的某个元素的值改为val了 比如：heap.set(某个index, val);
    // 所以此时堆的规律已经被打破了，因此再调用resign来重新让堆变有序，并且是O(logN)的复杂度.
    public void resign(T val){
        int valIndex= indexMap.get(val);
        // 重新修改的值，不是向上调整heapInsert，就是向下调整heapify，两者必中其一.
        heapInsert(valIndex);
        heapify(valIndex, size);
    }
}
