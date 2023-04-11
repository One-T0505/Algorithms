package GreatOffer.class22;

// 给你一个整数数组 nums 和一个整数 k ，找出三个长度为 k 、互不重叠、且全部数字和（3 * k 项）最大的子数组，
// 并返回这三个子数组。以下标的数组形式返回结果，数组中的每一项分别指示每个子数组的起始位置（下标从 0 开始）。
// 如果有多个结果，返回字典序最小的一个。

public class _01_Code {

    // 先构造一个辅助数组help和原数组等长。help[i]表示：在原数组0..i范围上，必须以i结尾，来选取子数组，那么
    // 最大累加和是多少。有了help这个辅助数组，再构建dp数组，dp[i]表示：从0..i任意选取子数组能得到的最大累加和
    // 是多少

    public static int[] overlapping(int[] nums, int k) {
        if (nums == null || nums.length < 3)
            return null;
        int N = nums.length;
        // 因为有了k这个限制，那么就需要对help和dp两个辅助数组的含义进行修改：
        // help[i] 表示必须以i结尾且长度为k的子数组的累加和，那么help[0..k-2]是无效的，因为凑不齐k个元素
        // dp[i] 表示从0..i可任意选择长度为k的子数组的最大累加和。
        int[] LToR = assistLtoR(nums, k);
        int[] RToL = assistRtoL(nums, k);
        int[] range = new int[N];
        int sum = 0;
        for (int i = 0; i <= k - 1; i++)
            sum += nums[i];
        range[0] = sum;
        for (int i = k; i < N; i++) {
            sum = sum + nums[i] - nums[i - k];
            range[i - k + 1] = sum;
        }
        int a = 0, b = 0, c = 0;
        int max = Integer.MIN_VALUE;
        // 思路是：固定住中间的那k长度的子数组，中间这个是必选的，可以推断出中间这个子数组的起始点的范围，
        // 就是下面i的变化范围。确定了中间这个数组后，那么左右各选一个即可。
        for (int i = k; i <= N - 2 * k; i++) {
            int left = range[LToR[i - 1]];
            int mid = range[i];
            int right = range[RToL[i + k]];
            int all = left + mid + right;
            if (all > max) {
                max = all;
                a = LToR[i - 1];
                b = i;
                c = RToL[i + k];
            }
        }
        return new int[]{a, b, c};
    }


    // 下面两个assist方法的原型就是最下面的init方法，不过根据这道题目的要求需要进行相应的修改，我们让dp[i]
    // 表示：从0..i随意选择长度为k的子数组找出的最大累加和情况下，该子数组的开头位置。
    private static int[] assistLtoR(int[] nums, int k) {
        int N = nums.length;
        int[] help = new int[N];
        int[] dp = new int[N];
        int sum = 0;
        // 先凑齐0..k-2这k-1个元素的累加和
        for (int i = 0; i < k; i++)
            sum += nums[i];
        int max = sum;
        // 从0..k-1只能选出一个长度为k的子数组，所以其开头必然是0位置
        dp[k - 1] = 0;
        // 从k-1..N-1才可能向前找到k个元素的子数组。每次还要减去最前面元素的值
        for (int i = k; i < N; i++) {
            sum += nums[i] - nums[i - k];
            // 先让dp[i] = dp[i - 1]
            dp[i] = dp[i - 1];
            if (sum >= max) {  // 这个很关键：就是为了达到题目说的，有多个结果的时候返回字典序最小的
                max = sum;
                dp[i] = i - k + 1;
            }
        }
        return dp;
    }


    // 这个和上面的方法完全一样，只是方向不一样，dp[i]表示：
    // 从i..N-1里任意选择长度为k的子数组，决策出累加和最大的一种情况下，其开头索引是多少
    private static int[] assistRtoL(int[] nums, int k) {
        int N = nums.length;
        int[] dp = new int[N];
        int sum = 0;
        // 先凑齐N-k..N-1这k个元素的累加和
        for (int i = N - 1; i >= N - k; i--)
            sum += nums[i];
        dp[N - k] = N - k;
        int max = sum;
        // 从0..N-k才可能向后找到k个元素的子数组。每次还要减去最前面元素的值
        for (int i = N - k - 1; i >= 0; i--) {
            sum = sum + nums[i] - nums[i + k];
            dp[i] = dp[i + 1];
            if (sum > max) {
                max = sum;
                dp[i] = i;
            }
        }
        return dp;
    }


    // ============================================================================================
    // init方法就是生成两个辅助数组help和dp，但是最终只返回dp
    private static int[] init(int[] nums) {
        int N = nums.length;
        int[] help = new int[N];
        help[0] = nums[0];
        for (int i = 1; i < N; i++) {
            // 在规定了help[i]的定义后，help[i]只有两种情况：
            //  1.nums[i]作为单独一个元素
            //  2.如果要向左扩，那么就需要用到help[i-1]
            help[i] = Math.max(nums[i], nums[i] + help[i - 1]);
        }
        // 再来填写dp数组  dp[i]表示从0..i中的子数组的最大累加和，不要求必须以i结尾
        int[] dp = new int[N];
        dp[0] = nums[0];
        // 那么dp[i]就有两种情况：
        //  1.必须以i结尾：那就是help[i]的值
        //  2.不以i结尾：那就是在0..i-1上任意选子数组，就是dp[i-1]的值
        for (int i = 1; i < N; i++) {
            dp[i] = Math.max(help[i], dp[i - 1]);
        }
        return dp;
    }


    // init方法可以简化  只用dp数组，help数组可以用简单变量来记录
    private static int[] initV2(int[] nums) {
        int N = nums.length;
        int[] dp = new int[N];
        dp[0] = nums[0];
        int pre = nums[0];
        for (int i = 1; i < N; i++) {
            dp[i] = Math.max(nums[i] + Math.max(0, pre), dp[i - 1]);
            pre = Math.max(nums[i], nums[i] + pre);
        }
        return dp;
    }
    // ============================================================================================
    // 这两个init方法是针对选取子数组无长度限制的时候使用的，但是现在有k这个长度限制，所以要对init方法改造，但是
    // 核心思想都是一样的，需要先有help，再根据help生成dp。


    public static void main(String[] args) {
    }
}
