package DynamicProgramming;

// 给定一个正数数组arr, 请把arr中所有的数分成两个集合，尽量让两个集合的累加和接近
// 返回: 最接近的情况下，较小集合的累加和

// 分析一下：可以先求出整个数组的累加和sum，其实问题就是从这个数组中选取一些数，使得他们的和 <= sum/2。 不要
// 超过 sum/2 就行。 尽量接近sum/2

public class SplitSetQ1 {
    // 暴力递归
    public static int splitSet(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        int sum = 0;
        for (int elem : arr)
            sum += elem;
        return process(arr, 0, sum / 2);
    }

    // arr[index...]可以自由选择，请返回累加和尽量接近rest,但不能超过rest的情况下，最接近的累加和是多少?
    private static int process(int[] arr, int index, int rest) {
        if (index == arr.length)
            return 0;
        // rest >= 0 && index != arr.length
        int p1 = process(arr, index + 1, rest);  // 不选择arr[index]
        int p2 = 0;
        if (arr[index] <= rest)
            p2 = arr[index] + process(arr, index + 1, rest - arr[index]);  // 选择arr[index]
        return Math.max(p1, p2);
    }
    // ====================================================================================================


    // 改成动态规划
    public static int dpV1(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        int sum = 0;
        for (int elem : arr)
            sum += elem;
        int aim = sum / 2;
        int N = arr.length;
        int[][] cache = new int[N + 1][aim + 1];
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int p1 = cache[index + 1][rest];  // 不选择arr[index]
                int p2 = 0;
                if (arr[index] <= rest)
                    p2 = arr[index] + cache[index + 1][rest - arr[index]];  // 选择arr[index]
                cache[index][rest] = Math.max(p1, p2);
            }
        }
        return cache[0][aim];
    }


    public static void main(String[] args) {
        int[] arr = {100, 2, 3, 5, 91};
        System.out.println(splitSet(arr));
        System.out.println(dpV1(arr));
    }
}
