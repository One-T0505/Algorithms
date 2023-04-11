package Hard;

// leetCode548
// 给定一个有 n 个整数的数组 nums，如果能找到满足以下条件的三元组 (i, j, k) 则返回 true
//  1. 0 < i,  i + 1 < j,  j + 1 < k < n - 1
//  2. 子数组 (0, i - 1)， (i + 1, j - 1) ， (j + 1, k - 1) ， (k + 1, n - 1) 的和应该相等。
// 这里我们定义子数组(l, r)表示原数组从索引为l的元素开始至索引为r的元素。

// 数据规模
// n == nums.length
// 1 <= n <= 2000
// -10^6 <= nums[i] <= 10^6

public class SplitThreeEqualSum {

    // 这个题没有优化，直接上暴力解。
    public static boolean splitArray(int[] nums) {
        if (nums == null || nums.length < 7)
            return false;
        int N = nums.length;
        int[] lToR = new int[N];
        int[] rToL = new int[N];
        int s = 0;
        // lToR[i] 表示 nums[0~i-1]的累加和
        for (int i = 0; i < N; i++) {
            lToR[i] = s;
            s += nums[i];
        }
        s = 0;
        // rToL[i] 表示i+1～N-1的累加和
        for (int i = N - 1; i >= 0; i--) {
            rToL[i] = s;
            s += nums[i];
        }
        // 思路就是：先确定第一部分0～i-1，i是分界点，再确定最后一部分，j是分界点，有第一部分和最后一部分
        // 累加和相等。然后再从i+1～j-1再寻找一个切割点，使得两边累加和相等。
        // 枚举第一个分割点的位置  选择第一个分割点的位置i，要保证后面还有至少5个元素
        // 第一个分割点的左侧最少得有1个元素，所以第一个分割点最左只能从1开始
        for (int i = 1; i < N - 5; i++) {
            // 枚举最后一个分割点的位置  第三个分割点和第一个分割点之间至少得有3个元素
            for (int j = N - 2; j > i + 3; j--) {
                if (lToR[i] == rToL[j] && find(lToR, rToL, i, j))
                    return true;
            }
        }
        return false;
    }


    // L、R是已经确定的两个分割点
    private static boolean find(int[] lToR, int[] rToL, int L, int R) {
        // lToR[L]表示0～L-1的累加和，刚好就是第一部分的累加和，也是最终要找的目标
        int sum = lToR[L];
        int prefix = lToR[L + 1]; // 0~L的累加和
        int suffix = rToL[R - 1]; // R~N-1的累加和
        // 再枚举切割点
        for (int i = L + 2; i <= R - 2; i++) {
            int part1 = lToR[i] - prefix;
            int part2 = rToL[i] - suffix;
            if (part1 == part2 && part1 == sum)
                return true;
        }
        return false;
    }
}
