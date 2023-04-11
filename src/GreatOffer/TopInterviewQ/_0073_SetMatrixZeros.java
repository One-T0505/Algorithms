package GreatOffer.TopInterviewQ;

// 给定一个 m x n 的矩阵，如果一个元素为 0，则将其所在行和列的所有元素都设为0。请使用原地算法。

public class _0073_SetMatrixZeros {

    // 该方法最简单的思路，就是遍历矩阵，发现当前元素为0了，就把对应行和对应列的元素全部设为0；
    // 这样做是不可以的，因为因为某个位置的0而导致整行整列都变成了0之后，当我们后续遍历到这些0的时候是否也要
    // 修改整行整列的值为0呢？痛点就在于碰到的0无法判断是矩阵中初始时就有的还是因为之前的某个0的改动才产生的。
    // 所以我们可以先遍历一遍矩阵，先不着急修改矩阵，而是把所有的0找出来，把对应的所有需要改动的行和列全部记录下来，
    // 最后一并修改。

    public static void setZeros(int[][] matrix) {
        int N = matrix.length;
        int M = matrix[0].length;

        boolean[] row = new boolean[N];
        boolean[] col = new boolean[M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (matrix[i][j] == 0) {
                    row[i] = true;
                    col[j] = true;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            if (row[i]) {
                for (int j = 0; j < M; j++)
                    matrix[i][j] = 0;
            }
        }

        for (int j = 0; j < M; j++) {
            if (col[j]) {
                for (int i = 0; i < N; i++)
                    matrix[i][j] = 0;
            }
        }
    }
    // ============================================================================================


    // 这个问题的优化点在于如何更好地省空间。我们先假设第0行和第0列上都没有0；然后在剩余位置遍历时，碰到了0，
    // 比如在[i,j]位置碰到了0，那么我们就把[0,j]设置为0，第0行上的某个元素为0，就表示最终修改矩阵时对应的列
    // 需要变为0；再把[i,0]设置为0，第0列上的某个元素为0，表明最终修改矩阵时对应的行需要变为0；所以除了第0行
    // 和第0列以外，其他所有位置的需要变动情况全部都在该矩阵上完成了信息的记录，不用再额外开辟空间，所以很省空间。
    // 但是如果第0行和第0列有0，那么就不可行，因为有了歧义，这个问题很好解决，只需要提前记录第0行和第0列的情况，
    // 再把对应位置的0修改成其他的值即可。

    public static void setZeros2(int[][] matrix) {
        int N = matrix.length;
        int M = matrix[0].length;

        // 提前记录第0行、第0列是否需要变为0
        boolean row = false;
        boolean col = false;

        for (int i = 0; i < N; i++) {
            if (matrix[i][0] == 0) {
                col = true;
                break;
            }
        }

        for (int j = 0; j < M; j++) {
            if (matrix[0][j] == 0) {
                row = true;
                break;
            }
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (matrix[i][j] == 0) {
                    matrix[0][j] = 0;
                    matrix[i][0] = 0;
                }
            }
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0)
                    matrix[i][j] = 0;
            }
        }

        if (row) {
            for (int j = 0; j < M; j++)
                matrix[0][j] = 0;
        }

        if (col) {
            for (int i = 0; i < N; i++)
                matrix[i][0] = 0;
        }
    }
    // ============================================================================================


    // 仅仅只使用一个额外变量。让(i,0)处的元素指示第i行是否要变为0；(0,j) j>=1 处的元素指示第j列是否需要变为0；
    // 所以除了第0列没有变量记录以外，其他的行列都有记录，所以我们只需要一个变量记录第0列是否需要变为0。

    public static void setZeros3(int[][] matrix) {
        int N = matrix.length;
        int M = matrix[0].length;

        // 提前记录第0列是否需要变为0
        boolean col = false;

        for (int i = 0; i < N; i++) {
            if (matrix[i][0] == 0) {
                col = true;
                break;
            }
        }

        for (int j = 1; j < M; j++) {
            if (matrix[0][j] == 0) {
                matrix[0][0] = 0;
                break;
            }
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0)
                    matrix[i][j] = 0;
            }
        }

        if (matrix[0][0] == 0) {
            for (int j = 1; j < M; j++)
                matrix[0][j] = 0;
        }

        if (col) {
            for (int i = 0; i < N; i++)
                matrix[i][0] = 0;
        }
    }


    public static void main(String[] args) {
        int[][] matrix = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        setZeros3(matrix);
    }
}
