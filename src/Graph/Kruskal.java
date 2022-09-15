package Graph;

import java.util.*;

public class Kruskal {
    // Kruskal 算法生成图的最小生成树。用并查集最合适
    public static class UnionFind{
        public HashMap<Node, Node> fatherMap;  // key: 某个结点  value: 结点往上的结点
        public HashMap<Node, Integer> sizes;   // key: 一个集合的根结点   value：该集合的元素个数

        public UnionFind(){
            fatherMap = new HashMap<>();
            sizes = new HashMap<>();
        }

        // 从一个图中建立初始信息：此时每个结点的父结点就是自己，并且以自己为根结点的集合size为1
        public void fromGraph(Graph graph){
            fatherMap.clear();
            sizes.clear();
            for (Node node : graph.nodes.values()){
                fatherMap.put(node, node);
                sizes.put(node, 1);
            }
        }

        public Node findRoot(Node node){
            Stack<Node> stack = new Stack<>();
            // 只有各个集合的根结点才会是：自己 == 自己的父结点
            while (node != fatherMap.get(node)){
                stack.push(node);
                node = fatherMap.get(node);
            }
            // 当跳出上面的循环后， node就指向了当前集合的根结点
            while (!stack.isEmpty())
                fatherMap.put(stack.pop(), node);
            return node;
        }

        public boolean isSameSet(Node a, Node b){
            return findRoot(a) == findRoot(b);
        }

        public void union(Node a, Node b){
            if (a == null || b == null)
                return;
            if (isSameSet(a, b)){
                Node aroot = findRoot(a);
                Node broot = findRoot(b);
                int aSize = sizes.get(aroot);
                int bSize = sizes.get(broot);
                Node big = aSize >= bSize ? aroot : broot;
                Node small = big == aroot ? broot : aroot;
                fatherMap.put(small, big);
                sizes.put(big, aSize + bSize);
                sizes.remove(small);
            }
        }
    }

    public static Set<Edge> kruskalMST(Graph graph){
        UnionFind unionFind = new UnionFind();
        unionFind.fromGraph(graph);
        // 堆 传入一个新的比较器：实现按照边的权重从小到大排序
        PriorityQueue<Edge> minHeap = new PriorityQueue<Edge>(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o2.weight;
            }
        });
        // 将边逐个加入到小根堆，会自动排序，将最小的边放到堆顶
        minHeap.addAll(graph.edges);
        HashSet<Edge> res = new HashSet<>();
        while (!minHeap.isEmpty()){
            Edge cur = minHeap.poll();
            if (!unionFind.isSameSet(cur.from, cur.to)){
                res.add(cur);
                unionFind.union(cur.from, cur.to);
            }
        }
        return res;
    }

}
