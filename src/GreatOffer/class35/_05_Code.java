package GreatOffer.class35;

// 网易
// 给定一个正数数组arr, 表示每个小朋友的得分
// 任何两个相邻的小朋友，如果得分一样，怎么分糖果无所谓，但如果得分不一样，分数大的一定要比分数少的多拿一些
// 假设所有的小朋友坐成一个环形， 返回在不破坏上一条规则的情况下，需要的最少糖果数

public class _05_Code {


    // 这个问题的原型是小朋友分糖果问题。不过问题原型是不带环的，所以我们需要先遍历一下数组，找到最小的那个数，
    // 这个值分到的糖果数一定是1，不会受到影响。
    // 这道题需要生成一个比原数组长度大1的新数组，两端填上找到的最小值，剩余元素按找顺序填。
    // 比如：[4, 2, 1, 6, 3, 7, 5]  -->  [1, 6, 3, 7, 5, 4, 2, 1]
    // 剩下思路和原型一模一样

    public static int minCandy(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length == 1)
            return 1;
        int N = arr.length;
        int pos = 0;  // 记录最小值位置
        for (int i = 1; i < N; i++)
            pos = arr[i] < arr[pos] ? i : pos;
        int[] help = new int[N + 1];
        help[0] = arr[pos];
        help[N] = arr[pos];
        System.arraycopy(arr, pos + 1, help, 1, N - 1 - pos);
        System.arraycopy(arr, 0, help, N - pos, pos);

        int[] left = new int[N + 1];
        left[0] = 1;
        for (int i = 1; i <= N; i++)
            left[i] = help[i] > help[i - 1] ? (left[i - 1] + 1) : 1;

        int[] right = new int[N + 1];
        right[N] = 1;
        for (int i = N - 1; i >= 0; i--)
            right[i] = help[i] > help[i + 1] ? (right[i + 1] + 1) : 1;

        int res = 0;
        for (int i = 0; i < N; i++)
            res += Math.max(left[i], right[i]);

        return res;
    }

}
