package DynamicProgramming;

import java.util.Arrays;

// 给定一个正数数组arr，请把arr中所有的数分成两个集合.如果arr长度为偶数，两个集合包含数的个数要一样多.
// 如果arr长度为奇数, 两个集合包含数的个数必须只差一个。请尽量让两个集合的累加和接近。
// 返回：最接近的情况下，较小集合的累加和(这里的较小不是指集合中元素的数量)

// 在Q1中已经会了没有数量限制的尝试，现在只是加了一个数量限制

public class SplitSetQ2 {
    // 暴力递归尝试
    public static int splitSet(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        int sum = 0;
        for (int elem : arr)
            sum += elem;
        if ((arr.length & 1) == 0)  // 长度为偶数
            return process(arr, 0, arr.length / 2, sum / 2);
        else { // 长度为奇数
            return Math.max(process(arr, 0, arr.length / 2, sum / 2),
                    process(arr, 0, arr.length / 2 + 1, sum / 2));
        }
    }

    // 从index处开始做选择，还可以选择picks个数，组成不超过rest但最接近rest的累加和。
    private static int process(int[] arr, int index, int picks, int rest) {
        if (index == arr.length)
            return rest == 0 ? 0 : -1;
        else {
            int p1 = process(arr, index + 1, picks, rest);
            int p2 = -1;
            int next = -1;
            if (arr[index] <= rest)
                next = process(arr, index + 1, picks - 1, rest- arr[index]);
            if (next != -1)
                p2 = arr[index] + next;
            return Math.max(p1, p2);
        }
    }
    // ====================================================================================================


    // 改成动态规划,这里有三个可变参数，他们的范围一定要搞清楚
    public static int dpV1(int[] arr){
        if (arr == null || arr.length < 2)
            return 0;
        int sum = 0;
        for (int elem : arr)
            sum += elem;
        int N = arr.length;
        int P = (N + 1) >> 1;   // 挑选的元素数量要么就是P，要不就是P-1
        int R = sum >> 1;
        int[][][] cache = new int[N + 1][P + 1][R + 1];
        for (int pick = 0; pick <= P; pick++) {
            for (int rest = 1; rest <= R; rest++)
                cache[N][pick][rest] = -1;
        }
        for (int index = N - 1; index >= 0; index--) {
            for (int pick = 0; pick <= P; pick++) {
                for (int rest = 0; rest <= R; rest++) {
                    int p1 = cache[index + 1][pick][rest];
                    int p2 = -1;
                    int next = -1;
                    if (arr[index] <= rest && pick - 1 >= 0)
                        next = cache[index + 1][pick - 1][rest- arr[index]];
                    if (next != -1)
                        p2 = arr[index] + next;
                    cache[index][pick][rest] = Math.max(p1, p2);
                }
            }
        }
        if ((N & 1) == 0)  // 长度为偶数
            return cache[0][N / 2][R];
        else  // 长度为奇数
            return Math.max(cache[0][N / 2][R], cache[0][N / 2 + 1][R]);
    }

    public static void main(String[] args) {
        int[] arr = {3, 1, 5, 10, 6, 11, 4};
        System.out.println(splitSet(arr));
        System.out.println(dpV1(arr));
    }
}
