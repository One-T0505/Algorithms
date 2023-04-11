package GreatOffer.class18;

// leetCode934  连接岛问题
// 给你一个大小为 n x n 的二元矩阵 grid ，其中 1 表示陆地，0 表示水域。
// 岛是由四面相连的 1 形成的一个最大组，即不会与非组内的任何其他 1 相连。grid 中 恰好存在两座岛。
// 你可以将任意数量的 0 变为 1 ，以使两座岛连接起来，变成 一座岛。
// 返回必须翻转的 0 的最小数目。

public class ShortestBridge {

    // 思路：
    //  1.先把两片岛找出来
    //  2.利用宽度优先遍历BFS，来做广播。首先从一片岛开始，找到距离岛1的所有位置，再从这些位置继续往外扩散。
    //    然后再从另一片岛往外扩散。这样矩阵中每个位置都可以记录出距离两个岛的距离，找出那个两者之和最小的几个点
    //    作为桥。


    public int shortestBridge(int[][] grid) {
        int N = grid.length;
        int M = grid[0].length;
        int all = N * M;
        int island = 0;
        // curs 和 nexts 就是两个用数组实现的队列。二维矩阵中每个位置其实都可以用一维坐标表示，即：i * M + j
        // curs 里面存放的就是一维地址，这样就可以用数组来存储，如果非要用 (行号，列号) 来表示，那我们还得封装
        // 成一个类，再用系统提供的队列来存储，比较繁琐，不够清爽。
        int[] curs = new int[all];
        int[] nexts = new int[all];
        // records 就是记录每个位置的两个信息。即：分别距离两个岛的距离。这里也是用一维坐标表示
        // records[0] 这个一维数组，记录的就是矩阵中每个位置到第一个岛的距离
        // records[1] 这个一维数组，记录的就是矩阵中每个位置到第二个岛的距离
        int[][] records = new int[2][all];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                // 发现岛屿了，就让他去感染这一片岛屿
                if (grid[i][j] == 1){
                    // 该方法会把这一片岛的值改为2，并且将这片岛的所有点存放入 curs 队列中，并且返回当前
                    // 队列的指针在哪，同样就说明了队列元素数量
                    int queueSize = infect(grid, i, j, N, M, curs, 0, records[island]);
                    // V 表示距离；代表每一层的结点应该填写的距离岛屿的距离
                    int V = 1;
                    while (queueSize != 0){
                        V++;
                        // 将 curs 中每个点的上下左右且可用的点都添加到了 nexts 队列中，并且在 record 中
                        // 更新好了距离，并且返回了 nexts 队列的元素数量。
                        queueSize = bfs(N, M, V, curs, queueSize, nexts, records[island]);
                        // 交换两个队列，空间复用
                        int[] tmp = curs;
                        curs = nexts;
                        nexts = tmp;
                    }
                    // 干扰过一次之后，就要把island++，因为下次碰到1的时候，就是第二片岛了，同样做一遍相同的扩散
                    island++;
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < all; i++) {
            min = Math.min(min, records[0][i] + records[1][i]);
        }
        // 为啥-3，因为我设置的岛屿到自己的距离为1，往后推下去，实际的值就是要-3，没有为什么。
        //
        //  1 * *            比如这两个岛，左上的1距离自己的距离为1，右边的就是2，再右边的就是3；
        //      1            右下的岛到自己距离为1，上面的就是2，上左就是3； 取最小值 (2+3, 3+2)==5
        //                   但实际上只需要两个点即可，所以 5要-3
        // 这是因为我自己的设置而需要最后修正的方式，如果你的设置和我不一样，那最后修正的方式也可能和我不一样
        return min - 3;
    }



    // 当前来到m[i][j] , 总行数是N，总列数是M
    // m[i][j]感染出去(找到这一片岛所有的1),把每一个1的坐标，放入到int[] curs队列！
    // 1 (a,b) -> curs[p++] = (a * M + b)
    // 1 (c,d) -> curs[p++] = (c * M + d)
    // 二维已经变成一维了， 1 (a,b) -> a * M + b
    // 我们默认岛上的点到自己这片岛的距离为1，这样往后推。当然，你也可以认为这些点到自己这片岛的距离为0，无所谓。
    // 设置距离record[a * M +b ] = 1
    // 并且把 curs 这个队列的指针位置最后返回，告诉别人用数组实现的队列填写到哪个位置了
    private int infect(int[][] m, int i, int j, int N, int M, int[] curs, int p, int[] record) {
        if (i < 0 || i >= N || j < 0 || j >= M || m[i][j] != 1)
            return p;
        m[i][j] = 2;
        int pos = i * M + j;
        record[pos] = 1;
        curs[p++] = pos;
        p = infect(m, i - 1, j, N, M, curs, p, record);
        p = infect(m, i + 1, j, N, M, curs, p, record);
        p = infect(m, i, j - 1, N, M, curs, p, record);
        p = infect(m, i, j + 1, N, M, curs, p, record);
        
        return p;
    }



    // curP  curs 这个队列的指针
    // V-1 是 curs 中每个点距离岛的距离  V 是由 curs 中的每个点向外广播一层的所有节点 应该填写的距离
    private int bfs(int N, int M, int V, int[] curs, int curP, int[] nexts, int[] record) {
        int nextP = 0; // nexts 这个队列的指针
        for (int i = 0; i < curP; i++) {
            // 记住了，curs 存放的都是一维坐标，当我们拿到一个一维坐标表示的点的时候，我们如何去推算他的上下左右
            // 位置的一维坐标呢？
            // 上位置：如果当前位置 < M 说明该点就在第0行，所以没上
            int up = curs[i] < M ? -1 : curs[i] - M;
            // curs[i] + M 就表示其下方的位置，如果 >= all 了，那就说明本身就在最后一行了，无下
            int down = curs[i] + M >= N * M ? -1 : curs[i] + M;
            // curs[i]  % M == 0  说明当前点就在第0列，无左
            int left = curs[i]  % M == 0 ? -1 : curs[i] - 1;
            // curs[i]  % M == M - 1  说明当前点就在最后一列，无右
            int right = curs[i]  % M == M - 1 ? -1 : curs[i] + 1;

            // curs 里面存放的是一批数，里面的每个数都要去寻找他的上下左右结点，所以必然会有重复的时候，如何判断这个
            // 结点确实没被计算过呢？那就在record对应位置去找，如果为0，就说明没算过
            // 所以，当从一个岛屿扩散到另一个岛屿的1时，也不会计算的。
            if (up != -1 && record[up] == 0){
                record[up] = V;
                nexts[nextP++] = up;
            }
            if (down != -1 && record[down] == 0){
                record[down] = V;
                nexts[nextP++] = down;
            }
            if (left != -1 && record[left] == 0){
                record[left] = V;
                nexts[nextP++] = left;
            }
            if (right != -1 && record[right] == 0){
                record[right] = V;
                nexts[nextP++] = right;
            }
        }
        return nextP;
    }
}
