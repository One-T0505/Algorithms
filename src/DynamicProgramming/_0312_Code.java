package DynamicProgramming;

// 有 n 个气球，编号为0 到 n - 1，每个气球上都标有一个数字，这些数字存在数组 nums 中。
// 现在要求你戳破所有的气球。戳破第 i 个气球，你可以获得 nums[i - 1] * nums[i] * nums[i + 1] 枚硬币。
// 这里的 i - 1 和 i + 1 代表和 i 相邻的两个气球的序号。如果 i - 1或 i + 1 超出了数组的边界，
// 那么就当它是一个数字为 1 的气球。求所能获得硬币的最大数量。

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class _0312_Code {

    public static int maxCoins(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        if (nums.length == 1)
            return nums[0];
        int N = nums.length;
        // 构造一个新数组，将虚拟的左右边界加上，这样写代码时就能少写很多逻辑判断
        int[] val = new int[N + 2];
        System.arraycopy(nums, 0, val, 1, N);
        val[0] = val[N + 1] = 1;
        // 转移如何写？
        // 假设这个区间是个开区间，最左边索引i，最右边索引j，我们只能戳爆 i 和 j 之间的气球，i 和 j 不要戳
        // DP思路是这样的，就先别管前面是怎么戳的，你只要管这个区间最后一个被戳破的是哪个气球，这最后一个被戳爆
        // 的气球假如是 k
        // 那么戳爆它可以得到的收益是：dp[i][k] + val[i] * val[k] * val[j] + dp[k][j]
        // val[i] * val[k] * val[j]  这个是戳爆气球k得到的直接收益，题目说的，因为k是这个区间里最后一个被戳爆的
        // 所以，区间内仅剩k了，之前被戳爆的气球的位置自动消除了，导致i、k、j在数组中虽然不相连，但逻辑上已经相连了，
        // 这也是题目给的实例表达的，被戳爆的气球的位置是不会保留的。
        // 由于最后一个k的存在，就将(i,j)内部的气球分成了(i,k) (k,j)两个区间，这两个区间的气球的爆炸对另一个区间
        // 都不会产生影响，所以我们呢可以安心分成两部分。dp[i][k]就是将左边这个区间气球全部戳爆得到的最大收益，
        // dp[k][j]就是将右边这个区间气球全部戳爆得到的最大收益，于是问题就被分解了。
        // 所以，dp[i][j]的值，我们可以枚举最后一个删除的是哪一个气球来获得
        // 为什么一开始设定这个开区间的设定呢？可能是由于val数组的左右边界是不可能真正删除的

        // 主对角线及左下部分都没用
        int[][] dp = new int[N + 2][N + 2]; // dp[i][j]表示戳爆(i,j)这个开区间内所有气球的最大收益
        // 像[0,1] [1,2] 这样的位置都填0，因为是开区间所以里面没有一个元素
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i + 2; j < N + 2; j++) {
                // 枚举开区间内最后删除的是哪一个气球
                for (int k = i + 1; k < j; k++) {
                    int sum = val[i] * val[k] * val[j];
                    sum += dp[i][k] + dp[k][j];
                    dp[i][j] = Math.max(dp[i][j], sum);
                }
            }
        }
        return dp[0][N + 1];
    }

}
