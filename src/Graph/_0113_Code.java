package Graph;

// 给你无向连通图中一个节点的引用，请你返回该图的深拷贝(克隆)。
// 图中的每个节点都包含它的值 val（int） 和其邻居的列表（list[Node]）。
// 简单起见，每个节点的值都和它的索引相同。例如，第一个节点值为 1（val = 1），第二个节点值为 2（val = 2），以此类推。
// 给定节点将始终是图中的第一个节点（值为 1）。你必须将给定节点的拷贝作为对克隆图的引用返回。

import java.util.*;

public class _0113_Code {


    // 方法1 利用BFS
    public Node cloneGraph(Node node) {
        if(node == null)
            return null;
        // 新老结点的映射  同时也记录着哪些结点访问过了，避免重复访问
        HashMap<Node, Node> map = new HashMap<>();
        Node src = new Node(node.val, new ArrayList<>());
        map.put(node, src);
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()){
            Node poll = queue.poll();
            for (Node neighbor : poll.neighbors){
                if (!map.containsKey(neighbor)){
                    queue.add(neighbor);
                    map.put(neighbor, new Node(neighbor.val, new ArrayList<>()));
                }
                map.get(poll).neighbors.add(map.get(neighbor));
            }
        }
        return map.get(node);
    }




    // 方法2  DFS
    // 记录已被访问过的结点，所以对应的新结点也不应该再访问
    HashMap<Node, Node> map = new HashMap<>();
    public Node cloneGraph2(Node node) {
        if(node == null)
            return null;
        if (map.containsKey(node))
            return map.get(node);
        Node clone = new Node(node.val, new ArrayList<>());
        map.put(node, clone);
        for (Node n : node.neighbors){
            clone.neighbors.add(cloneGraph2(n));
        }
        return clone;
    }

    private void dfs(Node from, Node to, HashMap<Node, Node> map) {
        if (map.containsKey(to))
            return;
        map.put(to, new Node(to.val, new ArrayList<>()));
        map.get(from).neighbors.add(map.get(to));
        for (Node n : to.neighbors)
            dfs(to, n, map);
    }


    class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, ArrayList<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
    }
}
