package SlidingWindow;

// 给你一个整数数组 nums 和一个整数 k ，判断数组中是否存在两个不同的索引 i 和 j，
// 满足 nums[i] == nums[j] 且 abs(i - j) <= k 。如果存在，返回 true ；否则，返回 false。

import java.util.HashSet;

public class _0219_Code {

    // 这个题目设定时，说明了k有可能比数组长度更大
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        if(nums == null || nums.length == 0 || k < 1)
            return false;
        int N = nums.length;
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < N; i++) {
            if (set.contains(nums[i]))
                return true;
            set.add(nums[i]);
            if (i >= k)
                set.remove(nums[i - k]);
        }
        return false;
    }


}
