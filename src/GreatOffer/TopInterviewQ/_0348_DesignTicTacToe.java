package GreatOffer.TopInterviewQ;


// 给一个正整数n，表示n*n大的棋盘。现在有两个人编号为1，2来下棋。1号每下一步棋就会让某个格子的值变为1；
// 2号每下一步棋就会让对应的格子变成2。获胜的条件：
//  1.某一行或某一列全是一样的数字
//  2.两条主对角线上有一条上的数字全都一样
// 两个条件完成其一就表示获胜了，获胜的某行或某列或某条对角线上的数字就代表获胜的人。

public class _0348_DesignTicTacToe {

    // 我们需要统计信息，并且每下一步，都能同步对局的信息

    public static class TicTacToe {
        private int[][] rows;   // rows[1][2] == 3  表示1号玩家在第2行上一共有3颗棋子
        private int[][] cols;
        private int[] leftUp;  // 从左上到右下那条对角线上的信息  leftUp[1] == 2  1号玩家在该对角线上下了2个
        private int[] rightUp;
        private boolean[][] matrix;  // 对应于整个N*N棋盘，记录某个格子是否下过
        private int N;


        public TicTacToe(int n) {
            N = n;
            rows = new int[3][N];   // 因为只有两个人编号为1，2，所以3就可以了
            cols = new int[3][N];
            leftUp = new int[3];
            rightUp = new int[3];
            matrix = new boolean[N][N];

        }


        // player玩家要在(i,j)处下一步棋，该方法要完成下棋这个动作
        // 并且在每下完一步后，都要对当前的局势进行判断，看是否有胜利方出现
        // 返回值0表示还没有获胜   返回1表示玩家1获胜  返回2表示玩家2获胜
        public int move(int i, int j, int player) {
            if (matrix[i][j])  // 如果已经下过了
                return 0;
            matrix[i][j] = true;
            rows[player][i]++;
            cols[player][j]++;
            // 还要判断是否在对角线上
            if (i == j)
                leftUp[player]++;
            if (i + j == N)
                rightUp[player]++;
            // 判断是否有获胜方出现
            if (rows[player][i] == N || cols[player][j] == N || leftUp[player] == N ||
                    rightUp[player] == N)
                return player;
            return 0;
        }
    }
}
