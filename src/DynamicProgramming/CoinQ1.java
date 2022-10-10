package DynamicProgramming;


// arr是货币数组，其中的值都是正数。再给定一个正数aim。每个值都认为是一张货币,即便是值相同的货币也认为每一张都是不同的,
// 返回组成aim的方法数.
// 例如: arr= {1,1,1}，aim= 2。 第0个和第1个能组成2，第1个和第2个能组成2，第0个和第2个能组成2.
//      一共就3种方法，所以返回3

public class CoinQ1 {

    // 暴力递归法
    public static int coinV1(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        return process1(arr, 0, aim);
    }

    // 该方法表示从arr[index...]处开始做决定，返回从index开始到最后一个货币能组成aim的方法数
    // 0..index-1处的货币已经做好决定了，不用管
    private static int process1(int[] arr, int index, int aim) {
        if (aim < 0)
            return 0;
        if (index == arr.length)
            return aim == 0 ? 1 : 0;
        int p1 = process1(arr, index + 1, aim - arr[index]);
        int p2 = process1(arr, index + 1, aim);
        return p1 + p2;
    }
    // ================================================================================================


    // 改成动态规划
    public static int dp(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        int N = arr.length;
        int[][] cache = new int[N + 1][aim + 1];
        cache[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int p1 = rest - arr[index] < 0 ? 0 : cache[index + 1][rest - arr[index]];
                int p2 = cache[index + 1][rest];
                cache[index][rest] = p1 + p2;
            }
        }
        return cache[0][aim];
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 1, 1, 2};
        int aim = 6;
        System.out.println(coinV1(arr, aim));
        System.out.println(dp(arr, aim));
    }
}
