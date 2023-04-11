package GreatOffer.TopInterviewQ;


// 给定一个 m x n 二维字符网格 board 和一个字符串单词 word。如果 word 存在于网格中，返回 true；否则，返回false。
// 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格
// 内的字母不允许被重复使用。

public class _0079_WordSearch {

    public boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0 || board[0].length == 0 ||
                word == null || word.equals(""))
            return false;
        char[] des = word.toCharArray();
        int N = board.length;
        int M = board[0].length;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j] == des[0] && f(board, i, j, des, 0))
                    return true;
            }
        }
        return false;
    }

    private boolean f(char[][] board, int i, int j, char[] des, int pos) {
        if (pos == des.length)
            return true;
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length)
            return false;
        if (board[i][j] != des[pos])
            return false;
        char tmp = board[i][j];
        board[i][j] = 0;
        boolean res = f(board, i - 1, j, des, pos + 1) |
                f(board, i + 1, j, des, pos + 1) | f(board, i, j - 1, des, pos + 1) |
                f(board, i, j + 1, des, pos + 1);
        board[i][j] = tmp;
        return res;
    }
}
