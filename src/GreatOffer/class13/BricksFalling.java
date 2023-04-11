package class13;

// leetCode803
// 有一个 m * n 的二元网格 grid ，其中 1 表示砖块，0 表示空白。砖块稳定(不会掉落）的前提是：
//  1.一块砖直接连接到网格的顶部，或者
//  2.至少有一块相邻(4个方向之一)砖块稳定不会掉落
// 给你一个数组 hits，这是需要依次消除砖块的位置。每当消除 hits[i] = (rowi, coli) 位置上的砖块时，
// 对应位置的砖块（若存在）会消失，然后其他的砖块可能因为这一消除操作而掉落。一旦砖块掉落，它会立即从网格
// grid 中消失（即，它不会落在其他稳定的砖块上。
// 返回一个数组 result ，其中 result[i] 表示第 i 次消除操作对应掉落的砖块数目，不包含消除自己的。
// 注意，消除可能指向是没有砖块的空白位置，如果发生这种情况，则没有砖块掉落。

public class BricksFalling {

    // 为了便于理解题目，你可以想象第0行上面是天花板，每一块砖只有上下左右4个方向有粘性，所以第0行的每块砖
    // 都有可能在下面连了一片砖。现在给一个hit数组，每个元素就是一个炮弹，元素信息就是瞄准的位置。看每个炮弹
    // 能打掉多少块砖，除了本身瞄准的位置不算。每个炮弹如果瞄准的位置没有砖，直接记0；如果有砖，则把对应位置的
    // 值改为2，按照hit给的顺序依次修改。计算结果应该逆向计算。从hit最后一个位置，如果对应位置有砖，则再把值
    // 修改回1，统计这个变化能增加多少块砖，就是这个炮弹打掉的数量。为什么要逆向？因为最后一个炮弹计算时，之前的
    // 炮弹影响已经造成了，所以要逆向。我们先遍历一遍数组，把所有炮弹瞄准的位置的值修改为2，表示造成了影响。
    // 然后对着这张修改完的表，从最后一个炮弹开始，将每个2修改回1，看这个变化能让天花板上的砖块增加多少，就是该炮弹
    // 能打落的砖块。
    // 这题需要用到并查集。


    // 主方法
    public static int[] hitBricks(int[][] grid, int[][] hits) {
        if (grid == null || grid.length == 0 || hits == null || hits.length == 0)
            return null;
        for (int[] hit : hits) {
            if (grid[hit[0]][hit[1]] == 1)
                grid[hit[0]][hit[1]] = 2;
        }
        // 要先把值修改成2之后，再去建并查集，然后逆序将2改回1，每次看会有多少影响
        UnionFind unionFind = new UnionFind(grid);
        int[] res = new int[hits.length];
        for (int i = hits.length - 1; i >= 0; i--) {
            if (grid[hits[i][0]][hits[i][1]] == 2)
                res[i] = unionFind.finger(hits[i][0], hits[i][1]);
        }
        return res;
    }


    // 封装的并查集结构
    public static class UnionFind {
        private int N;
        private int M;
        private int cellingAll; // 一共有多少块砖连到了天花板上
        private int[][] grid;   // 原始矩阵
        // cellingSet[i] = true; i 是头节点，所在的集合是天花板集合
        private boolean[] cellingSet; // 用一维数组标记原始网格中每个位置是否被连在了天花板上
        private int[] parent;    // 记录每个位置如果是天花板集合，则所在集合的根结点
        private int[] capacity;  // 每个天花板集合的元素数量，只有每个集合的根结点需要维护自己的集合元素数量
        private int[] stack;

        public UnionFind(int[][] m) {
            initSpace(m);
            initConnect();
        }

        private void initSpace(int[][] m) {
            N = m.length;
            M = m[0].length;
            grid = m;
            int all = N * M;
            cellingAll = 0;
            cellingSet = new boolean[all];
            parent = new int[all];
            capacity = new int[all];
            stack = new int[all];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (grid[i][j] == 1) {
                        int pos = i * M + j;  // 二维坐标转换为一维坐标
                        parent[pos] = pos;    // 一开始自己的根就是自己
                        capacity[pos] = 1;
                        if (i == 0) {  // 只有第0行有砖，才可能是天花板集合的根
                            cellingSet[pos] = true;
                            cellingAll++;
                        }
                    }
                }
            }
        }


        // initSpace方法只是为每个砖块单独做好信息了，但没有去连接
        private void initConnect() {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    union(i, j, i - 1, j); // 上
                    union(i, j, i + 1, j); // 下
                    union(i, j, i, j - 1); // 左
                    union(i, j, i, j + 1); // 右
                }
            }
        }


        // 合并两块砖块
        private void union(int r1, int c1, int r2, int c2) {
            if (isValid(r1, c1) && isValid(r2, c2)) {
                int root1 = find(r1, c1);
                int root2 = find(r2, c2);
                if (root1 != root2) {
                    int size1 = capacity[root1];
                    int size2 = capacity[root2];
                    boolean status1 = cellingSet[root1];
                    boolean status2 = cellingSet[root2];
                    int greater = size1 >= size2 ? root1 : root2;
                    int less = greater == root1 ? root2 : root1;
                    // 小集合挂在大集合上
                    parent[less] = greater;
                    capacity[greater] = size1 + size2;
                    // 两个都不是天花板集合，或者两个都是天花板，都不会改变跟天花板相关的变量
                    // 只有一个是，一个不是，才能有改变
                    // 假如两个都是天花板集合，那么在initSpace时就设置好了集合大小，集合是否为天花板，和天花板砖
                    // 块数量，必然已经将二者包含在内了，所以此时两个天花板集合合并，这些属性都不会有变化。
                    // 如果是两个非天花板集合，那么上面的代已经修改完成了。
                    if (status1 ^ status2) {
                        cellingSet[greater] = true;  // 只用设置大的，因为小的已经被合进来了
                        cellingAll += status1 ? size2 : size1; // 只用把那个非天花板集合的数量加进天花板砖块里
                    }
                }
            }
        }


        // 不仅位置要合法，并且所在位置要有砖块，不然没必要合并
        private boolean isValid(int r, int c) {
            return r >= 0 && r < N && c >= 0 && c < M && grid[r][c] == 1;
        }


        // 找到一个位置所在集合的根结点。是否回忆起来，之前用并查集查根结点时，会把沿途的结点，用
        // 栈记录下来，最后将他们的根结点一起修改。这样虽然实质上只找到了一个结点的根，但是所有沿途
        // 上的结点的根信息被一起修改了。
        private int find(int r, int c) {
            int pointer = 0;
            int pos = r * M + c;
            while (parent[pos] != pos) {
                stack[pointer++] = pos;
                pos = parent[pos];
            }
            while (pointer != 0)
                parent[stack[--pointer]] = pos;
            return pos;
        }


        // 将2修改回1，计算影响
        public int finger(int r, int c) {
            grid[r][c] = 1;
            int pos = r * M + c;
            // 先单独把这个变为1的结点建立好
            if (r == 0) {
                cellingSet[pos] = true;
                cellingAll++;
            }
            parent[pos] = pos;
            capacity[pos] = 1;
            int pre = cellingAll; // 因为不算自己这一块，所以先把自己那里位置变成砖块+1后再开始统计
            union(r, c, r - 1, c);
            union(r, c, r + 1, c);
            union(r, c, r, c - 1);
            union(r, c, r, c + 1);
            int now = cellingAll;
            if (r == 0)
                return now - pre;
            else
                return now == pre ? 0 : now - pre - 1;
        }
    }
    // ===============================================================================================
}
