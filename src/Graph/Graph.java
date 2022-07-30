package Graph;

import java.util.*;

// 图的表达形式有很多种比如邻接表、邻接矩阵，还有一种形式是一个 N*3 的二维矩阵：每一行3个元素表示：边的权重、出发点、终点
// createGraph 方法基于第三种形式转化为我们自己定义的图结构

public class Graph {
    public HashMap<Integer, Node> nodes;
    public HashSet<Edge> edges;

    public Graph() {
        this.nodes = new HashMap<>();
        this.edges = new HashSet<>();
    }

    public Graph createGraph(int[][] matrix){
        Graph graph = new Graph();
        for (int i = 0; i < matrix.length; i++) {
            int weight = matrix[i][0], from = matrix[i][1], to = matrix[i][2];
            if (!graph.nodes.containsKey(from))
                graph.nodes.put(from, new Node(from));
            if (!graph.nodes.containsKey(matrix[i][2]))
                graph.nodes.put(to, new Node(to));
            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);
            fromNode.out++;
            toNode.in++;
            fromNode.neighbors.add(toNode);
            Edge edge = new Edge(weight, fromNode, toNode);
            fromNode.edges.add(edge);
            graph.edges.add(edge);
        }
        return graph;
    }

    // 从一个结点出发进行宽度优先遍历
    public static void BFS(Node node){
        if (node == null)
            return;
        Queue<Node> queue = new LinkedList<>();
        HashSet<Node> set = new HashSet<>();
        queue.add(node);
        set.add(node);
        while (!queue.isEmpty()){
            Node cur = queue.poll();
            System.out.print(cur.val + " ");
            for (Node neighbor : cur.neighbors){
                if (!set.contains(neighbor)) {
                    set.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }

    // 从一个结点出发进行深度优先遍历
    public static void DFS(Node node){
        if (node == null)
            return;
        Stack<Node> stack = new Stack<Node>();
        HashSet<Node> set = new HashSet<Node>();
        set.add(node);
        stack.push(node);
        System.out.print(node.val + " ");
        while (!stack.isEmpty()){
            Node cur = stack.pop();
            for (Node neighbor : cur.neighbors){
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
    public static List<Node> Topology(Graph graph){
        // key: 一个结点   value：该结点剩余的入度
        HashMap<Node, Integer> map = new HashMap<Node, Integer>();
        // 入度为0的结点才能进该队列
        Queue<Node> queue = new LinkedList<>();
        for (Node node : graph.nodes.values()){
            map.put(node, node.in);
            if (node.in == 0)
                queue.add(node);
        }
        // 跳出for循环后，map中就记录了所有结点的入度，并且队列中存放了所有入度为0的结点
        List<Node> res = new ArrayList<>();
        while (!queue.isEmpty()){
            Node cur = queue.poll();
            res.add(cur);
            // 将所有以当前结点为出发点的结点的入度 -1。
            for (Node node : cur.neighbors){
                map.put(node, map.get(node) - 1);
                // 如果减完之后为0就加入队列
                if (map.get(node) == 0)
                    queue.add(node);
            }
        }
        return res;
    }
}
