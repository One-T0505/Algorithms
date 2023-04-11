package Basic.Recurse;

public class NQueens {
    // N皇后问题是指在N*N的棋盘，上要摆N个皇后,要求任何两个皇后不同行、不同列，也不在同一条斜线上.
    // 比如a[0][0]，a[1][3], a[2][2],这种情况也不符合，因为a[0][0]和a[2][2]在同一条斜线上；
    // 同样，a[3][3]和a[6][0]也不行，因为他们在反对角线上
    // 给定一个整数n，返回n皇后的摆法有多少种。n=1,返回1； ∩=2或3,无论怎么摆都不行，返回0；n=8，返回92

    public static int nQueens(int n){
        if (n < 1)
            return 0;
        int[] record = new int[n];  // 用一维数组记录各个皇后位置，默认索引就是行，值就是列
        return process1(0, record, n);
    }

    // 潜台词：record[0..i-1]的皇后都按要求摆好了，现在来摆第i皇后，也就是来到了棋盘的第i行
    // 返回值：摆完所有的皇后合理的摆法有多少种
    private static int process1(int i, int[] record, int n) {
        if (i == n)  // record最多到n-1
            return 1; // 上面0-i-1的状况算一种合理的摆法
        int res = 0;
        for (int j = 0; j < n; j++) { // 当前在第i行，尝试每一列的位置
            // 当前i行的皇后，放在j列，会不会和之前(0..i-1)的皇后，不共行共列或者共斜线，
            // 如果是，认为有效.如果不是，认为无效
            if (isValid(record, i, j)){
                record[i] = j;
                res += process1(i + 1, record, n);
            }
        }
        return res;
    }

    private static boolean isValid(int[] record, int i, int j) {
        for (int k = 0; k < i; k++) {
            // 共列 || 共斜线
            if (j == record[k] || Math.abs(k - i) == Math.abs(record[k] - j))
                return false;
        }
        return true;
    }

    // n皇后问题用位运算来极大缩小常数项复杂度
    public static int nQueens_v2(int n){
        if (n < 1 || n > 32)
            return 0;
        // 如果是8皇后问题，limit最右边8位都是1，其他都是0。limit表示问题规模
        int limit = n == 32 ? -1 : (1 << n) - 1;
        return process2(limit, 0, 0, 0);
    }

    // colLim 列的限制，1的位置不能放皇后，0的位置可以
    // leftLim 左对角线的限制 1的位置不能放皇后，0的位置可以
    // rightLim 右对角线的限制 1的位置不能放皇后，0的位置可以
    private static int process2(int limit, int colLim, int leftLim, int rightLim) {
        if (colLim == limit)
            return 1;
        // pos上为1的位置都是可摆皇后的地方
        // colLim | leftLim | rightLim 可得到总限制，1表示不能放的地方，再取反后1就表示可以放的位置。
        // 但是32位中前面有一大坨0取反时都变为1，所以再和limit与操作  limit其实就是为了让变乱的数在回归到整齐的状态
        int pos = limit & (~(colLim | leftLim | rightLim));
        // 然后尝试pos中每一个为1的位置
        int rightestOne = 0, res = 0;
        while (pos != 0){
            rightestOne = pos & (~pos + 1);  // 提取自己最右侧的1
            pos -= rightestOne;
            res += process2(limit, colLim | rightestOne, (leftLim | rightestOne) << 1,
                    (rightLim | rightestOne) >> 1);
        }
        return res;
    }


    public static void main(String[] args) {
        long start1 = System.currentTimeMillis();
        int res1 = nQueens_v2(14);
        long end1 = System.currentTimeMillis();
        System.out.println(res1 + "  " + (end1 - start1) + "ms");

        long start2 = System.currentTimeMillis();
        int res2 = nQueens(14);
        long end2 = System.currentTimeMillis();
        System.out.println(res2 + "  " + (end2 - start2) + "ms");
    }
}
