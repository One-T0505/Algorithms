package Graph;

import java.util.*;

// 从 source 到图中其他各点的最小距离
public class Dijkstra {
    public static HashMap<Node, Integer> dijkstraV1(Node src){
        // key：从source出发到达key结点  value：从source出发到达key结点的最小距离
        // 如果在hashmap中没有T这个结点的记录，说明source不可到达T
        HashMap<Node, Integer> distance = new HashMap<>();
        distance.put(src, 0);  // 自己到自己的距离为0
        // 当用某个点来计算最小距离更新表之后，该结点的记录就不要再修改了 lock存放哪些结点的记录不用再修改
        HashSet<Node> lock = new HashSet<>();
        Node minDis = selectNode(distance, lock);
        while (minDis != null){
            int dis = distance.get(minDis);
            for (Edge edge : minDis.edges){
                Node to = edge.to;
                if (!distance.containsKey(to)) // 该结点若还不在distance中，说明从原点到该点的距离还为无穷
                    distance.put(to, dis + edge.weight);
                else
                    distance.put(to, Math.min(distance.get(to), dis + edge.weight));
            }
            lock.add(minDis);
            minDis = selectNode(distance, lock);
        }
        return distance;
    }


    // 下面的方法是用来从distance中选择一个最小距离的点，并且该结点没有被锁定（不在lock中）
    // 该方法每次都是遍历distance这个hashmap来得到的，是否可以用堆来优化？是可以优化的。
    private static Node selectNode(HashMap<Node, Integer> distance, HashSet<Node> lock) {
        Node res = null;
        int minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distance.entrySet()){
            if (!lock.contains(entry.getKey()) && entry.getValue() < minDistance){
                res = entry.getKey();
                minDistance = entry.getValue();
            }
        }
        return res;
    }

    // 初版方法在每次循环时，都是遍历hashmap中的元素找到符合要求的结点，这样比较繁琐。如果是用堆来实现，每次直接
    // 从堆顶就可以弹出目标结点，而不需要遍历，这样就提高了速度。
    // 但是还存在一个问题：就是如果按照堆排好序了，在循环过程中有可能会改变已经排好序的结点的值，此时会打破原来的
    // 排序，但是系统提供的堆是不支持改变后自动更新排序的，所以我们需要手动写堆。
    public static HashMap<Node, Integer> dijkstraV2(Node src, int size){
        DynamicHeap heap = new DynamicHeap(size);
        heap.addOrUpdateOrIgnore(src, 0);
        HashMap<Node, Integer> res = new HashMap<>();
        while (!heap.isEmpty()){
            DynamicHeap.nodeRecord record = heap.pop();
            Node cur = record.node;
            int dis = record.distance;
            for (Edge edge : cur.edges)
                heap.addOrUpdateOrIgnore(edge.to, edge.weight + dis);
            res.put(cur, dis);
        }
        return res;
    }

    public static class DynamicHeap {
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

}
