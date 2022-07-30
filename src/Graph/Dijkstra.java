package Graph;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Dijkstra {
    // 从 source 到图中其他各点的最小距离
    public static HashMap<Node, Integer> dijkstra(Node source){
        // key：从source出发到达key结点  value：从source出发到达key结点的最小距离
        // 如果在hashmap中没有T这个结点的记录，说明source不可到达T
        HashMap<Node, Integer> distance = new HashMap<Node, Integer>();
        distance.put(source, 0);  // 自己到自己的距离为0
        // 当用某个点来计算最小距离更新表之后，该结点的记录就不要再修改了 lock存放哪些结点的记录不用再修改
        HashSet<Node> lock = new HashSet<Node>();
        Node minDis = selectNode(distance, lock);
        while (minDis != null){
            int dis = distance.get(minDis);
            for (Edge edge : minDis.edges){
                Node to = edge.to;
                if (!distance.containsKey(to))
                    distance.put(to, dis + edge.weight);
                else
                    distance.put(to, Math.min(distance.get(to), dis + edge.weight));
            }
            lock.add(minDis);
            minDis = selectNode(distance, lock);
        }
        return distance;
    }

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
}
