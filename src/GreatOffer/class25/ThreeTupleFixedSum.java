package GreatOffer.class25;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，
// 同时还满足 nums[i] + nums[j] + nums[k] == 0 。请你返回所有和为 0 且不重复的三元组。
// 这里三元组里记录的是值。

public class ThreeTupleFixedSum {


    // 要想完成这个问题，得先了解二元组问题的基本模型。二元组问题要求数组有序，可以包含重复值。
    // 在arr[0..end]这段区间上，找出所有相加为target的不重复二元组
    private static List<List<Integer>> twoSum(int[] arr, int end, int target) {
        int L = 0;
        int R = end;
        List<List<Integer>> res = new ArrayList<>();
        while (L < R) {
            if (arr[L] + arr[R] > target)
                R--;
            else if (arr[L] + arr[R] < target)
                L++;
            else { // arr[L] + arr[R] == target  也不是就能直接加入答案的，因为有重复值的情况，所以不要重复记录
                if (L == 0 || arr[L - 1] != arr[L]) {
                    ArrayList<Integer> cur = new ArrayList<>();
                    cur.add(arr[L]);
                    cur.add(arr[R]);
                    res.add(cur);
                }
                L++;
            }
        }
        return res;
    }


    // 有了二元组模型的基础，三元组问题会很好理解。因为题目要求的是三元组里记录的是值，所以，我们才可以
    // 用上面的模型，如果要求记录的是位置，那就不能用。
    public static List<List<Integer>> threeSum(int[] nums) {
        int N = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        // 让nums[i]作为三元组最后一个，那么问题就变成了，从0..i-1找出所有不同的二元组去满足-nums[i],
        // 这样三元组的和就是0了
        for (int i = N - 1; i > 1; i--) { // i最低来到2，因为前面至少要留2个数
            // 如果相等，那么不管找出的二元组相不相同，第三个数必然不相同
            if (i == N - 1 || nums[i] != nums[i + 1]) {
                List<List<Integer>> rest = twoSum(nums, i - 1, -nums[i]);
                // 把每一个二元组的最后加入当前数，组成完整的三元组
                for (List<Integer> cur : rest) {
                    cur.add(nums[i]);
                    res.add(cur);
                }
            }
        }
        return res;
    }


    // twoSum这个方法我们完全可以设计成这样的形式：twoSum(arr, L, R)，在给定的范围上去找二元组；然后在主函数中，
    // 从0 1 2..N-3 这样的方向让arr[i]作为第三个数，然后调用twoSum(arr, i+1, N-1)，因为这样的方式更符合人的
    // 直觉。这样做的问题就在于，找出的二元组的位置是我们决定的第三个数的后面，也就是说我们需要在每个二元组的最前面加上
    // nums[i]，这样就比较麻烦了。所以，我们采用上面实际的方式，为的就是可以直接在二元组的最后直接加上nums[i]。
}
