package utils;

import java.util.*;
import java.util.LinkedList;

// 图的表达形式有很多种比如邻接表、邻接矩阵，还有一种形式是一个 N*3 的二维矩阵：每一行3个元素表示：边的权重、出发点、终点
// createGraph 方法基于第三种形式转化为我们自己定义的图结构

public class Graph {
    public HashMap<Integer, GraphNode> nodes;
    public HashSet<GraphEdge> edges;

    public Graph() {
        this.nodes = new HashMap<>();
        this.edges = new HashSet<>();
    }

    // 假如给了一张A[N][3]的矩阵，每一行表示一条边的信息，每一行中：
    // A[i][0] 表示边的权重； A[i][1] 表示起始点；  A[i][2] 表示终点；
    // 如果是这样的形式，就可以用如下的方法将矩阵信息转化为图
    public Graph createGraph(int[][] matrix){
        Graph graph = new Graph();
        for (int i = 0; i < matrix.length; i++) {
            int weight = matrix[i][0], from = matrix[i][1], to = matrix[i][2];
            if (!graph.nodes.containsKey(from))
                graph.nodes.put(from, new GraphNode(from));
            if (!graph.nodes.containsKey(matrix[i][2]))
                graph.nodes.put(to, new GraphNode(to));
            GraphNode fromNode = graph.nodes.get(from);
            GraphNode toNode = graph.nodes.get(to);
            fromNode.out++;
            toNode.in++;
            fromNode.neighbors.add(toNode);
            GraphEdge edge = new GraphEdge(weight, fromNode, toNode);
            fromNode.edges.add(edge);
            graph.edges.add(edge);
        }
        return graph;
    }

    // 从一个结点出发进行宽度优先遍历
    public static void BFS(GraphNode node){
        if (node == null)
            return;
        Queue<GraphNode> queue = new java.util.LinkedList<>();
        HashSet<GraphNode> set = new HashSet<>();
        queue.add(node);
        set.add(node);
        while (!queue.isEmpty()){
            GraphNode cur = queue.poll();
            System.out.print(cur.val + " ");
            for (GraphNode neighbor : cur.neighbors){
                if (!set.contains(neighbor)) {
                    set.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }


    // 从一个结点出发进行深度优先遍历。栈中存放的结点顺序就代表已经探明可以走的深度轨迹，所以每次进栈时做相应
    // 操作，就是DFS序。
    public static void DFS(GraphNode node){
        if (node == null)
            return;
        Stack<GraphNode> stack = new Stack<GraphNode>();
        HashSet<GraphNode> set = new HashSet<GraphNode>();
        set.add(node);
        stack.push(node);
        System.out.print(node.val + " ");
        while (!stack.isEmpty()){
            GraphNode cur = stack.pop();
            for (GraphNode neighbor : cur.neighbors){
                if (!set.contains(neighbor)){
                    stack.push(cur);
                    stack.push(neighbor);
                    System.out.print(neighbor.val + " ");
                    break;
                }
            }
        }
    }

    // 拓扑排序算法：只适用于有向无环图
    public static List<GraphNode> Topology(Graph graph){
        // key: 一个结点   value：该结点剩余的入度
        HashMap<GraphNode, Integer> map = new HashMap<>();
        // 入度为0的结点才能进该队列
        Queue<GraphNode> queue = new LinkedList<>();
        for (GraphNode node : graph.nodes.values()){
            map.put(node, node.in);
            if (node.in == 0)
                queue.add(node);
        }
        // 跳出for循环后，map中就记录了所有结点的入度，并且队列中存放了所有入度为0的结点
        List<GraphNode> res = new ArrayList<>();
        while (!queue.isEmpty()){
            GraphNode cur = queue.poll();
            res.add(cur);
            // 将所有以当前结点为出发点的结点的入度 -1。
            for (GraphNode node : cur.neighbors){
                map.put(node, map.get(node) - 1);
                // 如果减完之后为0就加入队列
                if (map.get(node) == 0)
                    queue.add(node);
            }
        }
        return res;
    }
}
