package LeetCode;

import java.util.ArrayList;
import java.util.List;

/**
 * ymy
 * 2023/3/25 - 14 : 57
 **/

// leetCode228
// 给定一个无重复元素的有序整数数组 nums 。
// 返回恰好覆盖数组中所有数字的最小有序区间范围列表 。也就是说，nums 的每个元素都恰好被某个区间范围所覆盖，
// 并且不存在属于某个范围但不属于 nums 的数字 x 。列表中的每个区间范围 [a,b] 应该按如下格式输出：
//  1."a->b" ，如果 a != b
//  2."a" ，如果 a == b

public class SummaryRanges {

    public static List<String> summaryRanges(int[] nums) {
        List<String> res = new ArrayList<>();
        if (nums == null || nums.length == 0)
            return res;
        int N = nums.length;
        int L = nums[0], R = nums[0];
        for (int i = 1; i < N; i++) {
            if (nums[i] > R + 1){
                res.add(L + (L == R ? "" : "->" + R));
                L = nums[i];
                R = nums[i];
            } else {
                R++;
            }
        }
        res.add(L + (L == R ? "" : "->" + R));
        return res;
    }


    public static void main(String[] args) {
        int[] arr = {0, 2, 3, 4, 6, 8, 9};
        System.out.println(summaryRanges(arr));
    }
}
