package Recurse;

import java.util.Arrays;

public class PokerGame {

    // 给定一个整型数组arr，代表数值不同的纸牌排成--条线,玩家A和玩家B依次拿走每张纸牌,
    // 规定玩家A先拿，玩家B后拿,但是每个玩家每次只能拿走最左或最右的纸牌,玩家A和玩家B都绝顶聪明。请返回最后获胜者的分数。
    // A,B玩家都知道整个数组的值。eg：
    // 80 100 4 1 是该数组，A先手拿肯定会拿1，不会拿80，因为拿完80，100就会暴露给别人，B肯定会拿到。

    public static int winnerV1(int[] arr){
        if (arr == null || arr.length == 0)
            return -1;
        // first(arr, 0, arr.length - 1)    表示 A 获得的分数
        // second(arr, 0, arr.length - 1)   表示 B 获得的分数
        return Math.max(first1(arr, 0, arr.length - 1), second1(arr, 0, arr.length - 1));
    }

    // 先手函数：从arr[L]~arr[R]做选择，返回在该数组L～R范围内做选择能得到的最大分数
    private static int first1(int[] arr, int L, int R){
        if (L == R) // 只剩一张卡牌
            return arr[L];
        // 第一个参数表示拿掉最左边的卡牌，第二个参数表示拿掉最右边的卡牌
        return Math.max(arr[L] + second1(arr, L + 1, R), arr[R] + second1(arr, L, R - 1));
    }

    // 后手函数：从arr[L]~arr[R]做选择，返回在该数组L～R范围内做选择能得到的最大分数
    private static int second1(int[] arr, int L, int R) {
        if (L == R) // 只剩一张牌，作为后手肯定拿不到
            return 0;
        // A,B同样面对L～R的卡牌时，作为后手，一定面临到的是先手选择的对后手更不好的情况
        // 第一个参数是：对手拿走了L，后手能获得的分数就是后手在L+1～R做先手得到的分数
        return Math.min(first1(arr, L + 1, R), first1(arr, L, R - 1));
    }
    // ================================================================================================


    // 版本1的方法就是最贴合自然智慧的暴力递归方法，不难发现递归方法的可变参数只有left，right这两个，我们来分析下是否
    // 有重复计算。这里用一个具体例子来分析，假如arr有8个元素，用f表示先手first函数，s表示second后手函数。
    // 根据暴力递归的实现，可以发现：
    //             f(0,7)
    //            /       \
    //        s(1,7)      s(0,6)
    //        /   \        /    \
    //    f(2,7) f(1,6)  f(1,6) f(0,5)              是有重复计算的，所以优化才有必要
    //
    // left 范围：0~arr.length-1   right 范围：0~arr.length-1
    // 但是这个题目是稍微有点难改的，因为它的递归过程还依赖别的递归过程，不只依靠自己，是需要两种递归才能运作的。
    // 所以，缓存表得申请两份，一份对应于一种递归过程


    // 版本2的方法实际上就是减去重复计算的递归，称作记忆化搜索
    public static int winnerV2(int[] arr){
        if (arr == null || arr.length == 0)
            return -1;
        int N = arr.length;
        int[][] firstCache = new int[N][N];  // 用于记录先手递归的缓存表
        int[][] secondCache = new int[N][N];  // 用于记录后手递归的缓存表
        // 将缓存表所有值填为-1，这样就可以根据是否为-1判断该结果是否计算过
        for (int row = 0; row < N; row++) {
            Arrays.fill(firstCache[row], -1);
            Arrays.fill(secondCache[row], -1);
        }
        int score1 = first2(arr, 0, N - 1, firstCache, secondCache);
        int score2 = second2(arr, 0, N - 1, firstCache,secondCache);
        return Math.max(score1, score2);
    }

    private static int first2(int[] arr, int L, int R, int[][] firstCache, int[][] secondCache) {
        if (firstCache[L][R] != -1)
            return firstCache[L][R];
        int res = arr[L];
        if (L != R) {
            int score1 = second2(arr, L + 1, R, firstCache, secondCache) + arr[L];
            int score2 = second2(arr, L, R - 1, firstCache, secondCache) + arr[R];
            res = Math.max(score1, score2);
        }
        // 即使L==R，那结果也是对的
        firstCache[L][R] = res;
        return res;
    }

    private static int second2(int[] arr, int L, int R, int[][] firstCache, int[][] secondCache) {
        if (secondCache[L][R] != -1)
            return secondCache[L][R];
        int res = 0;
        if (L != R){
            int score1 = first2(arr, L + 1, R, firstCache, secondCache);
            int score2 = first2(arr, L, R - 1, firstCache, secondCache);
            res = Math.min(score1, score2);
        }
        secondCache[L][R] = res;
        return res;
    }
    // ================================================================================================


    // 动态规划  根据暴力递归的依赖关系改写成动态规划
    public static int winnerV3(int[] arr){
        if (arr == null || arr.length == 0)
            return -1;
        int N = arr.length;
        // 两张矩阵的昨下半个区域都没用，因为L>=R才有意义
        int[][] first = new int[N][N];  // 先手动态缓存表，行表示L的位置，列表示R的位置
        int[][] second = new int[N][N]; // 后手动态缓存表

        for (int i = 0; i < N; i++)
            first[i][i] = arr[i];
        // 经过递归调用关系可以发现，在矩阵的缓存中，是用一条对角线去推测另一条对角线。所以，整个的转移方程
        // 的遍历形式是以对角线的形式遍历的。
        for (int startCol = 1; startCol < N; startCol++) {
            int L = 0, R = startCol;
            while (L < N && R < N){
                first[L][R] = Math.max(arr[L] + second[L + 1][R], arr[R] + second[L][R - 1]);
                second[L][R] = Math.min(first[L + 1][R], first[L][R - 1]);
                L++;
                R++;
            }
        }
        return Math.max(first[0][N - 1], second[0][N - 1]);
    }

    public static void main(String[] args) {
        System.out.println(winnerV1(new int[]{80, 100, 4, 1, 10, 60, 29, 48}));
        System.out.println(winnerV2(new int[]{80, 100, 4, 1, 10, 60, 29, 48}));
        System.out.println(winnerV3(new int[]{80, 100, 4, 1, 10, 60, 29, 48}));
    }
}
