package Graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Prim {
    public static Set<Edge> primMST(Graph graph, Node source) {
        // 解锁的边进入小根堆
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                return o1.weight - o2.weight;
            }
        });

        // 哪些点被解锁出来了
        HashSet<Node> nodeSet = new HashSet<>();

        Set<Edge> resSet = new HashSet<>(); // 存放选中的边在resSet里

        // source 是开始点
        nodeSet.add(source);
        // 由一个点，解锁所有相连的边
        priorityQueue.addAll(source.edges);
        while (!priorityQueue.isEmpty()) {
            Edge edge = priorityQueue.poll(); // 弹出解锁的边中，最小的边
            Node toNode = edge.to; // 可能的一个新的点
            if (!nodeSet.contains(toNode)) { // 不含有的时候，就是新的点
                nodeSet.add(toNode);
                resSet.add(edge);
                priorityQueue.addAll(toNode.edges);  // 可能重复添加边，但是不影响结果
            }
        }
        return resSet;
    }

    // 请保证graph是连通图
    // graph[i][j]表示点i到点j的距离，如果是系统最大值代表无路
    // 返回值是最小连通图的路径之和
    public static int prim(int[][] graph) {
        int size = graph.length;
        int[] distances = new int[size];
        boolean[] visit = new boolean[size];
        visit[0] = true;
        for (int i = 0; i < size; i++) {
            distances[i] = graph[0][i];
        }
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
