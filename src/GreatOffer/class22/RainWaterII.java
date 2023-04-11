package GreatOffer.class22;

import java.util.PriorityQueue;

// 接雨水问题二  leetCode407
// 给你一个 m x n 的矩阵，其中的值均为非负整数，代表二维高度图每个单元的高度，请计算图中形状最多能接多少体积的雨水。

public class RainWaterII {


    // 我们需要用到一个小根堆，并且入堆的元素类型是Node型，将矩阵每个元素的值以及对应的行和列封装成Node，先把
    // 矩阵最外围一圈的结点入堆。然后每次从堆里弹出最小的元素，并且让其四周的元素入堆，每次入堆时结算答案。

    public int trapRainWater(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 ||
                heightMap[0] == null || heightMap[0].length == 0 ||
                heightMap.length * heightMap[0].length <= 4)
            return 0;
        int N = heightMap.length;
        int M = heightMap[0].length;
        // 用一个等大的矩阵表示对应的元素有没有进过堆
        boolean[][] isEntered = new boolean[N][M];
        PriorityQueue<Node> heap = new PriorityQueue<>();
        // 把矩阵最外围一圈的数值放入堆中
        for (int j = 0; j < M - 1; j++) {
            isEntered[0][j] = true;
            heap.add(new Node(heightMap[0][j], 0, j));
        }
        for (int i = 0; i < N - 1; i++) {
            isEntered[i][M - 1] = true;
            heap.add(new Node(heightMap[i][M - 1], i, M - 1));
        }
        for (int j = M - 1; j >= 1; j--) {
            isEntered[N - 1][j] = true;
            heap.add(new Node(heightMap[N - 1][j], N - 1, j));
        }
        for (int i = N - 1; i >= 0; i--) {
            isEntered[i][0] = true;
            heap.add(new Node(heightMap[i][0], i, 0));
        }

        // max变量很关键，他记录的是瓶颈，每次弹出一个结点时看他能否推高max
        int max = 0;
        int res = 0;
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            max = Math.max(max, cur.val);
            int i = cur.row;
            int j = cur.col;
            // 说明当前结点有上方结点且没进过堆
            if (i > 0 && !isEntered[i - 1][j]) {
                heap.add(new Node(heightMap[i - 1][j], i - 1, j));
                res += Math.max(max - heightMap[i - 1][j], 0);
                isEntered[i - 1][j] = true;
            }
            // 说明当前结点有下方结点且没进过堆
            if (i < N - 1 && !isEntered[i + 1][j]) {
                heap.add(new Node(heightMap[i + 1][j], i + 1, j));
                res += Math.max(max - heightMap[i + 1][j], 0);
                isEntered[i + 1][j] = true;
            }
            // 说明当前结点有左方结点且没进过堆
            if (j > 0 && !isEntered[i][j - 1]) {
                heap.add(new Node(heightMap[i][j - 1], i, j - 1));
                res += Math.max(max - heightMap[i][j - 1], 0);
                isEntered[i][j - 1] = true;
            }
            // 说明当前结点有右方结点且没进过堆
            if (j < M - 1 && !isEntered[i][j + 1]) {
                heap.add(new Node(heightMap[i][j + 1], i, j + 1));
                res += Math.max(max - heightMap[i][j + 1], 0);
                isEntered[i][j + 1] = true;
            }
        }
        return res;
    }

    public static class Node {
        public int val;
        public int row;
        public int col;

        public Node(int val, int row, int col) {
            this.val = val;
            this.row = row;
            this.col = col;
        }
    }
}
