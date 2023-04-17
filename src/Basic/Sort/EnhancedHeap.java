package Basic.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// 增强堆
// 系统提供的堆有一个问题，就是在堆上排好序的元素如果值发生变动了，系统堆是没办法更新堆排序的。
// 堆实际上就是数组实现的，它能天然知道哪个位置存了什么东西，但是缺少反向索引，不知道哪个东西存在哪个地方，只能通过遍历的方式找到，
// 所以代价比较高。于是，就有了增强堆，增强堆需要我们自己手写，能以很低的代价满足数值动态变化的情况

public class EnhancedHeap<K> {
    private final ArrayList<K> heap;
    private final HashMap<K, Integer> indexMap;  // 这个就是反向表，记录某个元素存放在堆上的位置
    private int heapSize;
    private final Comparator<? super K> comparator;

    public EnhancedHeap(Comparator<? super K> comparator) {
        heap = new ArrayList<>();
        indexMap = new HashMap<>();
        heapSize = 0;
        this.comparator = comparator;
    }

    public boolean isEmpty(){
        return heapSize == 0;
    }

    public int size(){
        return heapSize;
    }

    public boolean contains(K key){
        return indexMap.containsKey(key);
    }

    public K peek(){
        return heap.get(0);
    }

    public List<K> getAllElements(){
        ArrayList<K> res = new ArrayList<>();
        for (int i = 0; i < heapSize; i++) {
            res.add(heap.get(i));
        }
        return res;
    }

    public void push(K key){
        heap.add(key);
        // K 一定要是非基础类型，否则直接按值传递，那么put就会覆盖。如果一定要基础类型，那就在基础类型外再包一层类
        indexMap.put(key, heapSize);
        upHeapify(heapSize++);
    }

    public K pop(){
        K res = heap.get(0);
        swap(0, heapSize - 1);
        indexMap.remove(res);
        heap.remove(--heapSize);
        downHeapify(0);
        return res;
    }


    // 调用该方法时，说明该key已经变化了
    public void resign(K key){
        // 不管怎么变化，不是在堆上需要向上移动，就是需要向下移动，所以下面两个方法必中一个
        upHeapify(indexMap.get(key));
        downHeapify(indexMap.get(key));
    }

    // 在堆上删除一个元素，还要不能破坏整个堆。如果要用系统的PriorityQueue来做，那光是找到这个要删除的元素时间复杂度就来到了O(N).
    // 如果用增强堆，时间复杂度为O(logN)
    public void remove(K key) {
        if (indexMap.containsKey(key)){
            K replace = heap.get(heapSize - 1);  // 找到堆上最后一个元素
            Integer pos = indexMap.get(key);     // 找到要删除元素的位置
            indexMap.remove(key);           // 删除要删除元素的反向索引
            heap.remove(--heapSize);  // 将最后一个元素移除
            if (key != replace){ // 如果要删除的不是最后一个元素
                heap.set(pos, replace);
                indexMap.put(replace, pos);
                resign(replace);
            }
        }
    }

    private void downHeapify(int index) {
        int left = (index << 1) | 1;
        while (left < heapSize){
            int greater = left + 1 < heapSize && comparator.compare(heap.get(left), heap.get(left + 1)) < 0 ?
                    left + 1 : left;
            greater = comparator.compare(heap.get(index), heap.get(left)) < 0 ? index : greater;
            if (greater == index)
                break;
            swap(index, greater);
            index = greater;
            left = (index << 1) | 1;
        }
    }

    private void upHeapify(int index) {
        while ((index - 1) >> 1 >= 0 && comparator.compare(heap.get(index), heap.get((index - 1) >> 1)) < 0){
            swap(index, (index - 1) >> 1);
            index = (index - 1) >> 1;
        }
    }

    private void swap(int i, int j){
        K o1 = heap.get(i);
        K o2 = heap.get(j);
        heap.set(i, o2);
        heap.set(j, o1);
        indexMap.put(o1, j);
        indexMap.put(o2, i);
    }
}

