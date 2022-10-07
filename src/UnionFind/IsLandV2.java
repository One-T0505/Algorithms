package UnionFind;

// 岛问题的进化版：这次给的矩阵matrix全部为0，但是会提供一组位置[i, j]。该位置矩阵positions形状为N*2, 每一行表示一个位置，
// 每一个位置就代表让matrix[i, j] = 1，所以matrix是逐步增加1的个数的。
// 请问：每次增加1，当前matrix中岛的个数为多少。如果positions有N行，那结果就应该返回N个。

import java.util.ArrayList;
import java.util.List;

public class IsLandV2 {

    static class UnionFind {
        public int[] roots;

        // 这个问题中，sizes有了很关键的作用。比如两个位置i，j的集合要合并，如果i胜出，那么sizes[i]该增加还增加；此时sizes[j]
        // 不要抹0，让它留着，只要其值不为0，就说明之前这里有过岛
        public int[] sizes;
        public int[] help;
        public int rows;
        public int cols;
        public int isLand;

        public UnionFind(int m, int n) {
            rows = m;
            cols = n;
            roots = new int[rows * cols];
            sizes = new int[rows * cols];
            help = new int[rows * cols];
            isLand = 0;
        }

        public int connect(int row, int col) {
            int pos = row * cols + col;
            if (sizes[pos] == 0){  // 说明这个位置是第一次空降下来的
                roots[pos] = pos;
                sizes[pos] = 1;
                isLand++;
                // 和自己四周的检查下是否能合并
                union(row - 1, col, row, col);  // 上
                union(row + 1, col, row, col);  // 下
                union(row, col - 1, row, col);  // 左
                union(row, col + 1, row, col);  // 右
            }
            return isLand;
        }

        private void union(int row1, int col1, int row2, int col2) {
            if (row1 < 0 || row1 == rows || row2 < 0 || row2 == rows ||
                    col1 < 0 || col1 == cols || col2 < 0 || col2 == cols)
                return;

            int pos1 = row1 * cols + col1;
            int pos2 = row2 * cols + col2;
            // 只要有一个还没初始化过，就不用做了
            if (sizes[pos1] == 0 || sizes[pos2] == 0)
                return;
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

    // 主方法
    public static List<Integer> isLandV2(int m, int n, int[][] positions){
        if (positions == null)
            return null;
        UnionFind unionFind = new UnionFind(m, n);
        List<Integer> res = new ArrayList<>();
        for (int[] position : positions)
            res.add(unionFind.connect(position[0], position[1]));

        return res;
    }
}
