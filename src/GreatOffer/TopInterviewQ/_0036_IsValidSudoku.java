package GreatOffer.TopInterviewQ;


// 请你判断一个 9 x 9 的数独是否有效。只需要根据以下规则 ，验证已经填入的数字是否有效即可:
//  1.数字 1-9 在每一行只能出现一次。
//  2.数字 1-9 在每一列只能出现一次。
//  3.数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。（请参考示例图）

// 注意：
//  一个有效的数独（部分已被填充）不一定是可解的。
//  只需要根据以上规则，验证已经填入的数字是否有效即可。
//  空白格用 '.' 表示。

public class _0036_IsValidSudoku {


    // 因为这是一个9*9的网格，我们将其划分成9部分，也就是每3*3作为一个基本的数独单元，所以整个网格有9个数独单元。
    // 每一个数独单元里只能填1～9，且每个数字只能出现一次。
    // 我们需要定义三个变量：
    //  1.9*10的二维布尔型矩阵row  row[i][j] 表示第i行上数字j是否出现过
    //  2.9*10的二维布尔型矩阵col  col[i][j] 表示第i列上数字j是否出现过
    //  3.9*10的二维布尔型矩阵cell cell[i][j] 表示第i个数独单元里数字j是否出现过

    public boolean isValidSudoku(char[][] board) {
        boolean[][] row = new boolean[9][10];
        boolean[][] col = new boolean[9][10];
        boolean[][] cell = new boolean[9][10];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // 求当前位置属于第几个数独单元
                int k = (i / 3) * 3 + (j / 3);
                if (board[i][j] != '.') {
                    int cur = board[i][j] - '0';
                    if (!row[i][cur] && !col[j][cur] && !cell[k][cur]) {
                        row[i][cur] = true;
                        col[j][cur] = true;
                        cell[k][cur] = true;
                    } else
                        return false;
                }
            }
        }
        return true;
    }
}
