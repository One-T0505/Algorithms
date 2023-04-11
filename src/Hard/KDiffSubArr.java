package Hard;

// leetCode992
// 给定一个正整数数组 nums和一个整数 k ，返回 nums 中 「好子数组」 的数目。
// 如果 nums 的某个子数组中不同整数的个数恰好为 k，则称 nums 的这个连续、不一定不同的子数组为 「好子数组 」。
// 例如，[1,2,3,1,2] 中有 3 个不同的整数：1，2，以及 3。
// 1 <= nums.length <= 2 * 10^4
// 1 <= nums[i], k <= nums.length

import java.util.HashMap;

public class KDiffSubArr {

    // 有没有想到用滑动窗口？窗口的右边界不断移动，窗口内包含的不同整数的个数不会减少，所以这是一种隐含的单调关系。
    // 但这里的窗口得修改成两个窗口。比如数组：2 2 2 1 1 3 3 3 4  以6位置的3作为子数组结尾去收集k==3的子数组，
    // 我们需要两个窗口：一个是向左能找到的最远的k-1种整数的子数组边界，这里是3位置的1，因为再向左就到了2，这样子数组里
    // 就有3种数了；另一个是向左能找到的最远的k种整数的子数组的边界，这里是0位置的2；找到这两个边界后，其差值就是以当前
    // 元素为子数组结尾能找到的子数组的个数。然后就让下一个元素当子数组结尾，当确定了新结尾后，修改两个边界的值，使其保持
    // 相同含义，继续收集答案。


    // 上面讲的两个边界我们需要用到哈希表来维持。 key: 某个整数   value: 出现了多少次
    // 因为规定1 <= nums[i] <= nums.length  所以可以用一个N+1长度的数组来进行统计
    public static int subarrayWithKDistinct(int[] nums, int k) {
        int N = nums.length;
        int[] preC = new int[N + 1]; // 用于收集k-1种整数子数组的窗口
        int[] curC = new int[N + 1]; // 用于收集k种整数子数组的窗口
        int preK = 0;  // k-1的窗口内收集了多少种整数
        int curK = 0;  // k的窗口内收集了多少种整数
        int preLeft = 0; // 以当前元素为子数组结尾向左能找到的最远的包含k-1种整数的边界
        int curLeft = 0; // 以当前元素为子数组结尾向左能找到的最远的包含k种整数的边界
        int res = 0;

        // 遍历
        for (int r = 0; r < N; r++) {
            // 如果当前来的数是一个新数
            if (preC[nums[r]] == 0)
                preK++;
            if (curC[nums[r]] == 0)
                curK++;
            preC[nums[r]]++;
            curC[nums[r]]++;
            // 如果k-1的窗口收集到了k种不同的整数，那就要让其左边界preLfet不断向右移动，丢弃数，直到种类数为k-1
            // 此时就是以当前数为子数组结尾能找到的最左的k-1种整数的边界
            while (preK == k) {
                if (preC[nums[preLeft]] == 1) // 那刚好移除完后就要让种类数-1
                    preK--;
                preC[nums[preLeft++]]--; // 词频-1
            }
            while (curK > k) {
                if (curC[nums[curLeft]] == 1)
                    curK--;
                curC[nums[curLeft++]]--;
            }
            // 收集答案
            // 这是每轮比执行的代码。一开始窗口内的元素都没达到k个，不可能有答案形成，所以preLeft和curLeft初始值为0
            // 收集答案为0
            res += preLeft - curLeft;
        }
        return res;
    }
    // ====================================================================================================


    // 方法2
    // 找出所有包含的整数种类<=k的子数组a个     然后找出所有包含的整数种类<=k-1的子数组b个
    // a - b 就是恰好包含k种整数的所有子数组个数

    public static int subarrayWithKDistinct2(int[] nums, int k) {
        return window(nums, k) - window(nums, k - 1);
    }


    // 在nums上找出所有包含的整数种类<=k的子数组个数
    // 这个问题就是最简单的单个滑动窗口。以每个元素作为子数组的左边界L，然后向右扩到刚好包含k+1种整数的位置R
    // R-L 就是以当前元素为开头能找到的所有包含<=k种整数的子数组数量
    private static int window(int[] nums, int k) {
        int res = 0;
        int L = 0;
        int kinds = 0; // 已收集了多少种整数
        HashMap<Integer, Integer> dp = new HashMap<>();  // 词频统计
        for (int R = 0; R < nums.length; R++) {
            if (dp.getOrDefault(nums[R], 0) == 0)
                kinds++;
            dp.put(nums[R], dp.getOrDefault(nums[R], 0) + 1);
            while (kinds > k) {
                dp.put(nums[L], dp.get(nums[L]) - 1);
                if (dp.get(nums[L]) == 0)
                    kinds--;
                L++;
            }
            res += R - L + 1;
        }
        return res;
    }
}
