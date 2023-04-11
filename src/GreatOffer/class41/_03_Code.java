package GreatOffer.class41;

// 来自网易互娱
// 给定一个矩阵roads,roads[i]这个数组代表的是i号城市到哪几个城市有路，走一条路的时间为1。
// 给定一个传送门矩阵gates，每一个城市都有一个传送门，gates[i]表示i号城市能直接到哪几个城市，
// 且不花费时间，但是接下来一分钟不能走传送门，只能走路。
// 有N个城市，编号为0～N-1，现在处于0号结点，最终要到达N-1号结点，求最小的到达时间.
// 保证所有输入均有效，不存在环等情况

import java.util.PriorityQueue;

public class _03_Code {

    public static int minTime(int[][] roads, int[][] gates, int N) {
        // distance[0][i] -> 0 : 前一个城市到达i，是走路的方式, 最小代价，distance[0][i]
        // distance[1][i] -> 1 : 前一个城市到达i，是传送的方式, 最小代价，distance[1][i]
        // distance[0][0] == 0   distance[1][0] == 0
        int[][] distance = new int[2][N];
        // 除了起始点以外，先把其余点都当作不可达
        for (int i = 1; i < N; i++) {
            distance[0][i] = Integer.MAX_VALUE;
            distance[1][i] = Integer.MAX_VALUE;
        }
        // 当前结点是否访问过  我们把城市和上一个城市到当前城市的方式封装成一个Node
        boolean[][] visited = new boolean[2][N];
        // 小根堆  仅根据两个结点的代价排序
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> a.cost - b.cost);
        // 为什么一开始认为上一个城市到0号城市的方式是走路？
        // 因为起始点0既可以选择走路去下一个，也可以选择走门去下一个。而走路到达下一个城市后可以选择两种方式，
        // 如果是走门到下一个城市，那么下一个城市只能走路。所以上一个点到起始点的方式类似于走路。
        heap.add(new Node(0, 0, 0));
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            // 如果当前的(方式, 城市)已经访问过了，那就说明以某种方式到达该城市的最小代价已经结算过了，
            // 没必要重复结算
            if (visited[cur.way][cur.city])
                continue;
            // 说明没结算过
            visited[cur.way][cur.city] = true;
            // 枚举走路的方式去下一个城市
            for (int next : roads[cur.city]) {
                if (distance[0][next] > cur.cost + 1) {
                    distance[0][next] = cur.cost + 1;
                    // 如果堆里是：(2, 0, 14)  (3, 0, 16)  那么假设此时生成的新的结点是(3, 0, 12)
                    // 按理说应该把堆上的(3, 0, 16) 修改成 (3, 0, 12) 然后动态调整结构，这用到的是加强
                    // 堆的功能。但是这里，我们可以直接将(3, 0, 12) 加入堆，形成
                    // (3, 0, 12)  (2, 0, 14)  (3, 0, 16)   因为这个新的结点必然是先弹出栈的，会发现
                    // 该结点没被访问过，于是将其设置为true表示访问过，当(3, 0, 16)弹出时会发现已经被访问过了，
                    // 该结点会被直接跳过
                    heap.add(new Node(next, 0, cur.cost + 1));
                }
            }

            // 枚举传送的方式  只有前一个城市到当前城市是走路的方式，当前城市才有可能选择传送门去下一个城市
            if (cur.way == 0) {
                for (int next : gates[cur.city]) {
                    if (distance[1][next] > cur.cost) {
                        distance[1][next] = cur.cost;
                        heap.add(new Node(next, 1, cur.cost));
                    }
                }
            }
        }
        return Math.min(distance[0][N - 1], distance[1][N - 1]);
    }


    public static class Node {
        public int city;  // 到达的当前城市编号
        public int way;   // 从上一个城市到达当前城市的方式，0表示是走路来的  1表示是从传送门来的
        public int cost;  // 到达当前城市的花费

        public Node(int city, int way, int cost) {
            this.city = city;
            this.way = way;
            this.cost = cost;
        }
    }
}
