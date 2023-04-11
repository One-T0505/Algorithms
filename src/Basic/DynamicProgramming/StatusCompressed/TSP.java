package Basic.DynamicProgramming.StatusCompressed;

// TSP问题
// 有N个城市，任何两个城市之间的都有距离，任何一座城市到自己的距离都为0。所有点到点的距离存在一个N*N的二维
// 数组matrix里，也就是整张图由邻接矩阵表示。现要求一旅行商从k城市出发必须经过每一个城市且只在一个城市逗留
// 一次，最后回到出发的k城，返回总距离最短的路的距离。参数给定一个matrix，给定k。

import java.util.ArrayList;
import java.util.Arrays;
import utils.arrays;

public class TSP {

    public static int DES;

    public static int tsp1(int[][] m, int k){
        int N = m.length;  // 城市数量
        // 我们用数组来做一个集合，来表示哪些城市已经被访问过了。所以list.size一直都为N  下标从0～N-1
        // 如果list[i]==1说明i城还没被访问过，如果为null就说明，i城已经被访问过了
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            list.add(1);
        }
        DES = k;
        // 递归函数含义：从城市k出发，访问list中所有没被访问过的城市后，再返回到DES城市的最小距离
        return f(m, list, DES);
    }


    // 潜台词就是说start一定在list中且不为null
    private static int f(int[][] m, ArrayList<Integer> list, int start) {
        int cityNum = 0;
        for (Integer c : list) {
            if (c != null)
                cityNum++;
        }
        // 如果list中只有一个城市没被访问过了，并且起点还是start，说明这个城市就是start。
        // 回想递归函数的意义：从城市start出发，访问list中所有没被访问过的城市后，再返回到DES城市的最小距离
        // 现在只剩下一个start了，那就直接返回start去DES的距离就行了
        if (cityNum == 1){
            return m[start][DES];
        }
        // cityNum > 1
        int res = Integer.MAX_VALUE;
        list.set(start, null); // 设置为空，下面的递归中就不会重复访问了
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null){
                // 不为空说明i城市还没被访问过，才能继续递归下去。再回想下递归的含义：
                // 从城市i出发，访问list中所有没被访问过的城市后，再返回到DES城市的最小距离
                // 那么是不是还需要加上 start～i 这段的距离 才完整
                res = Math.min(res, f(m, list, i) + m[start][i]);
            }
        }
        list.set(start, 1); // 还原现场
        return res;
    }
    // ================================================================================================





    // 用二进制来表示城市存在与否，替代链表，优化空间。status的第i位是1，表示i城还没被访问过
    // 递归函数的含义一点都不变
    public static int tsp2(int[][] m, int k){
        int N = m.length;  // 城市数量
        DES = k;
        int status = (1 << N) - 1;
        // 递归函数含义：从城市k出发，访问list中所有没被访问过的城市后，再返回到DES城市的最小距离
        return g(m, status, DES);
    }

    private static int g(int[][] m, int status, int start) {
        // 这个表达式其实就是在说，如果status只剩一个1的情况  status & (-status) 是提取最右侧的1
        // 如果最右侧的1就等于本身，说明只有1个1了
        if ((status & (-status)) == status)
            return m[start][DES];
        // 还有多个城市
        int res = Integer.MAX_VALUE;
        // 先把start位上的1去掉
        status &= ~(1 << start);
        for (int i = 0; i < m.length; i++) {
            if (((1 << i) & status) != 0){
                // status ^ (1 << i)  就是将status的第i位设置为0
                res = Math.min(res, g(m, status, i) + m[start][i]);
            }
        }
        // 恢复现场
        status |= (1 << start);
        return res;
    }
    // =========================================================================================





    // 改成记忆化搜索，有两个可变参数，status 和 start
    public static int tsp3(int[][] m, int k){
        int N = m.length;  // 城市数量
        DES = k;
        int status = (1 << N) - 1;
        int[][] dp = new int[1 << N][N];
        for (int[] arr : dp)
            Arrays.fill(arr, -1);
        // 递归函数含义：从城市k出发，访问list中所有没被访问过的城市后，再返回到DES城市的最小距离
        return h(m, status, DES, dp);
    }

    private static int h(int[][] m, int status, int start, int[][] dp) {
        if (dp[status][start] != -1)
            return dp[status][start];
        int res = Integer.MAX_VALUE;
        if ((status & (-status)) == status)
            res = m[start][DES];
        else { // 还有多个城市
            status &= (~(1 << start));
            for (int i = 0; i < m.length; i++) {
                if (((1 << i) & status) != 0){
                    // status ^ (1 << i)  就是将status的第i位设置为0
                    // 为什么不用还原现场？看一下第464题的讲解
                    res = Math.min(res, h(m, status, i, dp) + m[start][i]);
                }
            }
            status |= 1 << start;
        }
        dp[status][start] = res;
        return res;
    }
    // ============================================================================================




    // 改成位置严格依赖的动态规划
    public static int tsp4(int[][] m, int k){
        int N = m.length;  // 城市数量
        DES = k;
        int status = (1 << N) - 1;
        // dp[i][j]表示：在i这种状态下，以j为起点，将剩下的还没有访问的城市都访问完，并返回到k城市需要的最短距离
        int[][] dp = new int[1 << N][N];
        return 0;
    }






    // for test
    // 生成符合题目要求的：主对角线全为0的对称矩阵，并且除对角线以外，所有值都>0
    public static int[][] generateGraph(int maxSize, int maxVal){
        int N = (int) (Math.random() * maxSize) + 1;
        int[][] m = new int[N][N];
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                int cur = (int) (Math.random() * maxVal) + 1;
                m[i][j] = m[j][i] = cur;
            }
        }
        return m;
    }



    public static void test(int testTime, int maxSize, int maxVal){
        System.out.println("功能测试开始");
        for (int i = 0; i < testTime; i++) {
            int[][] m = generateGraph(maxSize, maxVal);
            int k = (int) (Math.random() * m.length);
            int res1 = tsp1(m, k);
            int res2 = tsp2(m, k);
            int res3 = tsp3(m, k);
            if (res1 != res2 || res1 != res3){
                System.out.println("失败");
                System.out.println("--------------------------------------");
                arrays.printMatrix(m);
                System.out.println("--------------------------------------");
                System.out.println("方法1：" + res1);
                System.out.println("方法2：" + res2);
                System.out.println("方法3：" + res3);
                System.out.println("k：" + k);
                return;
            }
        }
        System.out.println("AC");
    }


    public static void main(String[] args) {
        test(10000, 9, 100);
//        int[][] m = {{0, 6}, {6, 0}};
//        System.out.println(tsp3(m, 1));
    }
}
