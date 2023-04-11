package Basic.DynamicProgramming;

// 给定3个参数：N, M，K
// 怪兽有N滴血，等着英雄来砍自己，英雄每一次打击，都会让怪兽流失[0~M]的血量，到底流失多少?
// 每一次在[0~M]上等概率的获得一个值，求K次打击之后，英雄把怪兽砍死的概率。k次之前砍死也算

public class KillMonster {

    // 暴力递归法，不剪枝。就是说中途让怪兽的血量<=0时也不停止，继续递归，只需要判断最后血量是否<=0即可
    public static double killMonsterV1(int N, int M, int K){
        if (N < 1 || M < 1 || K < 1)
            return 0;
        long all = (long) Math.pow(M + 1, K); // 所有的可能性
        long killed = (long) process1(N, M, K);
        return ((double) killed / (double) all);
    }

    // 该方法的定义是：N表示剩余血量，K表示剩余攻击次数，返回能杀死怪兽的方法数
    private static int process1(int N, int M, int K) {
        if (K == 0)  // 这个base case就体现了：即使中途血量<=0也不会停止，唯一会停止的情况就是攻击次数用完
            return N <= 0 ? 1 : 0;
        // 这个分支在一开始写递归的时候是没有的，但是在写动态规划时，发现填表时的一些问题，所以反向给
        // 递归加了个分支用于剪枝
        if (N <= 0)
            return (int) Math.pow(M + 1, K);
        int res = 0;
        for (int i = 0; i <= M; i++)
            res += process1(N - i, M, K - 1);
        return res;
    }
    // ====================================================================================================


    // 改成动态规划法
    public static double dpV1(int N, int M, int K){
        if (N < 1 || M < 1 || K < 1)
            return 0;
        // 每个血量值下都有M+1个分支，实际上就是(M+1)叉树
        long all = (long) Math.pow(M + 1, K); // 所有的可能性
        int[][] cache = new int[N + 1][K + 1];
        cache[0][0] = 1;
        for (int col = 1; col <= K; col++) {
            // 行代表剩余血量，血量等于0了，所以下面(M+1)肯定全部<=0，每一个路径到叶子结点时，如果血量<=0就算1种方法
            cache[0][col] = (int) Math.pow(M + 1, col);
            for (int row = 1; row <= N; row++) {
                int res = 0;
                for (int i = 0; i <= M; i++)
                    res += row - i >= 0 ? cache[row - i][col - 1] : (int) Math.pow(M + 1, col - 1);
                cache[row][col] = res;
            }
        }
        long killed = cache[N][K];
        return ((double) killed / (double) all);
    }
    // ====================================================================================================


    // 可以发现动态规划算每个单元格时有枚举行为，所以要思考如何继续优化。在纸上画出表格，严格推理依赖关系，找出可消除枚举的
    // 计算。
    public static double dpV2(int N, int M, int K){
        if (N < 1 || M < 1 || K < 1)
            return 0;
        // 每个血量值下都有M+1个分支，实际上就是(M+1)叉树
        long all = (long) Math.pow(M + 1, K); // 所有的可能性
        int[][] cache = new int[N + 1][K + 1];
        // 下面就是如何高效填表的方法，可能看起来会很难懂，所以最好是在纸上画图推算后再来看
        cache[0][0] = 1;
        for (int col = 1; col <= K; col++) {
            cache[0][col] = (int) Math.pow(M + 1, col);
            // cache[0] 这一行数组成等比数列，公比为(M+1)。其中cache[0][0]=1  cache[0][1]=M+1
            for (int row = 1; row <= N; row++) {
                cache[row][col] = cache[row][col - 1] + cache[row - 1][col];
                if (row <= (M + 1))
                    cache[row][col] -= cache[0][col - 1];
                else
                    cache[row][col] -= cache[row - M - 1][col - 1];
            }
        }
        long killed = cache[N][K];
        return ((double) killed / (double) all);
    }

    public static void main(String[] args) {
        System.out.println(killMonsterV1(3, 2, 3));
        System.out.println(dpV1(3, 2, 3));
        System.out.println(dpV2(3, 2, 3));
    }
}
