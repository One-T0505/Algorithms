package GreatOffer.TopInterviewQ;

/**
 * ymy
 * 2023/3/13 - 22 : 02
 **/

// 给定一个 n × n 的二维矩阵 matrix 表示一个图像。请你将图像顺时针旋转 90 度。
// 你必须在 原地 旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要 使用另一个矩阵来旋转图像。

public class _0048_RotateImage {


    // 这个就是体系学习班里讲的矩阵相关处理的问题。关键点是找到一种宏观调度方式
    public static void rotate(int[][] matrix) {
        int N = matrix.length;
        int edgeLen = N - 1;
        for (int layer = 0; layer < N; layer++) {
            if (edgeLen < 1)
                break;
            for (int i = 0; i < edgeLen; i++) {
                int tmp = matrix[layer][layer + i];
                matrix[layer][layer + i] = matrix[N - 1 - i - layer][layer];
                matrix[N - 1 - i - layer][layer] = matrix[N - 1 - layer][N - 1 - i - layer];
                matrix[N - 1 - layer][N - 1 - i - layer] = matrix[layer + i][N - 1 - layer];
                matrix[layer + i][N - 1 - layer] = tmp;
            }
            edgeLen -= 2;
        }
    }


    public static void main(String[] args) {
        int[][] m = {{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
        rotate(m);
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
