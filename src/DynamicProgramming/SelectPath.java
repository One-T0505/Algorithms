package DynamicProgramming;

// 给定一个二维数组matrix， 一个人必须从左上角出发，最后到达右下角
// 沿途只可以向下或者向右走，沿途的数字都累加就是距离累加和 返回最小距离累加和

public class SelectPath {

    // 暴力递归尝试1
    public static int selectPathV1(int[][] matrix){
        if (matrix == null || matrix.length == 0)
            return 0;
        int rows = matrix.length;
        int cols = matrix[0].length;
        return process1(matrix, 0, 0, rows - 1, cols - 1, 0);
    }

    // 返回从(curRow,curCol)到(desRow,desCol)能收集到的最小距离累加和
    // pre表示之前已经收集到的距离累加和
    private static int process1(int[][] matrix, int curRow, int curCol, int desRow, int desCol, int pre) {
        if (curRow < 0 || curRow > desRow || curCol < 0 || curCol > desCol)
            return 0;
        if (curRow == desRow && curCol == desCol)
            return pre + matrix[curRow][curCol];
        else if (curRow == desRow) { // 只能向右走
            return process1(matrix, curRow, curCol + 1, desRow, desCol,
                    pre + matrix[curRow][curCol]);
        } else if (curCol == desCol) { // 只能向下走
            return process1(matrix, curRow + 1, curCol, desRow, desCol,
                    pre + matrix[curRow][curCol]);
        } else { // 既能向下走也能向右走
            int p1 = process1(matrix, curRow, curCol + 1, desRow, desCol,
                    pre + matrix[curRow][curCol]);
            int p2 = process1(matrix, curRow + 1, curCol, desRow, desCol,
                    pre + matrix[curRow][curCol]);
            return Math.min(p1, p2);
        }
    }
    // ====================================================================================================


    // 暴力递归尝试2
    public static int selectPathV2(int[][] matrix){
        if (matrix == null || matrix.length == 0)
            return 0;
        int rows = matrix.length;
        int cols = matrix[0].length;
        return process2(matrix, 0, 0, rows - 1, cols - 1);
    }

    // 该方法返回从(curRow,curCol)到(desRow,desCol)能收集到的最小距离累加和
    private static int process2(int[][] matrix, int curRow, int curCol, int desRow, int desCol) {
        if (curRow > desRow || curCol > desCol)
            return 0;
        if (curRow == desRow && curCol == desCol)
            return matrix[curRow][curCol];
        if (curRow == desRow)
            return matrix[curRow][curCol] + process2(matrix, curRow, curCol + 1, desRow, desCol);
        if (curCol == desCol)
            return matrix[curRow][curCol] + process2(matrix, curRow + 1, curCol, desRow, desCol);
        int p1 = matrix[curRow][curCol] + process2(matrix, curRow + 1, curCol, desRow, desCol);
        int p2 = matrix[curRow][curCol] + process2(matrix, curRow, curCol + 1, desRow, desCol);
        return Math.min(p1, p2);
    }
    // ====================================================================================================


    // 用动态规划法改写暴力递归尝试2
    public static int dp(int[][] matrix){
        if (matrix == null || matrix.length == 0)
            return 0;
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] cache = new int[rows][cols];
        cache[rows - 1][cols - 1] = matrix[rows - 1][cols - 1];
        // 先单独逆序填最后一行
        for (int col = cols - 2; col >= 0; col--)
            cache[rows - 1][col] = matrix[rows - 1][col] + cache[rows - 1][col + 1];
        // 逆序填最后一列
        for (int row = rows - 2; row >= 0; row--)
            cache[row][cols - 1] = matrix[row][cols - 1] + cache[row + 1][cols - 1];
        // 填剩余部分
        for (int row = rows - 2; row >= 0; row--) {
            for (int col = cols - 2; col >= 0; col--) {
                int p1 = matrix[row][col] + cache[row + 1][col];
                int p2 = matrix[row][col] + cache[row][col + 1];
                cache[row][col] = Math.min(p1, p2);
            }
        }
        return cache[0][0];
    }

    public static void main(String[] args) {
        int[][] matrix = {{3, 5, 2, 9},
                          {6, 11, 8, 4},
                          {10, 13, 4, 5}};
        System.out.println(selectPathV1(matrix));
        System.out.println(selectPathV2(matrix));
        System.out.println(dp(matrix));
    }
}
