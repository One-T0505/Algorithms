package UnionFind;

// leetCode200
// 岛问题
// 给定一个二维数组matrix，里面的值不是1就是0, 上、下、左、右相邻的1认为是一片岛, 返回matrix中岛的数量
public class IsLand {

    // 方法1：用最暴力的递归方法做
    public static int isLandsV1(char[][] matrix){
        if (matrix == null)
            return 0;
        int isLand = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == '1'){
                    isLand++;
                    infect(matrix, i, j);
                }
            }
        }
        return isLand;
    }

    private static void infect(char[][] matrix, int i, int j) {
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length || matrix[i][j] != '1')
            return;
        matrix[i][j] = '2';    // 将已经访问过的地方改为2，否则下面递归就会无穷尽
        infect(matrix, i - 1, j);  // 上
        infect(matrix, i + 1, j);  // 下
        infect(matrix, i, j - 1);  // 左
        infect(matrix, i, j + 1);  // 右
    }


    // 方法2：用并查集的方法，首先根据题目定制一个UnionFind结构
    static class UnionFind {
        public int[] roots;
        public int[] sizes;
        public int[] help;

        public int rows;
        public int cols;
        public int isLand;

        public UnionFind(char[][] matrix) {
            rows = matrix.length;
            cols = matrix[0].length;
            isLand = 0;
            // rows * cols 有可能会超出int型的范围，但是几乎不会
            roots = new int[rows * cols];
            sizes = new int[rows * cols];
            help = new int[rows * cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (matrix[i][j] == '1'){
                        int pos = i * cols + j;
                        roots[pos] = pos;  // 一开始，每一个'1'的根结点都是自己
                        sizes[pos] = 1;   // 一开始，每一个'1'所在的集合都只有自己一个结点
                        isLand++;
                    }
                }
            }
        }


        public void union(int row1, int col1, int row2, int col2) {
            int pos1 = row1 * cols + col1;
            int pos2 = row2 * cols + col2;
            int root1 = findRoot(pos1);
            int root2 = findRoot(pos2);
            if (root1 != root2){
                if (sizes[root1] <= sizes[root2]){
                    sizes[root1] += sizes[root2];
                    roots[root2] = root1;
                } else {
                    sizes[root2] += sizes[root1];
                    roots[root1] = root2;
                }
                isLand--;
            }
        }

        private int findRoot(int pos) {
            int index = 0;
            while (pos != roots[pos]){
                help[index++] = pos;
                pos = roots[pos];
            }
            for (index -= 1; index >= 0; index--){
                roots[help[index]] = pos;
            }
            return pos;
        }
    }


    // 主方法：
    public static int isLandV2(char[][] grid){
        if (grid == null)
            return 0;
        UnionFind unionFind = new UnionFind(grid);
        // 对每个元素只需要考察左、上有没有'1'即可。只考察右、下得不到同样的效果。
        // 第一行的元素无上，第一列的元素无左，为了不把所有的事情都放在一个for循环里实现，此时可以拆成多个for循环，
        // 这样就少写了很多条件判断

        // matrix[0][0]元素，既无左也无上，所以不用考虑
        // 该for循环处理第一行
        for (int j = 1; j < unionFind.cols; j++) {
            if (grid[0][j] == '1' && grid[0][j - 1] == '1')
                unionFind.union(0, j - 1, 0, j);
        }
        // 该for循环处理第一列
        for (int i = 1; i < unionFind.rows; i++) {
            if (grid[i][0] == '1' && grid[i - 1][0] == '1')
                unionFind.union( i, 0, i - 1, 0);
        }
        // 该for循环处理剩余所有既有左又有上的元素
        for (int i = 1; i < unionFind.rows; i++) {
            for (int j = 1; j < unionFind.cols; j++) {
                if (grid[i][j] == '1' && grid[i - 1][j] == '1')  // 上
                    unionFind.union(i, j, i - 1, j);
                if (grid[i][j] == '1' && grid[i][j - 1] == '1')  // 左
                    unionFind.union(i, j, i, j - 1);
            }
        }
        return unionFind.isLand;
    }
}
