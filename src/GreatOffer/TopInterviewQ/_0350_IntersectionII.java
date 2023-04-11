package GreatOffer.TopInterviewQ;

// 给你两个整数数组 nums1 和 nums2 ，请你以数组形式返回两数组的交集。返回结果中每个元素出现的次数，
// 应与元素在两个数组中都出现的次数一致（如果出现次数不一致，则考虑取较小值）。可以不考虑输出结果的顺序。

import java.util.Arrays;
import java.util.HashMap;

public class _0350_IntersectionII {

    public int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> dp = new HashMap<>();
        for (int elem : nums1)
            dp.put(elem, dp.getOrDefault(elem, 0) + 1);

        int[] list = new int[nums1.length];
        int i = 0;
        for (int elem : nums2){
            if (dp.containsKey(elem) && dp.get(elem) > 0){
                list[i++] = elem;
                dp.put(elem, dp.get(elem) - 1);
            }
        }
        return Arrays.copyOfRange(list, 0, i);
    }

}
