package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 用别人题目给出的图的结构来做拓扑排序. 并且该方法是在不知道出入度的情况下，利用深度的方法实现拓扑排序
// 建议先去看TopologyOrder的实现
public class TopologyDeepth {

    static class DirectedGraphNode {
        public int label;  // 相当于点的编号
        public ArrayList<DirectedGraphNode> neighbors;  // 有向图的直接邻居

        public DirectedGraphNode(int label) {
            this.label = label;
            neighbors = new ArrayList<>();
        }
    }

    static class Record {
        public DirectedGraphNode node;
        public int deepth;

        public Record(DirectedGraphNode node, int deepth) {
            this.node = node;
            this.deepth = deepth;
        }
    }

    public static List<DirectedGraphNode> topology(ArrayList<DirectedGraphNode> graph){
        HashMap<DirectedGraphNode, Record> cache = new HashMap<>();
        // 制作缓存
        for (DirectedGraphNode cur : graph)
            f(cur, cache);
        ArrayList<Record> records = new ArrayList<>(cache.values());
        // 按深度由大到小排序
        records.sort(((o1, o2) -> o2.deepth - o1.deepth));

        ArrayList<DirectedGraphNode> res = new ArrayList<>();
        for (Record r : records)
            res.add(r.node);
        return res;
    }

    private static Record f(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> cache) {
        if (cache.containsKey(cur))
            return cache.get(cur);
        int deepth = 0;
        for (DirectedGraphNode node : cur.neighbors)
            deepth = Math.max(deepth, f(node, cache).deepth);

        Record res = new Record(cur, deepth + 1);
        cache.put(cur, res);
        return res;
    }
}
