package GreatOffer.TopInterviewQ;


// 给你一个 m x n 的矩阵 board，由若干字符 'X' 和 'O'，找到所有被 'X' 围绕的区域，
// 并将这些区域里所有的 'O' 用 'X' 填充。

public class _0130_SurroundedRegions {


    // 这个问题的核心是体系学习班第16节的岛问题的感染过程
    public void solve(char[][] board) {
        if (board == null || board.length == 0 || board[0] == null || board[0].length == 0)
            return;
        int N = board.length;
        int M = board[0].length;

        // 下面两个for循环是把board最外一圈的先改成F，这些区域是不算作完全被X封闭的区域
        for (int j = 0; j < M; j++) {
            if (board[0][j] == 'O')
                free(board, 0, j);
            if (board[N - 1][j] == 'O')
                free(board, N - 1, j);
        }

        for (int i = 1; i < N - 1; i++) {
            if (board[i][0] == 'O')
                free(board, i, 0);
            if (board[i][M - 1] == 'O')
                free(board, i, M - 1);
        }


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == 'O')
                    board[i][j] = 'X';
                if (board[i][j] == 'F')
                    board[i][j] = 'O';
            }
        }
    }

    private void free(char[][] board, int i, int j) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'O')
            return;
        board[i][j] = 'F';
        free(board, i - 1, j);
        free(board, i + 1, j);
        free(board, i, j - 1);
        free(board, i, j + 1);
    }
}
