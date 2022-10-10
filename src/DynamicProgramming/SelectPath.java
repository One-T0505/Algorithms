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
    public static int dpV1(int[][] matrix){
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
    // ====================================================================================================


    // 一定要一个和matrix等大的缓存表吗？事实上可以进一步对矩阵进行压缩只需要一个一维数组即可。
    // 为什么可以继续压缩？ 因为缓存表中第i行的值只依赖于第i-1行，和之前的已经无关了。
    public static int dpV2(int[][] matrix){
        if (matrix == null || matrix.length == 0)
            return 0;
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] cache = new int[cols];
        // 先单独将matrix第一行填入到cache中，之后再将每行的值更新到cache
        cache[0] = matrix[0][0];
        for (int i = 1; i < cols; i++)
            cache[i] = cache[i - 1] + matrix[0][i];
        // 再来处理剩下的行
        for (int row = 1; row < rows; row++) {
            cache[0] += matrix[row][0];
            for (int col = 1; col < cols; col++)
                cache[col] = Math.min(cache[col - 1], cache[col]) + matrix[row][col];
        }
        return cache[cols - 1];
    }
    // 得到的启发：
    // 1.如果有别的题目也是这种依赖左、上的关系，那么也可以使用这种空间压缩的技巧。 依赖两方的熟悉之后，如果是依赖
    //   三方的呢？比如cache中一个非边界单元依赖其左、上、左上三个值，这样的话如何使用空间压缩？
    //   其实只需要一个变量单独保存一下其左上的值，然后让该值也一起移动，每次更新成当前元素左上的值。
    // 2.这样申请一个与列数保持一致的数组来实现压缩只适用于列数和行数相差不大或者列数小于行数的情况，如果有一个矩阵
    //   形状为(4,100万)，这样压缩的效果不明显。可以申请一个和行数保持一致的数组，然后按列处理矩阵的元素。
    // ====================================================================================================


    public static void main(String[] args) {
        int[][] matrix = {{3, 5, 2, 9},
                          {6, 11, 8, 4},
                          {10, 13, 4, 5}};
        System.out.println(selectPathV1(matrix));
        System.out.println(selectPathV2(matrix));
        System.out.println(dpV1(matrix));
        System.out.println(dpV2(matrix));
    }
}
