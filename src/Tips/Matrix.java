package Tips;

public class Matrix {
    // 1.拉链式打印矩阵
    public static void zigZag(int[][] matrix){
        int endRow = matrix.length - 1, endCol = matrix[0].length - 1;
        boolean topToBottom = false;  // 最开始是从下到上

        int Arow = 0, Acol = 0; // 锚点A
        int Brow = 0, Bcol = 0; // 锚点B

        while (Arow != endRow + 1){
            printLine(matrix, Arow, Acol, Brow, Bcol, topToBottom);
            Arow = Acol == endCol ? Arow + 1 : Arow;
            Acol = Acol == endCol ? Acol : Acol + 1;
            Bcol = Brow == endRow ? Bcol + 1 : Bcol;
            Brow = Brow == endRow ? Brow : Brow + 1;
            topToBottom = !topToBottom;
        }
    }

    private static void printLine(int[][] matrix, int arow, int acol, int brow, int bcol, boolean topToBottom) {
        if (topToBottom){
            while (arow != brow + 1){
                System.out.print(matrix[arow++][acol--] + "\t");
            }
        } else {
            while (bcol != acol + 1)
                System.out.print(matrix[brow--][bcol++] + "\t");
        }
        System.out.println();
    }

    // 2.旋转式打印矩阵
    public static void Circle(int[][] matrix){
        int startR = 0, startC = 0;
        int endR = matrix.length - 1, endC = matrix[0].length - 1;

        while (startR <= endR && startC <= endC){
            printLayer(matrix, startR++, startC++, endR--, endC--);
            System.out.println();
        }
    }

    private static void printLayer(int[][] matrix, int startR, int startC, int endR, int endC) {
        if (startR == endR){
            while (startC <= endC)
                System.out.print(matrix[startR][startC++] + "\t");
        } else if (startC == endC) {
            while (startR <= endR)
                System.out.print(matrix[startR++][startC] + "\t");
        } else {
            int SC = startC, SR = startR;
            while (startC < endC)
                System.out.print(matrix[startR][startC++] + "\t");
            while (startR < endR)
                System.out.print(matrix[startR++][startC] + "\t");
            while (startC > SC)
                System.out.print(matrix[startR][startC--] + "\t");
            while (startR > SR)
                System.out.print(matrix[startR--][startC] + "\t");

        }
    }

    // 3.方阵顺时针旋转90度
    public static void rotate(int[][] matrix){
        int startR = 0, startC = 0;
        int endR = matrix.length - 1, endC = matrix[0].length - 1;
        while (startR <= endR && startC <= endC)
            rotateLayer(matrix, startR++, startC++, endR--, endC--);
    }

    private static void rotateLayer(int[][] matrix, int startR, int startC, int endR, int endC) {
        int groups = endC - startC; // 分成的组数
        int tmp = 0;
        for (int i = 0; i < groups; i++) {
            tmp = matrix[startR][startC + i];
            matrix[startR][startC + i] = matrix[endR - i][startC];
            matrix[endR - i][startC] = matrix[endR][endC - i];
            matrix[endR][endC - i] = matrix[startR + i][endC];
            matrix[startR + i][endC] = tmp;
        }
    }

    public static void main(String[] args) {
        int[][] m = {{12, 6, 21, 8},
                     {4, 15, 10, 22},
                     {33, 14, 5, 24},
                     {19, 17, 3, 13}};
        rotate(m);
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
