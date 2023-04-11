package Basic.DynamicProgramming;

// 给一个正整数n，求将它分解的方法数有多少种。分解的方法是将n拆分成若干个>0的整数的累加和，并且后一个数必须要不小于前一个数。
// eg：1的分解数有1种：1  2的分解数有2种：1+1  2    3的分解数有3种：1+1+1  1+2 （2+1是不可以的） 3

public class SplitNumber {

    // 暴力递归尝试
    public static int splitNumber(int n){
        if (n <= 0)
            return 0;
        if (n == 1)
            return 1;
        return process(1, n);
    }

    // pre表示上一次从数中分解出去的数值，rest表示还剩下多少需要分解
    // 返回值就是分解rest的方法数。传一个pre进去就是要告诉当前函数分解时必须要从>=pre的地方分解出去，
    // 所以在主函数中调用的是process(1,n) 就表示：当前要分解n时，分解出去的数不能小于1，合理。
    private static int process(int pre, int rest) {
        if (rest == 0)
            return 1;   // 说明之前的分解策略合理，返回一种成立的方法数
        if (pre > rest)
            return 0;   // 说明之前的策略失败
        int res = 0;
        for (int i = pre; i <= rest; i++) {
            res += process(i, rest - i);
        }
        return res;
    }
    // ==================================================================================================


    // 动态规划
    public static int dpV1(int n){
        if (n <= 0)
            return 0;
        if (n == 1)
            return 1;
        int[][] cache = new int[n + 1][n + 1]; // 行表示pre，列表示rest
        // 单独填第一列.  整个第一行是用不到的
        for (int i = 1; i <= n; i++)
            cache[i][0] = 1;
        // if (pre > rest) return 0;  表示对角线以下除了第一列就全部为0，但是默认初始化就是0。所以不用管
        // 并且pre=0这一行是用不到的
        for (int pre = n; pre >= 1; pre--) {
            for (int rest = pre; rest <= n; rest++) {
                int res = 0;
                for (int i = pre; i <= rest; i++)
                    res += cache[i][rest - i];
                cache[pre][rest] = res;
            }
        }
        return cache[1][n];
    }
    // ==================================================================================================


    // 动态规划优化。对空间依赖有了一定感知后，发现对角线上的元素c[i][i]的值就等于它所在行的开头元素 即：
    // c[i][i] = c[i][0] = 1   对于一般位置的元素依赖关系也很简单：
    //   c[3][5] = c[3][2] + c[4][1] + c[5][0]
    //   c[2][5] = c[2][3] + c[3][2] + c[4][1] + c[5][0]
    public static int dpV2(int n){
        if (n <= 0)
            return 0;
        if (n == 1)
            return 1;
        int[][] cache = new int[n + 1][n + 1]; // 行表示pre，列表示rest
        // 单独填第一列
        for (int i = 1; i <= n; i++)
            cache[i][0] = 1;
        // 单独填对角线
        for (int i = 1; i <= n; i++)
            cache[i][i] = 1;
        // if (pre > rest) return 0;  表示对角线以下除了第一列就全部为0，但是默认初始化就是0。所以不用管
        // 并且pre=0这一行是用不到的
        for (int pre = n - 1; pre >= 1; pre--) {
            for (int rest = pre + 1; rest <= n; rest++)
                cache[pre][rest] = cache[pre + 1][rest] + cache[pre][rest - pre];
        }
        return cache[1][n];
    }

    public static void main(String[] args) {
        System.out.println(splitNumber(37));
        System.out.println(dpV1(37));
        System.out.println(dpV2(37));
    }
}
