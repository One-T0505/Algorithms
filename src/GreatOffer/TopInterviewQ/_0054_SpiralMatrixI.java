package GreatOffer.TopInterviewQ;

// 给你一个 m 行 n 列的矩阵 matrix ，请按照顺时针螺旋顺序，返回矩阵中的所有元素。

import java.util.ArrayList;
import java.util.List;

public class _0054_SpiralMatrixI {


    public static List<Integer> spiralOrder(int[][] matrix) {
        ArrayList<Integer> res = new ArrayList<>();
        if (matrix == null || matrix[0].length == 0)
            return res;
        int N = matrix.length;
        int M = matrix[0].length;
        int endR = N - 1;
        int endC = M - 1;
        int startR = 0, startC = 0;
        while (startR <= endR && startC <= endC){
            printCycle(matrix, startR++, startC++, endR--, endC--, res);
        }
        return res;
    }

    private static void printCycle(int[][] m, int startR, int startC, int endR, int endC,
                                  ArrayList<Integer> res) {
        // 水平线
        if (startR == endR){
            while (startC <= endC)
                res.add(m[startR][startC++]);
        } else if (startC == endC) { // 垂线
            while (startR <= endR)
                res.add(m[startR++][startC]);
        } else {
            int i = startR, j = startC;
            while (j < endC)
                res.add(m[i][j++]);
            while (i < endR)
                res.add(m[i++][j]);
            while (j > startC)
                res.add(m[i][j--]);
            while (i > startR)
                res.add(m[i--][j]);
        }
    }


    public static void main(String[] args) {
        int[][] m = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        int[][] m2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<Integer> res = spiralOrder(m);
        List<Integer> res2 = spiralOrder(m2);
        System.out.println(res);
        System.out.println(res2);
    }
}
