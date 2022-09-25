package Graph;

import java.util.HashMap;

public class DynamicHeap {
    private Node[] heap;  // 这个就是实现堆序的数组

    // 记录当前结点在heap的索引位置，如果为-1，说明该结点，曾经入过堆，并且已经弹出了，也就是说该结点锁住了，
    // 源结点到该结点的距离不再更新了。在图中，可能会出现回溯的边指向已经不需要再更新的结点了。
    private HashMap<Node, Integer> index;
    private HashMap<Node, Integer> distance;
    private int size;       // 表示堆上有多少个元素

    public DynamicHeap(int size) {
        heap = new Node[size];
        index = new HashMap<>();
        distance = new HashMap<>();
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    // 表示从源点到cur新发现了一个距离dis，来判断cur和dis要如何处理
    public void addOrUpdateOrIgnore(Node cur, int dis) {
        // 如果该结点进过堆，并且此刻就在堆上
        if (isInHeap(cur) && dis < distance.get(cur)){
            distance.put(cur, dis);
            upHeapify(cur, index.get(cur));
        }
        // 如果根本没进过堆
        if (!isEntered(cur)){
            heap[size] = cur;
            index.put(cur, size);
            distance.put(cur, dis);
            upHeapify(cur, size++);
        }
        // 如果进过堆，但是此时已经不在堆上了，就什么都不做
    }


    public nodeRecord pop() {
        nodeRecord record = new nodeRecord(heap[0], distance.get(heap[0]));
        swap(0, size - 1);
        index.put(heap[size - 1], -1);
        distance.remove(heap[size - 1]);
        heap[size - 1] = null;  // 指向为空就表示释放了
        downHeapify(0, --size);
        return record;
    }

    // 不管是-1还是正常数组的索引，都表示进来过
    private boolean isEntered(Node node){
        return index.containsKey(node);
    }

    // 表示进来过，但此刻是否还在堆上
    private boolean isInHeap(Node node){
        return isEntered(node) && index.get(node) != -1;
    }

    private void upHeapify(Node src, int pos) {
        while ((pos - 1) / 2 >= 0 && distance.get(heap[pos]) < distance.get(heap[(pos - 1) / 2])){
            swap(pos, (pos - 1) / 2);
            pos = (pos - 1) >> 1;
        }
    }

    private void downHeapify(int start, int size) {
        int left = start << 1 | 1;
        while (left < size){
            int lesser = left + 1 < size && distance.get(heap[left + 1]) < distance.get(heap[left]) ?
                    left + 1 : left;
            lesser = distance.get(heap[lesser]) < distance.get(heap[start]) ? lesser : start;
            if (lesser == start)
                break;
            swap(lesser, start);
            start = lesser;
            left = start << 1 | 1;
        }
    }

    private void swap(int i, int j) {
        index.put(heap[i], j);
        index.put(heap[j], i);
        Node tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    static class nodeRecord {
        public Node node;
        public int distance;  // 表示源点到node的距离

        public nodeRecord(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
