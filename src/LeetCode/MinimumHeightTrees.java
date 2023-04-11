package LeetCode;

import java.util.*;

/**
 * ymy
 * 2023/3/26 - 18 : 49
 **/

// leetCode310
// 树是一个无向图，其中任何两个顶点只通过一条路径连接。换句话说，一个任何没有简单环路的连通图都是一棵树。
// 给你一棵包含 n 个节点的树，标记为 0 到 n - 1 。给定数字 n 和一个有 n - 1 条无向边的 edges 列表
//（每一个边都是一对标签），其中 edges[i] = [ai, bi] 表示树中节点 ai 和 bi 之间存在一条无向边。
// 可选择树中任何一个节点作为根。当选择节点 x 作为根节点时，设结果树的高度为 h 。在所有可能的树中，具有
// 最小高度的树（即，min(h)）被称为最小高度树。
// 请你找到所有的最小高度树并按任意顺序返回它们的根节点标签列表。
// 树的高度是指根节点和叶子节点之间最长向下路径上边的数量。


// 1 <= n <= 2 * 10^4
// edges.length == n - 1
// 0 <= ai, bi < n
// ai != bi
// 所有 (ai, bi) 互不相同
// 给定的输入保证是一棵树，并且不会有重复的边


public class MinimumHeightTrees {

    // 建议去官方看一下给的实例，这样比较好理解。
    // 下面的方法初代版本是已经完成算法了，但是超时了，所以想着优化。
    // 官方题解里给的证明方法太过数学了。

    public static List<Integer> findMinHeightTrees(int n, int[][] edges) {
        List<Integer> res = new ArrayList<>();
        if (n == 1){
            res.add(0);
            return res;
        }
        // 希望把edges变成好用的数据结构。把它修改成邻接矩阵表。直接让 m[i][j] == 1 表示两个结点之间有边
        HashMap<Integer, List<Integer>> dp = new HashMap<>();
        for (int[] e : edges){
            if (!dp.containsKey(e[0]))
                dp.put(e[0], new ArrayList<>());
            dp.get(e[0]).add(e[1]);
            if (!dp.containsKey(e[1]))
                dp.put(e[1], new ArrayList<>());
            dp.get(e[1]).add(e[0]);
        }
        // 记录哪些点已经被访问过了
        HashSet<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        int min = n; // 因为给了n-1条边，所以最差情况就是n-1
        for (int i = 0; i < n; i++) {
            queue.add(i);
            visited.add(i);
            int h = 0;
            while (!queue.isEmpty()){
                int layer = queue.size();
                for (int j = 0; j < layer; j++) {
                    int poll = queue.poll();
                    for (int child : dp.get(poll)){
                        if (!visited.contains(child)){
                            queue.add(child);
                            visited.add(child);
                        }
                    }
                }
                h++;
            }
            if (h < min){
                min = h;
                res.clear();
                res.add(i);
            } else if (h == min) {
                res.add(i);
            }

            // 换一个根结点的时候，都要把 visited 清空
            visited.clear();
        }

        return res;
    }


    public static void main(String[] args) {
        int n = 6;
        int[][] edges = {{1, 0}, {0, 2}, {0, 3}, {3, 4}, {4, 5}};
        System.out.println(findMinHeightTrees(n, edges));
    }

}
