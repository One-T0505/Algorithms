package Basic.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ymy
 * 2023/3/18 - 10 : 22
 **/

// 给你一个由 n 个整数组成的数组 nums ，和一个目标值 target 。请你找出并返回满足下述全部条件且不重复的四元组
// [nums[a], nums[b], nums[c], nums[d]] （若两个四元组元素一一对应，则认为两个四元组重复）：
// 0 <= a, b, c, d < n
// a、b、c 和 d 互不相同
// nums[a] + nums[b] + nums[c] + nums[d] == target
// 你可以按任意顺序返回答案 。


// 1 <= nums.length <= 200
// -10^9 <= nums[i] <= 10^9
// -10^9 <= target <= 10^9

public class _0018_FourSum {


    // 这个和 leetCode16 题如出一辙，都是先枚举之前的元素，最后剩的两个元素采用双指针加速。
    // 别看下面的代码很多，那么多 break  continue  其实都只是为了加速枚举，提前过滤掉不必要的枚举
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 4)
            return res;
        Arrays.sort(nums);
        int N = nums.length;
        for (int i = 0; i < N - 3; i++) {
            // 保证枚举的第一个元素都是不相同的，这样就能天然过滤了
            if (i > 0 && nums[i] == nums[i - 1])
                continue;
            // 如果在第一个元素确定的情况下，和全局剩余最小的三个元素匹配的和依然大于target，那就不用枚举了
            // 因为再往下枚举的第一个元素必然不会小于当前
            if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target)
                break;
            // 如果在第一个元素确定的情况下，和全局剩余最大的三个元素匹配的和依然小于target，那就不用继续了，
            // 直接枚举下一个开头
            if ((long) nums[i] + nums[N - 1] + nums[N - 2] + nums[N - 3] < target)
                continue;
            for (int j = i + 1; j < N - 2; j++) {
                // 保证第一个元素不同的情况下，也要保证第二个枚举的元素也不相同
                if (j > i + 1 && nums[j] == nums[j - 1])
                    continue;
                if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target)
                    break;
                if ((long) nums[i] + nums[j] + nums[N - 1] + nums[N - 2] < target)
                    continue;
                // 最后两个元素的选取使用双指针
                int L = j + 1, R = N - 1;
                while (L < R){
                    long cur = (long) nums[i] + nums[j] + nums[L] + nums[R];
                    if (cur == target){
                        res.add(Arrays.asList(nums[i], nums[j], nums[L], nums[R]));
                        // 此时要让L、R一起移动到下一个有效位置
                        while (L < R && nums[L] == nums[L + 1])
                            L++;
                        L++;
                        while (L < R && nums[R] == nums[R - 1])
                            R--;
                        R--;
                    } else if (cur > target) { // 那就让R向左移动，保证移动到一个不同的值的地方
                        while (L < R && nums[R] == nums[R - 1])
                            R--;
                        R--;
                    } else {
                        while (L < R && nums[L] == nums[L + 1])
                            L++;
                        L++;
                    }
                }
            }
        }
        return res;
    }



    public static void main(String[] args) {
        int[] arr = {0, 0, 0, 1000000000, 1000000000, 1000000000, 1000000000};
        int t = 1000000000;
        System.out.println(fourSum(arr, t));
    }
}
