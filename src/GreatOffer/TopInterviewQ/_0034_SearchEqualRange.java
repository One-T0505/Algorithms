package GreatOffer.TopInterviewQ;


// 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。
// 如果数组中不存在目标值 target，返回 [-1, -1]。
//你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。

public class _0034_SearchEqualRange {

    public int[] searchRange(int[] nums, int target) {
        if (nums == null || nums.length == 0)
            return new int[]{-1, -1};
        if (nums.length == 1)
            return nums[0] == target ? new int[]{0, 0} : new int[]{-1, -1};
        int N = nums.length;
        // 如果<target的最右的元素的下一个 != target 说明整个数组里都没有target了
        int left = mostRightL(nums, 0, N - 1, target) + 1;
        if (left == N || nums[left] != target)
            return new int[]{-1, -1};
        int right = mostRightL(nums, 0, N - 1, target + 1);

        return new int[]{left, right};
    }


    // 找出nums中<target中最右的位置
    private int mostRightL(int[] nums, int L, int R, int target) {
        int pos = -1;
        int mid = ((R - L) >> 1);
        while (L <= R) {
            if (nums[mid] < target) {
                pos = mid;
                L = mid + 1;
            } else {
                R = mid - 1;
            }
            mid = L + ((R - L) >> 1);
        }
        return pos;
    }

}
