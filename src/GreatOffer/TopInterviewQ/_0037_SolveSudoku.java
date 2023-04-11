package GreatOffer.TopInterviewQ;


// 编写一个程序，通过填充空格来解决数独问题。
// 数独的解法需 遵循如下规则：
//  1.数字 1-9 在每一行只能出现一次。
//  2.数字 1-9 在每一列只能出现一次。
//  3.数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
// 数独部分空格内已填入了数字，空白格用 '.' 表示。

// 提示：题目数据保证输入数独仅有一个解

public class _0037_SolveSudoku {

    // 36题和本道题有承上启下的作用，这道题我们依然要用到上一题的三个布尔矩阵来告诉我们哪些元素可以填，
    // 然后在整个网格上做深度优先遍历
    // 因为提示告诉我们给定的网格必然有唯一解，说明给的网格必然是有效的

    public void solveSudoku(char[][] board) {
        boolean[][] row = new boolean[9][10];
        boolean[][] col = new boolean[9][10];
        boolean[][] cell = new boolean[9][10];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // 求当前位置属于第几个数独单元
                int k = (i / 3) * 3 + (j / 3);
                if (board[i][j] != '.') {
                    int cur = board[i][j] - '0';
                    row[i][cur] = true;
                    col[j][cur] = true;
                    cell[k][cur] = true;
                }
            }
        }

        f(board, 0, 0, row, col, cell);
    }

    private boolean f(char[][] board, int i, int j, boolean[][] row, boolean[][] col,
                      boolean[][] cell) {
        if (i == 9)
            return true;
        if (j == 9)
            return f(board, i + 1, 0, row, col, cell);
        if (board[i][j] != '.')
            return f(board, i, j + 1, row, col, cell);
        else {
            int k = (i / 3) * 3 + (j / 3);
            for (int cur = 1; cur <= 9; cur++) {
                if (!row[i][cur] && !col[j][cur] && !cell[k][cur]) {
                    board[i][j] = (char) cur;
                    row[i][cur] = true;
                    col[j][cur] = true;
                    cell[k][cur] = true;
                    if (f(board, i, j + 1, row, col, cell))
                        return true;
                    board[i][j] = '.';
                    row[i][cur] = false;
                    col[j][cur] = false;
                    cell[k][cur] = false;
                }
            }
            return false;
        }
    }
}
