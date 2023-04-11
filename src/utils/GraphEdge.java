package utils;

public class GraphEdge {
    public int weight;  // 边的权重
    public GraphNode from;
    public GraphNode to;

    public GraphEdge(int weight, GraphNode from, GraphNode to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }
}
