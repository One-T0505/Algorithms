package GreatOffer.class35;

import java.util.PriorityQueue;

// 网易
// map[i][j] == 0，代表(i, j)是海洋，渡过的话代价是2
// map[i][j] == 1,代表(i,j)是陆地，渡过的话代价是1
// map[i][j] == 2,代表(i,j)是障碍，无法渡过
// 每一步上、下、左、右都能走，返回从左上角走到右下角最小代价是多少，如果无法到达返回-1


public class _04_Code {

    // 这道题不能使用动态规划或者递归，因为每个位置都可以上下左右走，说明可以走回头路。
    // 这道题要使用小根堆，里面的记录要这样填：(0, 1, 3)，表示当前来到了(0,1)位置，从起点到现在一共的代价是3
    // 每次从小根堆里弹出一条记录，从这里出发，所以小根堆一直记录着最经济的走路方式。

    public static int minCost(int[][] map) {
        int N = map.length;
        int M = map[0].length;
        if (map[0][0] == 2 || map[N - 1][M - 1] == 2)
            return -1;
        PriorityQueue<Node> heap = new PriorityQueue<>((a, b) -> (a.cost - b.cost));
        boolean[][] visited = new boolean[N][M];
        add(map, 0, 0, 0, heap, visited);
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            if (cur.row == N - 1 && cur.col == M - 1)
                return cur.cost;
            add(map, cur.row - 1, cur.col, cur.cost, heap, visited);
            add(map, cur.row + 1, cur.col, cur.cost, heap, visited);
            add(map, cur.row, cur.col - 1, cur.cost, heap, visited);
            add(map, cur.row, cur.col + 1, cur.cost, heap, visited);
        }
        return -1;
    }


    // 之前的代价是pre，当前来到了(i,j)位置，看当前位置是否能进入堆
    private static void add(int[][] m, int i, int j, int pre, PriorityQueue<Node> heap,
                            boolean[][] visited) {
        if (i >= 0 && i < m.length && j >= 0 && j < m[0].length && m[i][j] != 2 && !visited[i][j]) {
            heap.add(new Node(i, j, pre + (m[i][j] == 0 ? 2 : 1)));
            visited[i][j] = true;
        }
    }

    public static class Node {
        public int row;
        public int col;
        public int cost;

        public Node(int row, int col, int cost) {
            this.row = row;
            this.col = col;
            this.cost = cost;
        }
    }
}
