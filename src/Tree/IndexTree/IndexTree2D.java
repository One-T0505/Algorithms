package Tree.IndexTree;

// 将IndexTree改写成二维形式，就是说不是一个数组的累加和形式，而是一个矩阵。累加和数组的开始下标依然是[1,1]，
// 传统的二维累加和数组中，sum[3][7]表示的含义就是：从[1,1]～[3,7] 这个范围的累加和。而用IndexTree来实现
// 二维累加和也是运用了和之前同样的规律。
//
// sum[i][j]的管辖范围是多少？就是分别将行号i写成二进制并提取出最右侧的1代表的数值x，所以行号i管辖的行范围是：i-x+1～i；
// 同样的，列号j提取出最右侧1代表的数值y，那么列号j管辖的列范围是：j-y+1～j，所以 i-x+1～i 和 j-y+1～j 的每种组合就是
// 其管辖的范围。
//
// 修改copy[i][j]的值会影响哪些单元格？同样的规律，先对行号i，每次加上最右侧1代表的数值x，就能找到所有被影响的行；用同样的方法
// 找出j可能影响的列，那么每种组合就是被影响的单元格。
//
// 如何求一个任意范围[m,n]～[p,q]上的累加和？ 比如要求[2,3]～[4,6]范围上的累加和，如下图所示：
//     1  2  3  4  5  6  7
//   1 ●  ●  ●  ●  ●  ●  ●
//   2 ●  ●  ●  ●  ●  ●  ●                 sum([2,3],[4,6]) == sum([1,1],[4,6]) - sum([1,1],[4,2]) -
//   3 ●  ●  ●  ●  ●  ●  ●                                     sum([1,1],[1,6]) + sum([1,1],[1,2])
//   4 ●  ●  ●  ●  ●  ●  ●                 而求[1,1]～[i,j]范围上的累加和是有规律的
//   5 ●  ●  ●  ●  ●  ●  ●
//
// 当i不为0时，每次累加i位置上的数值，再让i -= i最右侧1代表的数值，这样就能找到所有需要用到的行号，
// 用同样的方法找出所有可能用到的列号，那么他们的每种组合上的累加值就是结果。

import utils.arrays;

public class IndexTree2D {
    public int rows;
    public int cols;
    public int[][] copy;
    public int[][] tree;

    public IndexTree2D(int[][] ori) {
        rows = ori.length + 1;
        cols = ori[0].length + 1;
        copy = new int[rows][cols];
        tree = new int[rows][cols];
        for (int row = 1; row < rows; row++)
            System.arraycopy(ori[row - 1], 0, copy[row], 1, cols - 1);
        for (int row = 1; row < rows; row++) {
            for (int col = 1; col < cols; col++) {
                int startRow = row - (row & (-row)) + 1;
                int startCol = col - (col & (-col)) + 1;
                for (int i = startRow; i <= row; i++) {
                    for (int j = startCol; j <= col; j++)
                        tree[row][col] += copy[i][j];
                }
            }
        }
    }

    public void add(int i, int j, int C){
        if (i < 1 || j < 1 || i >= rows || j >= cols)
            return;
        copy[i][j] += C;
        for (int row = i; row < rows; row += (row & (-row))) {
            for (int col = j; col < cols; col += (col & (-col)))
                tree[row][col] += C;
        }
    }

    // 返回[startR,startC]～[endR,endC]范围上的累加和
    public int sum(int startR, int startC, int endR, int endC){
        if (startR < 1 || startC < 1 || endR >= rows || endC >= cols || startR > endR || startC > endC)
            return Integer.MIN_VALUE;
        return preSum(endR, endC) - preSum(endR, startC - 1) - preSum(startR - 1, endC)
                + preSum(startR - 1, startC - 1);
    }


    // 返回[1,1]～[row,col]范围上的累加和
    private int preSum(int row, int col) {
        // 不用判断无效参数，因为上游调用时已经排查过了
        int res = 0;
        for (int i = row; i != 0; i -= (i & (-i))) {
            for (int j = col; j != 0; j -= (j & (-j)))
                res += tree[i][j];
        }
        return res;
    }

    static class PlanB{
        public int[][] copy;
        public int rows;
        public int cols;

        public PlanB(int[][] ori) {
            rows = ori.length + 1;
            cols = ori[0].length + 1;
            copy = new int[rows][cols];
            for (int row = 1; row < rows; row++)
                System.arraycopy(ori[row - 1], 0, copy[row], 1, cols - 1);
        }

        public int sum(int startR, int startC, int endR, int endC){
            if (startR < 1 || startC < 1 || endR >= rows || endC >= cols || startR > endR || startC > endC)
                return Integer.MIN_VALUE;
            int res = 0;
            for (int i = startR; i <= endR; i++)
                for (int j = startC; j <= endC; j++)
                    res += copy[i][j];
            return res;
        }

        public void add(int row, int col, int C){
            copy[row][col] += C;
        }
    }


    public static void main(String[] args) {
        int addTime = 50;
        int addLimit = 100;
        int queryTime = 30;
        int maxRow = 10;
        int maxCol = 10;
        int maxVal = 100;
        int testTime = 1000000;
        System.out.println("test begin");
        for (int i = 0; i < testTime; i++) {
            int[][] ori = arrays.generateRandomMatrix(maxRow, maxCol, maxVal);
            IndexTree2D tree = new IndexTree2D(ori);
            IndexTree2D.PlanB planB = new IndexTree2D.PlanB(ori);
            int rows = ori.length;
            int cols = ori[0].length;
            for (int k = 0; k < addTime; k++) {
                int row = (int) (Math.random() * rows) + 1;
                int col = (int) (Math.random() * cols) + 1;
                int C = (int) (Math.random() * addLimit) + 1;
                tree.add(row, col, C);
                planB.add(row, col, C);
            }
            for (int k = 0; k < queryTime; k++) {
                int row1 =(int) (Math.random() * rows) + 1;
                int row2 =(int) (Math.random() * rows) + 1;
                int col1 =(int) (Math.random() * cols) + 1;
                int col2 =(int) (Math.random() * cols) + 1;
                int startR = Math.min(row1, row2);
                int endR = Math.max(row1, row2);
                int startC = Math.min(col1, col2);
                int endC = Math.max(col1, col2);
                if (tree.sum(startR, startC, endR, endC) != planB.sum(startR, startC, endR, endC)){
                    System.out.println("Failed");
                    return;
                }
            }
        }
        System.out.println("AC");
    }
}
