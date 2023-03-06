package Sort;

// 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。
// 你需要返回给定数组中的重要翻转对的数量。

public class _0493_Code {

    // 很明显是一个利用归并排序解决的问题
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2)
            return 0;
        return f(nums, 0, nums.length - 1);
    }

    private int f(int[] nums, int L, int R) {
        if (L == R)
            return 0;
        int mid = L + ((R - L) >> 1);
        return f(nums, L, mid) + f(nums, mid + 1, R) + merge(nums, L, mid, R);
    }

    private int merge(int[] nums, int L, int M, int R) {
        int N = R - L + 1;
        int l = L, r = M + 1;
        int res = 0;
        while (l <= M){
            while (r <= R && (long) nums[l] > ((long) nums[r]) << 1)
                r++;
            res += r - M - 1;
            l++;
        }
        // 合并
        l = L;
        r = M + 1;
        int i = 0;
        int[] help = new int[N];
        while (l <= M && r <= R){
            help[i++] = nums[l] <= nums[r] ? nums[l++] : nums[r++];
        }
        while (l <= M){
            help[i++] = nums[l++];
        }
        while (r <= R){
            help[i++] = nums[r++];
        }
        System.arraycopy(help, 0, nums, L, N);
        return res;
    }
}
