package Graph;

import java.util.ArrayList;

public class Node {
    public int val;
    public int in;   // 入度
    public int out;   // 出度
    public ArrayList<Node> neighbors;   // 该顶点出发能到的直接邻居
    public ArrayList<Edge> edges;       // 该顶点出发的边

    public Node(int val) {
        this.val = val;
        this.in = 0;
        this.out = 0;
        this.neighbors = new ArrayList<>();
        this.edges = new ArrayList<>();
    }
}
