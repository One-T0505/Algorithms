package Basic.Graph;

import utils.GraphEdge;
import utils.Graph;
import utils.GraphNode;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Prim {
    public static Set<GraphEdge> primMST(Graph graph, GraphNode source) {
        // 解锁的边进入小根堆
        PriorityQueue<GraphEdge> priorityQueue = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(GraphEdge o1, GraphEdge o2) {
                return o1.weight - o2.weight;
            }
        });

        // 这两个set分别用于记录哪些边和点已经处理过了，避免重复处理
        HashSet<GraphNode> nodeSet = new HashSet<>();
        HashSet<GraphEdge> edgeSet = new HashSet<>(source.edges);

        Set<GraphEdge> res = new HashSet<>(); // 存放选中的边在resSet里

        // source 是开始点
        nodeSet.add(source);
        // 由一个点，解锁所有相连的边
        priorityQueue.addAll(source.edges);
        while (!priorityQueue.isEmpty()) {
            GraphEdge cur = priorityQueue.poll(); // 弹出解锁的边中，最小的边
            GraphNode toNode = cur.to; // 可能的一个新的点
            if (!nodeSet.contains(toNode)) { // 不含有的时候，就是新的点
                nodeSet.add(toNode);
                res.add(cur);
                for (GraphEdge edge : toNode.edges){
                    if (!edgeSet.contains(edge)){
                        edgeSet.add(edge);
                        priorityQueue.add(edge);
                    }
                }
            }
        }
        return res;
    }

    // 请保证graph是连通图
    // graph[i][j]表示点i到点j的距离，如果是系统最大值代表无路
    // 返回值是最小连通图的路径之和
    public static int prim(int[][] graph) {
        int size = graph.length;
        int[] distances = new int[size];
        boolean[] visit = new boolean[size];
        visit[0] = true;
        System.arraycopy(graph[0], 0, distances, 0, size);
        int sum = 0;
        for (int i = 1; i < size; i++) {
            int minPath = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] < minPath) {
                    minPath = distances[j];
                    minIndex = j;
                }
            }
            if (minIndex == -1) {
                return sum;
            }
            visit[minIndex] = true;
            sum += minPath;
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] > graph[minIndex][j]) {
                    distances[j] = graph[minIndex][j];
                }
            }
        }
        return sum;
    }
}
