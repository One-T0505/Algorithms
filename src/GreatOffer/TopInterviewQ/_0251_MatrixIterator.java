package GreatOffer.TopInterviewQ;


// 手动实现一个迭代器类

public class _0251_MatrixIterator {

    public static class Vector2D {
        private int[][] matrix;
        private int row;   // row和col两个变量就是记录现在游标的位置
        private int col;
        private boolean curUse;  // 游标当前指向位置的元素是否使用过

        public Vector2D(int[][] m) {
            matrix = m;
            // 初始化时，整个矩阵都还没访问，所以游标的位置就是0行-1列
            row = 0;
            col = -1;
            curUse = true;
            hasNext();
        }


        public int next() {
            int res = matrix[row][col];
            curUse = true;
            hasNext();
            return res;
        }

        public boolean hasNext() {
            if (row == matrix.length)
                return false;
            if (!curUse)
                return true;
            // (row, col)这个位置的元素已经使用过了
            if (col < matrix[row].length - 1)
                col++;
            else {
                col = 0;
                do {
                    row++;
                } while (row < matrix.length && matrix[row].length == 0);
            }
            // 来到了新的(row, col)位置
            if (row != matrix.length) {
                curUse = false;
                return true;
            } else
                return false;
        }


    }
}
