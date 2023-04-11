package Basic.DynamicProgramming;

// 请自行搜索或者想象一个象棋的棋盘,然后把整个棋盘放入第一象限，棋盘的最左下角是(0,0)位置
// 那么整个棋盘就是横坐标上9条线、纵坐标上10条线的区域。给你三个参数x,y,k
// 返回“马”从(0,0)位置出发，必须走k步，最后落在(x,y)上的方法数有多少种?
// 其实就是十条竖线，九条横线，左下角为(0,0)，右上角为(9,8)

public class Chess {

    // 暴力递归寻求尝试
    public static int jump(int x, int y, int k){
        if (x < 0 || x > 9 || y < 0 || y > 8)
            return 0;
        return process(0, 0, x, y, k);
    }

    // 该方法表示：当前位置在(curX,curY)，经过k步能到达(x,y)的方法数
    private static int process(int curX, int curY, int x, int y, int rest) {
        if (curX < 0 || curX > 9 || curY < 0 || curY > 8)  // 跳越界的情况
            return 0;
        // 执行到这里说明当前位置不越界
        if (rest == 0)
            return (curX == x && curY == y) ? 1 : 0;
        // 分析一般情况：一个马在任意一点上，都有8种跳法，朝着四面八方出去，可以画个图看看马的跳法的可能性。
        // 最后的结果就是这8种可能性的和
        int res = process(curX + 2, curY + 1, x, y, rest - 1);
        res += process(curX + 1, curY + 2, x, y, rest - 1);
        res += process(curX - 1, curY + 2, x, y, rest - 1);
        res += process(curX - 2, curY + 1, x, y, rest - 1);
        res += process(curX - 2, curY - 1, x, y, rest - 1);
        res += process(curX - 1, curY - 2, x, y, rest - 1);
        res += process(curX + 1, curY - 2, x, y, rest - 1);
        res += process(curX + 2, curY - 1, x, y, rest - 1);

        return res;
    }
    // ===================================================================================================


    // 现在用动态规划来改暴力递归。发现递归过程中有3个可变参数，所以得申请一个三维数组。
    // curX范围：0~9   curY范围：0～8   rest范围：0~k
    public static int dp(int x, int y, int k){
        if (x < 0 || x > 9 || y < 0 || y > 8)
            return 0;
        int[][][] cache = new int[10][9][k + 1];
        // 可以发现递归时：rest依赖的8种情况都是rest-1的，所以这个填的顺序是从下到上一层一层填的
        cache[x][y][0] = 1;
        for (int rest = 1; rest <= k; rest++) {
            for (int curX = 0; curX < 10; curX++) {
                for (int curY = 0; curY < 9; curY++) {
                    int res = pick(cache, curX + 2, curY + 1, rest - 1);
                    res += pick(cache, curX + 1, curY + 2,rest - 1);
                    res += pick(cache, curX - 1, curY + 2, rest - 1);
                    res += pick(cache, curX - 2, curY + 1, rest - 1);
                    res += pick(cache, curX - 2, curY - 1, rest - 1);
                    res += pick(cache, curX - 1, curY - 2, rest - 1);
                    res += pick(cache, curX + 1, curY - 2, rest - 1);
                    res += pick(cache, curX + 2, curY - 1, rest - 1);

                    cache[curX][curY][rest] = res;
                }
            }
        }
        return cache[0][0][k];
    }

    // process里面的第一个base case，就是跳越界的情况，单独列出来，这样下面8种情况的递归就不用考虑边界了，但是
    // 在动态规划中，没有办法是否判断想取的数值是否越界了，所以单独写一个方法，看要拿的位置是否越界，
    // 越界返回0，不越界则如实返回
    private static int pick(int[][][] cache, int curX, int curY, int rest) {
        if (curX < 0 || curX > 9 || curY < 0 || curY > 8)  // 跳越界的情况
            return 0;
        return cache[curX][curY][rest];
    }


    public static void main(String[] args) {
        int x = 7, y = 8;
        int k = 12;
        long s1 = System.currentTimeMillis();
        int res1 = jump(x, y, k);
        long e1 = System.currentTimeMillis();
        System.out.println(res1 + "  " + (e1 - s1) + "ms");
        long s2 = System.currentTimeMillis();
        int res2 = dp(x, y, k);
        long e2 = System.currentTimeMillis();
        System.out.println(res2 + "  " + (e1 - s1) + "ms");
    }

}
