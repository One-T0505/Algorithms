package class16;

import java.util.Arrays;

// 给定一个正数数组arr，返回arr的子集不能累加出的最小正数
// 1) 正常怎么做?
// 2) 如果arr中肯定有1这个值，怎么做?

public class MinAbsentSubSeqSum {

    // 问题一
    // 解法和 SubSeqSumToK 用动态规划做的一样，只需要在那个dp表的最后一行，逐一遍历，找到第一个为false的值即可，
    // 唯一的区别是该题为正数，之前那个可正可负可0

    public static int minAbsentSubSeqSum(int[] arr) {
        if (arr == null || arr.length == 0)
            return 1;
        if (arr.length == 1)
            return arr[0] == 1 ? 2 : 1;
        int N = arr.length;
        int sum = 0;
        for (int j : arr) {
            sum += j;
        }
        boolean[][] dp = new boolean[N][sum + 1];
        dp[0][arr[0]] = true;
        for (int i = 1; i < N; i++) {
            for (int j = 1; j <= sum; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - arr[i] >= 0 && !dp[i][j])
                    dp[i][j] |= dp[i - 1][j - arr[i]];
            }
        }
        for (int j = 1; j <= sum; j++) {
            if (!dp[N - 1][j])
                return j;
        }
        return sum + 1;
    }
    // ------------------------------------------------------------------------------------------


    // 问题二   arr是正数数组，且里面必然有1
    public static int minNum(int[] arr) {
        if (arr == null || arr.length == 0)
            return 1;
        if (arr.length == 1)
            return arr[0] == 1 ? 2 : 1;
        Arrays.sort(arr);
        // 排完序后，arr[0]必然是1
        int range = 1;
        // range的含义：表示1～range这个范围上的所有数，该数组一定有个子集可以搞定。
        // 比如：range=100，就说明1～100中的任意一个数都可以搞定，假设来到了i位置，arr[i]=83，那么range可以更新为
        // 183，因为101～183这些数都可搞定。比如101，那就是从0..i-1拼出18即可，而range=100，就说明1～100都是可以
        // 拼出来的。同理，102～183都可以拼出。如果arr[i]=101，只需要让前面0..i-1拼出0即可。但是arr[i]=102，就不
        // 可以了，就算前面0..i-1拼出0，那也凑不出101。综上所述：arr[i] <= range + 1，range就可以更新，否则
        // 最小的不可由自己拼出的累加和就是range+1.
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > range + 1)
                return range + 1;
            else
                range += arr[i];
        }
        return range + 1;
    }
}
