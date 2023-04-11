package GreatOffer.class10;

import java.util.*;

// 在实时数据流中找到最常使用的k个单词.实现TopK类中的三个方法:
// TopK(k), 构造方法
// add(word), 增加一个新单词
// topk(), 得到当前最常使用的k个单词.

public class TopKFrequentWordsII {

    // 这个题目就要用到加强堆来实现，维持一个大小为K的小根堆，为什么要用小根堆？因为替换的时候，是以前topK个中出现次数
    // 最小的那个作为替换门槛的。


    public static class Node {
        public String word;
        public int times;  // 出现的次数

        public Node(String w, int t) {
            word = w;
            times = t;
        }
    }

    public static class HeapComp implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return o1.times != o2.times ? (o1.times - o2.times) : (o2.word.compareTo(o1.word));
        }

    }

    public static class TopK {
        private Node[] heap;   // 这个堆只维持k个容量
        private int size;  // 记录堆上有多少个数据

        private HeapComp comp;
        private HashMap<String, Node> timeMap;     // 字符串封装成node，就是词频表，维持所有记录
        private HashMap<Node, Integer> indexMap;   // 结点在堆上具体某个位置, V为-1时表示该结点没上堆，维持所有记录

        // 这个有序表也只会记录k个结点，不会维持所有记录，就是为了最后输出topK个结果时方便点
        private TreeSet<Node> treeSet;

        public TopK(int k) {
            heap = new Node[k];
            size = 0;
            comp = new HeapComp();
            timeMap = new HashMap<>();
            indexMap = new HashMap<>();
            // tressSet按递减排序, 词频越大的越靠前
            treeSet = new TreeSet<>(((o1, o2) -> o1.times != o2.times ?
                    (o2.times - o1.times) : (o1.word.compareTo(o2.word))));
        }


        public void add(String word) {
            if (heap.length == 0)  // 如果要求是找到前0个出现次数多的情况，就属于非法情况
                return;
            Node cur = null;
            int preIndex = -1;
            if (!timeMap.containsKey(word)) {  // 该词是第一次来
                cur = new Node(word, 1);
                timeMap.put(word, cur);
                indexMap.put(cur, -1);
            } else {  // 已经不是第一次了
                cur = timeMap.get(word);
                // 要在times++之前，先在treeSet中删掉
                // 原因是因为一但times++，cur在treeSet中的排序就失效了,这种失效会导致整棵treeSet的排序出问题
                // 系统提供的treeSet不能动态调整排序
                if (treeSet.contains(cur))
                    treeSet.remove(cur);   // 下面会把该结点再加上的，因为此时如果已经在树上，那times++后更应该在树上
                cur.times++;
                preIndex = indexMap.get(cur);
            }

            // 说明当前word是第一次来，中了上面的if分支，所以preIndex没变;
            // 也有可能是已经来的结点，但是没能上堆
            if (preIndex == -1) {
                if (size == heap.length) {  // 此时堆已满
                    if (comp.compare(heap[0], cur) < 0) {  // 当前结点击败了堆中最小结点，那就要替换
                        treeSet.remove(heap[0]);
                        treeSet.add(cur);
                        indexMap.put(heap[0], -1);    // 下堆
                        indexMap.put(cur, 0);    // 放在堆的0号位
                        heap[0] = cur;
                        downHeapify(0, size);
                    }
                } else {  // 当前word是第一次来，并且堆没满
                    treeSet.add(cur);
                    indexMap.put(cur, size);
                    heap[size] = cur;
                    upHeapify(size++);
                }
            } else {  // preIndex != -1，说明该word对应的结点是在堆上的
                treeSet.add(cur);
                downHeapify(preIndex, size);
            }
        }


        public List<String> topk() {
            ArrayList<String> res = new ArrayList<>();
            for (Node node : treeSet)
                res.add(node.word);
            return res;
        }


        // 从cur位置向上调整堆
        private void upHeapify(int cur) {
            while (cur != 0) {
                int parent = (cur - 1) >> 1;
                if (comp.compare(heap[cur], heap[parent]) < 0) {
                    swap(parent, cur);
                    cur = parent;
                } else {
                    break;
                }
            }
        }


        // 从cur这个位置向下调整堆，size是结尾位置
        private void downHeapify(int cur, int size) {
            int left = cur << 1 | 1;
            int right = (cur << 1) + 2;
            int less = cur;
            while (left < size) {
                if (comp.compare(heap[left], heap[cur]) < 0)
                    less = left;
                if (right < size && comp.compare(heap[right], heap[left]) < 0)
                    less = right;
                if (less != cur)
                    swap(less, cur);
                else
                    break;
                cur = less;
                left = (cur << 1) | 1;
                right = (cur << 1) + 2;
            }
        }


        private void swap(int pos1, int pos2) {
            indexMap.put(heap[pos1], pos2);
            indexMap.put(heap[pos2], pos1);
            Node tmp = heap[pos1];
            heap[pos1] = heap[pos2];
            heap[pos2] = tmp;
        }
    }
    // ------------------------------------------------------------------------------------------



    public static void main(String[] args) {

        TopK topK = new TopK(2);
        topK.add("a");
        topK.add("b");
        topK.add("c");
        topK.add("a");
        topK.add("c");
        List<String> res = topK.topk();
        for (int i = 0; i < 2; i++)
            System.out.println(res.get(i));
    }
}
