package Basic.UnionFind.Exercise;

// leetCode695 岛问题相关拓展
// 给你一个大小为 m x n 的二进制矩阵 grid 。
// 岛屿是由一些相邻的 1 (代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在水平或者竖直的四个方向上相邻。
// 你可以假设 grid 的四个边缘都被 0（代表水）包围着。岛屿的面积是岛上值为 1 的单元格的数目。
// 计算并返回 grid 中最大的岛屿面积。如果没有岛屿，则返回面积为 0 。

public class IslandIII {

    public int maxAreaOfIsland(int[][] grid) {
        if (grid == null)
            return 0;
        UnionFind uf = new UnionFind(grid);
        for (int j = 1; j < uf.C; j++) {
            if (grid[0][j] == 1 && grid[0][j - 1] == 1)
                uf.union(0, j - 1, 0, j);
        }
        // 该for循环处理第一列
        for (int i = 1; i < uf.R; i++) {
            if (grid[i][0] == 1 && grid[i - 1][0] == 1)
                uf.union( i, 0, i - 1, 0);
        }
        // 该for循环处理剩余所有既有左又有上的元素
        for (int i = 1; i < uf.R; i++) {
            for (int j = 1; j < uf.C; j++) {
                if (grid[i][j] == 1 && grid[i - 1][j] == 1)  // 上
                    uf.union(i, j, i - 1, j);
                if (grid[i][j] == 1 && grid[i][j - 1] == 1)  // 左
                    uf.union(i, j, i, j - 1);
            }
        }
        return uf.max;
    }


    static class UnionFind {
        public int R;
        public int C;
        public int[] roots;
        public int[] sizes;
        public int[] help;
        public int max;

        public UnionFind(int[][] grid){
            R = grid.length;
            C = grid[0].length;
            roots = new int[R * C];
            sizes = new int[R * C];
            help = new int[R * C];
            max = 0;
            for(int i = 0; i < R; i++){
                for(int j = 0; j < C; j++){
                    if(grid[i][j] == 1){
                        int pos = i * C + j;
                        roots[pos] = pos;
                        sizes[pos] = 1;
                        max = Math.max(max, sizes[pos]);
                    }
                }
            }
        }


        public void union(int r1, int c1, int r2, int c2){
            int p1 = r1 * C + c1;
            int p2 = r2 * C + c2;
            int root1 = findRoot(p1);
            int root2 = findRoot(p2);
            if(root1 != root2){
                if(sizes[root1] <= sizes[root2]){
                    sizes[root1] += sizes[root2];
                    roots[root2] = root1;
                    max = Math.max(max, sizes[root1]);
                } else {
                    sizes[root2] += sizes[root1];
                    roots[root1] = root2;
                    max = Math.max(max, sizes[root2]);
                }
            }
        }


        private int findRoot(int pos){
            int i = 0;
            while(roots[pos] != pos){
                help[i++] = pos;
                pos = roots[pos];
            }
            while(i > 0){
                roots[help[--i]] = pos;
            }
            return pos;
        }
    }
}
