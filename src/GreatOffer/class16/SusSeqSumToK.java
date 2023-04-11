package class16;


// 给定一个有正、有负、有0的数组arr，给定一个整数k，返回arr的子集是否能累加出k
// 1) 正常怎么做?
// 2) 如果arr中的数值很大，但是arr的长度不大，怎么做?

import java.util.HashSet;

public class SusSeqSumToK {

    // 问题一
    // 这很像背包问题。先来想累加和的范围是多少：最小就是数组中所有负数的累加和，最大就是数组中所有正数的累加和，不可能
    // 不在这个范围内。
    public static boolean isSumQ1(int[] arr, int k) {
        if (k == 0)
            return true;
        if (arr == null || arr.length == 0)
            return false;
        // 先统计两个界限
        int min = 0, max = 0;
        for (int elem : arr) {
            min += Math.min(elem, 0);
            max += Math.max(elem, 0);
        }
        if (k < min || k > max)
            return false;
        int N = arr.length;
        // dp[i][j]的含义：在arr[0..i]范围上，是否有子集能凑出j；因为min可能为负数，如果有负数则让0列表示该负数，
        // 自动平移。比如min=-3，那么用第0列表示-3，所以计算下标时需要转换。
        boolean[][] dp = new boolean[N][max - min + 1];
        // 第0行有两个位置肯定能搞定：1> arr[0]的位置，因为列数表示arr[0]的值可能平移了，所以要转换下标
        dp[0][arr[0] - min] = true;
        // 2> dp[0][-min]=true , 因为-min就是转换后的表示0的下标，只要是空集就可以搞定0.
        dp[0][-min] = true;
        for (int i = 1; i < N; i++) {
            for (int j = min; j <= max; j++) {
                // 第一种情况：arr[i]不参与，arr[0..i-1]就可以搞定j
                // 第二种情况：arr[i]参与，arr[0..i-1]就要搞定j-arr[i]
                dp[i][j - min] = dp[i - 1][j - min];
                int next = j - arr[i] - min;
                // 如果dp[i][j - min]=dp[i - 1][j - min]为真，说明0..i-1能搞定j
                // 如果不行，那么i就必须参与，那就要看0..i-1能否搞定j-arr[i]了。我们需要 min <= j-arr[i] <= max
                dp[i][j - min] |= (next >= 0 && next <= max - min && dp[i - 1][next]);
            }
        }
        return dp[N - 1][k - min];
    }
    // ================================================================================================


    // 问题二
    // 如果arr中的数值很大，那么用上面的方法去解决，会让动态缓存表巨大。但实际上整个数组大小可能就二十多个元素。
    // 所以不能再用上面的方法了。此时，应该用分治。将整个数组均分成两半，分别取收集左部分、右部分所有可能的累加和。
    // 最后整合的时候有三种逻辑：只用左边的能否凑出k；只用右边的能否凑出k；左右两边能否凑出k

    public static boolean isSumQ2(int[] arr, int k) {
        if (k == 0)
            return true;
        if (arr == null || arr.length == 0)
            return false;
        if (arr.length == 1)
            return arr[0] == k;
        int N = arr.length;
        // 两个集合，分别用于收集两半部分所有可能的累加和
        HashSet<Integer> left = new HashSet<>();
        HashSet<Integer> right = new HashSet<>();
        f(arr, 0, N >> 1, 0, left); // 收集前半部分累加和
        f(arr, N >> 1, N, 0, right);     // 收集后半部分累加和

        // 这个for循环就已经包含了上面所说的三种逻辑。因为f函数在跑的时候，是会收集到所有元素都不选的情况，也就是说
        // left、right都有0的存在，也就表示了只用右半部分和只用左半部分的两个逻辑。
        for (int elem : left) {
            if (right.contains(k - elem))
                return true;
        }
        return false;
    }


    // 0..i-1位置已经做好决定了，不用管。此时来到了i位置，end是终止位置。
    // pre是0..i-1上做完决定产生的累加和
    private static void f(int[] arr, int i, int end, int pre, HashSet<Integer> res) {
        if (i == end)
            res.add(pre);
        else {
            f(arr, i + 1, end, pre, res);
            f(arr, i + 1, end, pre + arr[i], res);
        }
    }
}
