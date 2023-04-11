package Basic.MonotonicStack;

// leetCode85
// 给定一个二维数组matrix，其中的值不是0就是1,返回全部由1组成的最大子矩形，内部有多少个1

// 分析：如果使用暴力方法，找出matrix中所有可能的矩形区域，然后验证该区域中是否全都是1，就可以了。
// 但是时间复杂度会来到：O(N的6次方)
// 用单调栈的结构可以优化到O(N2)

// 思路：若给一个矩阵matrix如下图：  用肉眼找可以找到最大的1矩形区域就是第0行， 一共6个
//                                                   0   1 1 1 1 1 1
//                                                   1   1 0 1 0 1 1
//                                                   2   0 1 1 1 0 1
//                                                   3   1 1 0 1 1 0
// 我们这样为每行做一个辅助数组help0, 从第0行开始，让help0和
// 第0行一模一样：help0=[1, 1, 1, 1, 1, 1]  help1就是第1行的辅助数组，
//            ：help1=[2, 0, 2, 0, 2, 2]  因为第1行对应的位置如果为0，那么辅助数组中的值就清空为0，
// 如果第1行对应的位置为1，那就累加上之前一个辅助数组对应位置的值，这样就能做出和行数一样多的辅助数组：
// help0 = [1, 1, 1, 1, 1, 1]
// help1 = [2, 0, 2, 0, 2, 2]
// help2 = [0, 1, 3, 1, 0, 3]
// help3 = [1, 2, 0, 2, 1, 0]
//
// 每一个辅助数组就相当于以自己的那行元素为底向上到第0行做成的和exercise02里相同含义的统计图，然后对每个辅助数组都做一次单调栈
// 运算求出最大值即可。这里的可能性不是美剧所有情况，比如就没有单独选第1行和第2行来找子矩阵，因为以第2行为底时，我们考虑的是最大
// 的情况，向上到第0行，所以是包含了这种子情况的。

public class exercise03 {
    public static int findOneV1(int[][] matrix){
        // 制作辅助数组help
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] help = new int[rows][cols];
        System.arraycopy(matrix[0], 0, help[0], 0, cols);
        for (int row = 1; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                help[row][col] = matrix[row][col] == 0 ? 0 : help[row - 1][col] + 1;
            }
        }
        int res = Integer.MIN_VALUE;
        for (int i = 0; i < rows; i++) {
            int rectangle = exercise02.findRectangle(help[i]);
            res = Math.max(res, rectangle);
        }
        return res;
    }


    // help其实只需要一行就行了，不需要和matrix一样大的空间，这里需要用到数组压缩
    public static int findOneV2(int[][] matrix){
        // 制作辅助数组help
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] help = new int[cols];
        System.arraycopy(matrix[0], 0, help, 0, cols);
        int res = Integer.MIN_VALUE;
        res = Math.max(res, exercise02.findRectangle(help));
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                help[j] = matrix[i][j] == 0 ? 0 : help[j] + 1;
            }
            res = Math.max(res, exercise02.findRectangle(help));
        }
        return res;
    }


    public static void main(String[] args) {
        int[][] matrix = {{1, 1, 1, 1, 1, 1},
                          {1, 0, 1, 0, 1, 1},
                          {0, 1, 1, 1, 0, 1},
                          {1, 1, 0, 0, 1, 0}};
        System.out.println(findOneV1(matrix));
        System.out.println(findOneV2(matrix));
    }
}
