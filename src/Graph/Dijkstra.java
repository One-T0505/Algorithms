package Graph;

import java.security.KeyStore;
import java.util.*;

// 从 source 到图中其他各点的最小距离
public class Dijkstra {
    public static HashMap<Node, Integer> dijkstraV1(Node src){
        // key：从source出发到达key结点  value：从source出发到达key结点的最小距离
        // 如果在hashmap中没有T这个结点的记录，说明source不可到达T
        HashMap<Node, Integer> distance = new HashMap<>();
        distance.put(src, 0);  // 自己到自己的距离为0
        // 当用某个点来计算最小距离更新表之后，该结点的记录就不要再修改了 lock存放哪些结点的记录不用再修改
        HashSet<Node> lock = new HashSet<>();
        Node minDis = selectNode(distance, lock);
        while (minDis != null){
            int dis = distance.get(minDis);
            for (Edge edge : minDis.edges){
                Node to = edge.to;
                if (!distance.containsKey(to)) // 该结点若还不在distance中，说明从原点到该点的距离还为无穷
                    distance.put(to, dis + edge.weight);
                else
                    distance.put(to, Math.min(distance.get(to), dis + edge.weight));
            }
            lock.add(minDis);
            minDis = selectNode(distance, lock);
        }
        return distance;
    }


    // 下面的方法是用来从distance中选择一个最小距离的点，并且该结点没有被锁定（不在lock中）
    // 该方法每次都是遍历distance这个hashmap来得到的，是否可以用堆来优化？是可以优化的。
    private static Node selectNode(HashMap<Node, Integer> distance, HashSet<Node> lock) {
        Node res = null;
        int minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distance.entrySet()){
            if (!lock.contains(entry.getKey()) && entry.getValue() < minDistance){
                res = entry.getKey();
                minDistance = entry.getValue();
            }
        }
        return res;
    }

    // 初版方法在每次循环时，都是遍历hashmap中的元素找到符合要求的结点，这样比较繁琐。如果是用堆来实现，每次直接
    // 从堆顶就可以弹出目标结点，而不需要遍历，这样就提高了速度。
    // 但是还存在一个问题：就是如果按照堆排好序了，在循环过程中有可能会改变已经排好序的结点的值，此时会打破原来的
    // 排序，但是系统提供的堆是不支持改变后自动更新排序的，所以我们需要手动写堆。
    public static HashMap<Node, Integer> dijkstraV2(Node src, int size){
        DynamicHeap heap = new DynamicHeap(size);
        heap.addOrUpdateOrIgnore(src, 0);
        HashMap<Node, Integer> res = new HashMap<>();
        while (!heap.isEmpty()){
            DynamicHeap.nodeRecord record = heap.pop();
            Node cur = record.node;
            int dis = record.distance;
            for (Edge edge : cur.edges)
                heap.addOrUpdateOrIgnore(edge.to, edge.weight + dis);
            res.put(cur, dis);
        }
        return res;
    }
}
