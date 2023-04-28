package Basic.UnionFind.Exercise;

// 存在一个无向图，图中有 n 个节点。其中每个节点都有一个介于 0 到 n - 1 之间的唯一编号。给你一个二维数组 graph，
// 其中 graph[u] 是一个节点数组，由节点 u 的邻接节点组成。形式上，对于 graph[u] 中的每个 v，都存在一条位于节点
// u 和节点 v 之间的无向边。该无向图同时具有以下属性：
//  1.不存在自环（graph[u] 不包含 u）。
//  2.不存在平行边（graph[u] 不包含重复值）。
//  3.如果 v 在 graph[u] 内，那么 u 也应该在 graph[v] 内（该图是无向图）
//  4.这个图可能不是连通图，也就是说两个节点 u 和 v 之间可能不存在一条连通彼此的路径。
// 二分图定义：如果能将一个图的节点集合分割成两个独立的子集 A 和 B ，并使图中的每一条边的两个节点一个来自 A 集合，
// 一个来自 B 集合，就将这个图称为二分图。如果图是二分图，返回 true；否则，返回 false。

import java.util.LinkedList;
import java.util.Queue;

public class _0785_Code {

    // 对于图中的任意两个节点 u 和 v，如果它们之间有一条边直接相连，那么 u 和 v 必须属于不同的集合。
    // 如果给定的无向图连通，那么我们就可以任选一个节点开始，给它染成红色。随后我们对整个图进行遍历，
    // 将该节点直接相连的所有节点染成绿色，表示这些节点不能与起始节点属于同一个集合。我们再将这些绿色节
    // 点直接相连的所有节点染成红色，以此类推，直到无向图中的每个节点均被染色。
    // 如果我们能够成功染色，那么红色和绿色的节点各属于一个集合，这个无向图就是一个二分图；如果我们未能
    // 成功染色，即在染色的过程中，某一时刻访问到了一个已经染色的节点，并且它的颜色与我们将要给它染上的颜色不相同，
    // 也就说明这个无向图不是一个二分图。

    // 有两种解法：DFS 和 BFS
    // 默认 0 表示无色   1 表示红色   2 表示绿色
    public static boolean isBipartite(int[][] graph) {
        int N = graph.length;
        int[] color = new int[N];  // 默认初始值刚好为0，表示全部都还没有被染过色
        // 这里为什么要套个for循环呢？就是为了避免给的图是非连通图，里面包含几个连通分量的时候
        // 通过for就能所有遍历到，如果不套循环，只能遍历一个连通分量
        for (int i = 0; i < N; i++) {
            if (color[i] == 0){ // 当前没被上过色
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                color[i] = 1; // 先把当前结点染成红色
                while (!queue.isEmpty()){
                    int node = queue.poll();
                    // 表示当前结点的直接相连结点应该涂的颜色
                    int expect = color[node] == 1 ? 2 : 1;
                    for (int neighbor : graph[node]){
                        if (color[neighbor] == 0){ // 如果结点没被染过色，就染上，并入队
                            queue.add(neighbor);
                            color[neighbor] = expect;
                        } else if (color[neighbor] != expect) {
                            return false;
                        }
                        // 为什么neighbor == expect 没有任何操作呢，也不需要让其进队吗？
                        // 如果neighbor被染成了expect色，那必然也是从无色染成的，那么那次从无色变成expect的时候
                        // 该结点就入过队了，所以这里不需要入队了。
                    }
                }
            }
        }
        return true;
    }




    // 方法2  DFS
    int[] color;
    boolean valid;


    public boolean isBipartite2(int[][] graph) {
        int N = graph.length;
        color = new int[N];
        valid = true;
        for (int i = 0; i < N; i++) {
            if (color[i] == 0){
                dfs(i, 1, graph);
            }
        }
        return valid;
    }


    // i号结点被染成了c颜色
    private void dfs(int i, int c, int[][] graph) {
        color[i] = c;
        int expect = c == 1 ? 2 : 1;
        for (int neighbor : graph[i]){
            if (color[neighbor] == 0){
                dfs(neighbor, expect, graph);
                if (!valid)
                    return;
            } else if (color[neighbor] != expect){
                valid = false;
                return;
            }
        }
    }
}
