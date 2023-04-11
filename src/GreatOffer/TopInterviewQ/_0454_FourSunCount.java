package GreatOffer.TopInterviewQ;

import java.util.HashMap;

// 给你四个整数数组 nums1、nums2、nums3 和 nums4 ，数组长度都是 n ，请你计算有多少个元组
// (i, j, k, l) 能满足：
//    0 <= i, j, k, l < n
//    nums1[i] + nums2[j] + nums3[k] + nums4[l] == 0

public class _0454_FourSunCount {

    // 思路：使用一张哈希表，先让nums1和nums2的两个元素配对，生成所有的和，并用哈希表记录下来
    // key->和   val->出现了几次  然后利用这张表，再去遍历nums3和nums4，让nums3的每个元素都去和nums4中的
    // 每个元素配对，组成一个sum，然后在表中去查是否存在 -sum，如果存在，那么-sum对应的val就是一部分结果。

    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        int N = nums1.length;
        int sum = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum = nums1[i] + nums2[j];
                map.put(sum, map.getOrDefault(sum, 0) + 1);
            }
        }

        int res = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum = nums3[i] + nums4[j];
                res += map.getOrDefault(-sum, 0);
            }
        }
        return res;
    }
}
