package LeetCode;

import java.util.Queue;

/**
 * ymy
 * 2023/4/3 - 19 : 16
 **/


// leetCode718  最长重复子数组
// 给两个整数数组 nums1 和 nums2 ，返回 两个数组中 公共的 、长度最长的子数组的长度 。


public class LongestRepeatedSubArr {
    public static int findLength(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0 || nums2 == null || nums2.length == 0)
            return 0;
        int N = nums1.length;
        int M = nums2.length;
        int res = 0;
        for (int i = 0; i < N; i++) {
            int len = Math.min(M, N - i);
            int maxLen = longestSubArr(nums1, i, nums2, 0, len);
            res = Math.max(res, maxLen);
        }

        for (int i = 0; i < M; i++) {
            int len = Math.min(N, M - i);
            int maxLen = longestSubArr(nums1, 0, nums2, i, len);
            res = Math.max(res, maxLen);
        }
        return res;
    }


    private static int longestSubArr(int[] nums1, int i, int[] nums2, int j, int len) {
        int res = 0, k = 0;
        for (int l = 0; l < len; l++) {
            if (nums1[i + l] == nums2[j + l])
                k++;
            else
                k = 0;
            res = Math.max(res, k);
        }
        return res;
    }
}
