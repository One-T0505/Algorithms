package Basic.DynamicProgramming;

// leetCode78
// 给你一个整数数组 nums ，数组中的元素互不相同 。返回该数组所有可能的子集（幂集）。
// 解集不能包含重复的子集。你可以按任意顺序返回解集。

import java.util.ArrayList;
import java.util.List;

public class _0078_SubsetsI {

    public static List<List<Integer>> subsets(int[] nums) {
        ArrayList<List<Integer>> res = new ArrayList<>();
        f(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private static void f(int[] nums, int i, List<Integer> path, ArrayList<List<Integer>> res) {
        if (i == nums.length) {
            res.add(new ArrayList<>(path));
            return;
        }
        f(nums, i + 1, path, res);
        path.add(nums[i]);
        f(nums, i + 1, path, res);
        path.remove(path.size() - 1);
    }


    public static void main(String[] args) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(4);
        path.add(8);
        ArrayList<List<Integer>> dp = new ArrayList<>();
        dp.add(new ArrayList<>(path));

    }
}
