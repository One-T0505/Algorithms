package GreatOffer.TopInterviewQ;

// 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以按任意顺序返回答案。
// 1 <= nums.length <= 6
// -10 <= nums[i] <= 10
// nums 中的所有整数互不相同


import java.util.ArrayList;
import java.util.List;

public class _0046_AllPermutationI {

    public List<List<Integer>> permute(int[] nums) {
        if (nums == null || nums.length == 0)
            return null;
        List<List<Integer>> res = new ArrayList<>();
        f(nums, 0, 0, new ArrayList<Integer>(), res);

        return res;
    }

    private void f(int[] nums, int i, int used, ArrayList<Integer> path, List<List<Integer>> res) {
        if (i == nums.length){
            res.add(new ArrayList<>(path));
        } else {
            for (int j = 0; j < nums.length; j++){
                if (((used >> j) & 1) == 0){
                    path.add(nums[j]);
                    used |= 1 << j;
                    f(nums, i + 1, used, path, res);
                    used ^= (1 << j);
                    path.remove(path.size() - 1);
                }
            }
        }
    }

}
