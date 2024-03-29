package Basic.Graph;

import utils.GraphEdge;
import utils.Graph;
import utils.GraphNode;

import java.util.*;

public class Kruskal {

    // Kruskal 算法生成图的最小生成树。用并查集最合适
    public static class UnionFind{
        public HashMap<GraphNode, GraphNode> rootMap;  // key: 某个结点  value: 结点所在集合的根结点
        public HashMap<GraphNode, Integer> sizes;   // key: 一个集合的根结点   value：该集合的元素个数

        public UnionFind(){
            rootMap = new HashMap<>();
            sizes = new HashMap<>();
        }

        // 从一个图中建立初始信息：此时每个结点的父结点就是自己，并且以自己为根结点的集合size为1
        public void fromGraph(Graph graph){
            rootMap.clear();
            sizes.clear();
            for (GraphNode node : graph.nodes.values()){
                rootMap.put(node, node);
                sizes.put(node, 1);
            }
        }

        public GraphNode findRoot(GraphNode node){
            Stack<GraphNode> stack = new Stack<>();
            // 只有各个集合的根结点才会是：自己 == 自己的父结点
            while (node != rootMap.get(node)){
                stack.push(node);
                node = rootMap.get(node);
            }
            // 当跳出上面的循环后， node就指向了当前集合的根结点
            while (!stack.isEmpty())
                rootMap.put(stack.pop(), node);
            return node;
        }

        public boolean isSameSet(GraphNode a, GraphNode b){
            return findRoot(a) == findRoot(b);
        }

        public void union(GraphNode a, GraphNode b){
            if (a == null || b == null)
                return;
            if (!isSameSet(a, b)){
                GraphNode aRoot = findRoot(a);
                GraphNode bRoot = findRoot(b);
                int aSize = sizes.get(aRoot);
                int bSize = sizes.get(bRoot);
                GraphNode big = aSize >= bSize ? aRoot : bRoot;
                GraphNode small = big == aRoot ? bRoot : aRoot;
                rootMap.put(small, big);
                sizes.put(big, aSize + bSize);
                sizes.remove(small);
            }
        }
    }

    public static Set<GraphEdge> kruskalMST(Graph graph){
        UnionFind unionFind = new UnionFind();
        unionFind.fromGraph(graph);
        // 堆 传入一个新的比较器：实现按照边的权重从小到大排序
        PriorityQueue<GraphEdge> minHeap = new PriorityQueue<>((o1, o2) -> o1.weight - o2.weight);
        // 将边逐个加入到小根堆，会自动排序，将最小的边放到堆顶
        minHeap.addAll(graph.edges);
        HashSet<GraphEdge> res = new HashSet<>();
        while (!minHeap.isEmpty()){
            GraphEdge cur = minHeap.poll();
            if (!unionFind.isSameSet(cur.from, cur.to)){
                res.add(cur);
                unionFind.union(cur.from, cur.to);
            }
        }
        return res;
    }
}
