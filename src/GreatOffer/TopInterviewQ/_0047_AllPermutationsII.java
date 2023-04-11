package GreatOffer.TopInterviewQ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * ymy
 * 2023/3/19 - 19 : 53
 **/

// 给定一个可包含重复数字的序列 nums ，按任意顺序返回所有不重复的全排列。

// 1 <= nums.length <= 8
// -10 <= nums[i] <= 10

public class _0047_AllPermutationsII {

    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0)
            return res;
        int N = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        int[] dp = new int[max - min + 1];
        for (int num : nums)
            dp[num - min]++;
        ArrayList<Integer> path = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            path.add(0);
        }
        dfs(dp, min, 0, path, res);
        return res;
    }

    private static void dfs(int[] dp, int offset, int i, ArrayList<Integer> path,
                            List<List<Integer>> res) {
        if (i == path.size())
            res.add(new ArrayList<>(path));
        else {
            for (int p = 0; p < dp.length; p++){
                if (dp[p] > 0){
                    path.set(i, p + offset);
                    dp[p]--;
                    dfs(dp, offset, i + 1, path, res);
                    dp[p]++;
                }
            }
        }
    }


    public static void main(String[] args) {
        int[] arr = {1, 2, 2};
        System.out.println(permuteUnique(arr));
    }
}
