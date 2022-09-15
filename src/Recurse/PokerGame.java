package Recurse;

public class PokerGame {

    // 给定一个整型数组arr，代表数值不同的纸牌排成--条线,玩家A和玩家B依次拿走每张纸牌,
    // 规定玩家A先拿，玩家B后拿,但是每个玩家每次只能拿走最左或最右的纸牌,玩家A和玩家B都绝顶聪明。请返回最后获胜者的分数。
    // A,B玩家都知道整个数组的值。eg：
    // 80 100 4 1 是该数组，A先手拿肯定会拿1，不会拿80，因为拿完80，100就会暴露给别人，B肯定会拿到。

    // 先手函数：从arr[L]~arr[R]做选择，返回在该数组L～R范围内做选择能得到的最大分数
    public static int first(int[] arr, int L, int R){
        if (L == R) // 只剩一张卡牌
            return arr[L];
        // 第一个参数表示拿掉最左边的卡牌，第二个参数表示拿掉最右边的卡牌
        return Math.max(arr[L] + second(arr, L + 1, R), arr[R] + second(arr, L, R - 1));
    }

    // 后手函数：从arr[L]~arr[R]做选择，返回在该数组L～R范围内做选择能得到的最大分数
    private static int second(int[] arr, int L, int R) {
        if (L == R) // 只剩一张牌，作为后手肯定拿不到
            return 0;
        // A,B同样面对L～R的卡牌时，作为后手，一定面临到的是先手选择的对后手更不好的情况
        // 第一个参数是：对手拿走了L，后手能获得的分数就是后手在L+1～R做先手得到的分数
        return Math.min(first(arr, L + 1, R), first(arr, L, R - 1));
    }

    public static int winner(int[] arr){
        if (arr == null || arr.length == 0)
            return -1;
        // first(arr, 0, arr.length - 1)    表示 A 获得的分数
        // second(arr, 0, arr.length - 1)   表示 B 获得的分数
        return Math.max(first(arr, 0, arr.length - 1), second(arr, 0, arr.length - 1));
    }

    // 动态规划改暴力递归
    public static int dp(int[] arr){
        if (arr == null || arr.length == 0)
            return -1;
        int N = arr.length;
        // 两张矩阵的昨下半个区域都没用，因为L>=R才有意义
        int[][] first = new int[N][N];  // 先手动态缓存表，行表示L的位置，列表示R的位置
        int[][] second = new int[N][N]; // 后手动态缓存表

        for (int i = 0; i < N; i++)
            first[i][i] = arr[i];

        for (int i = 1; i < N; i++) {
            int L = 0, R = i;
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
        System.out.println(winner(new int[]{80, 100, 4, 1, 10, 30}));
        System.out.println(dp(new int[]{80, 100, 4, 1, 10, 30}));
    }
}
