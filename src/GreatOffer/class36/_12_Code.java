package GreatOffer.class36;

import java.util.ArrayList;
import java.util.HashMap;

// 来自三七互娱
// leetCode原题第815题
// 给你一个数组 routes，表示一系列公交线路，其中每个routes[i]表示一条公交线路，第 i 辆公交车将会在上面循环行驶。
// 例如，路线 routes[0] = [1, 5, 7] 表示第 0 辆公交车会一直按序列 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ...
// 这样的车站路线行驶。现在从 source 车站出发（初始时不在公交车上），要前往 target 车站。 期间仅可乘坐公交车。
// 求出最少乘坐的公交车数量。如果不可能到达终点车站，返回 -1 。

public class _12_Code {

    // 这样的公交线路给人的感觉就像是下面的图一样：
    //
    //           ●---●   ●---●        每个环都是一条公交线路，每条线路站点数量可能不同，公共点就是可以换乘
    //          /     \ /    |        的站点。我们需要这样的信息：每个车站属于哪几条线路。当给定了起点站时，
    //     ●---●-------●     ●        我们把起点站所在线路遍历一遍，找出所有站点隶属于哪些线路，并把未曾浏
    //    /    |        \   /         览过的线路添加进来，逐步循环下去，其实就是按照线路进行宽度优先遍历。
    //   ●-----●          ●
    //

    public int numBusesToDestination(int[][] routes, int source, int target) {
        if (source == target)
            return 0;
        int N = routes.length;
        // 记录每个站点隶属于哪些线路
        HashMap<Integer, ArrayList<Integer>> dp = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < routes[i].length; j++) {
                if (!dp.containsKey(routes[i][j]))
                    dp.put(routes[i][j], new ArrayList<>());
                dp.get(routes[i][j]).add(i);
            }
        }

        int[] queue = new int[N];
        int h = 0, t = 0;  // 队列的头和尾
        boolean[] set = new boolean[N];  // 记录哪些线路已经被访问过了
        // 把起点站隶属的所有线路当做第一层
        for (int route : dp.get(source)) {
            queue[t++] = route;
            set[route] = true;
        }
        int lines = 1;  // 坐了几条公交线路
        while (t != h) { // 队列不为空
            int size = t - h; // 一次梳理一层，这是之前BFS层次遍历时经常使用的技巧
            // 此时queue里装的都是上一层的线路，遍历这些线路时会引出新的线路作为下一层，把所有的下一层线路存放在
            // nextLevel里
            for (int i = 0; i < size; i++) {
                int curRoute = queue[h++];
                for (int station : routes[curRoute]) {
                    if (station == target)
                        return lines;
                    if (dp.containsKey(station)) {
                        for (int newRoute : dp.get(station)) {
                            if (!set[newRoute]) {
                                set[newRoute] = true;
                                queue[t++] = newRoute;
                            }
                        }
                        dp.remove(station);  // 已经分析过的车站就可以删除了
                    }
                }
            }
            lines++;
        }
        return -1;  // 如果队列都空了，都没到达，说明不可达
    }
}
