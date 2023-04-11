package Basic.DynamicProgramming;

import java.util.*;

/**
 * ymy
 * 2023/3/19 - 19 : 04
 **/

// 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
// 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。 [1, 2]   [2, 1]  认为是同样的集合

// 1 <= nums.length <= 10
// -10 <= nums[i] <= 10


public class _0090_SubsetsII {

    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        if (nums == null)
            return null;
        Arrays.sort(nums);
        int N = nums.length;
        List<Integer> path = new ArrayList<>();
        List<List<Integer>> res = new ArrayList<>();
        dfs(nums, false, 0, path, res);
        return res;
    }


    // choosePre 表示是否选择了 i-1 位置的数
    private static void dfs(int[] nums, boolean choosePre, int i, List<Integer> path,
                List<List<Integer>> res) {
        if (i == nums.length){
            res.add(new ArrayList<>(path));
            return;
        }
        // 不选自己的情况肯定是有的
        dfs(nums, false, i + 1, path, res);
        // 选择自己的时候是有条件的
        // 如果上一个没选，并且此时还是相等的值，那么就会有重复答案产生。
        if (!choosePre && i > 0 && nums[i - 1] == nums[i])
            return;
        path.add(nums[i]);
        dfs(nums, true, i + 1, path, res);
        path.remove(path.size() - 1);
    }


    public static void main(String[] args) {
        int[] arr = {1, 2, 2};
        System.out.println(subsetsWithDup(arr));
    }
}
