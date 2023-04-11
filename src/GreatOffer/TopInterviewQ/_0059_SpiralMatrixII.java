package GreatOffer.TopInterviewQ;

/**
 * ymy
 * 2023/3/20 - 10 : 07
 **/

// 给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix 。

public class _0059_SpiralMatrixII {


    public static int[][] generateMatrix(int n) {
        int[][] m = new int[n][n];
        int endR = n - 1, endC = n - 1;
        int startR = 0, startC = 0;
        int seed = 1;
        while(startR <= endR){
            int i = startR, j = startC;
            if (startR == endR){
                m[startR][startC] = seed;
            } else {
                while(j < endC)
                    m[i][j++] = seed++;
                while(i < endR)
                    m[i++][j] = seed++;
                while(j > startC)
                    m[i][j--] = seed++;
                while(i > startR)
                    m[i--][j] = seed++;
            }
            startR++;
            startC++;
            endR--;
            endC--;
        }
        return m;
    }
}
