package utils;

import java.util.ArrayList;

public class GraphNode {
    public int val;
    public int in;   // 入度
    public int out;   // 出度
    public ArrayList<GraphNode> neighbors;   // 该顶点出发能到的直接邻居
    public ArrayList<GraphEdge> edges;       // 该顶点出发的边

    public GraphNode(int val) {
        this.val = val;
        this.in = 0;
        this.out = 0;
        this.neighbors = new ArrayList<>();
        this.edges = new ArrayList<>();
    }
}
