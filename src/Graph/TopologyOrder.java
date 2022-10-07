package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 实战：用别人题目给出的图的结构来做拓扑排序. 并且该方法是在不知道出入度的情况下，利用点次的方法实现拓扑排序
public class TopologyOrder {

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
        public int nums;

        public Record(DirectedGraphNode node, int nums) {
            this.node = node;
            this.nums = nums;
        }
    }

    // 该方法只传入了一个图中的所有结点及其直接邻居，不像我们自己的图结构还有入度，所以要想别的办法来解决。
    // 在一个有向无环图中，一个结点能到达的所有子图的结点数量再加上自己本身得到的数值可以称为该结点的点次，
    // 如果结点x的点次 > 结点y的点次，那么在拓扑序中x在y之前。eg：
    //
    //    4 -> 6 -> 8       4的点次为5  6的点次为2   8的点次为1   3的点次为2
    //    |->  3  __↑
    //
    // 计算点次的事其实就是在做缓存，从4计算其点次，再计算6的点次的时候，其实计算4的时候就遍历过了
    // 有了缓存就能省很多计算。所以定义一个Record类记录一个结点的点次

    public static List<DirectedGraphNode> topology(ArrayList<DirectedGraphNode> graph){
        HashMap<DirectedGraphNode, Record> cache = new HashMap<>();
        // 建立缓存表
        for (DirectedGraphNode cur : graph)
            f(cur, cache);
        // 将缓存中所有的Record都单独拉出来放在一个列表中
        ArrayList<Record> records = new ArrayList<>(cache.values());
        // 按照每个点的点次从大到小排序
        records.sort(((o1, o2) -> o2.nums - o1.nums));
        ArrayList<DirectedGraphNode> res = new ArrayList<>();
        for (Record r : records)
            res.add(r.node);
        return res;
    }

    // 该方法表示来到当前结点cur，计算并返回cur的点次
    private static Record f(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> cache){
        if (cache.containsKey(cur))
            return cache.get(cur);
        int nums = 0;
        for (DirectedGraphNode neighbor : cur.neighbors)
            nums += f(neighbor, cache).nums;

        Record res = new Record(cur, nums + 1); // + 1 表示加上自己那个
        cache.put(cur, res);
        return res;
    }
}
