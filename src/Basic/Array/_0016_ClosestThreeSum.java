package Basic.Array;

import java.util.Arrays;

import utils.arrays;

/**
 * ymy
 * 2023/3/17 - 22 : 15
 **/

// 给你一个长度为 n 的整数数组 nums 和 一个目标值 target。请你从 nums 中选出三个整数，使它们的和与 target 最接近。
// 返回这三个数的和。假定每组输入只存在恰好一个解。

// 3 <= nums.length <= 1000
// -1000 <= nums[i] <= 1000
// -10^4 <= target <= 10^4

public class _0016_ClosestThreeSum {

    public static int threeSumClosest(int[] nums, int target) {
        if (nums == null || nums.length < 3)
            return Integer.MIN_VALUE;
        if (nums.length == 3)
            return nums[0] + nums[1] + nums[2];
        int N = nums.length;
        Arrays.sort(nums);
        int res = 10000000; // 这个是根据数据规模来选定的，如果直接设置成系统最大或最小，下面更新答案时不太方便

        // 枚举第一个元素的位置
        for (int i = 0; i < N - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) // 这种情况枚举过了
                continue;
            int L = i + 1, R = N - 1;  // 剩下两个元素可以选择的范围
            while (L < R){
                int cur = nums[i] + nums[L] + nums[R];
                if (cur == target)
                    return cur;
                if (Math.abs(cur - target) < Math.abs(res - target))
                    res = cur;
                // 这种情况下，要让R移动
                if (cur > target){
                    // 让R移动到下一个不相等值的地方
                    int R0 = R - 1;
                    while (R0 > L && nums[R0] == nums[R])
                        R0--;
                    R = R0;
                } else { // cur < target  此时要让L移动
                    int L0 = L + 1;
                    while (L0 < R && nums[L0] == nums[L])
                        L0++;
                    L = L0;
                }
            }
        }
        return res;
    }



    // 在nums[L..R]上找到 >=t 最左边的位置
    private static int mostLeft(int[] nums, int L, int R, int t) {
        int res = R;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (nums[mid] >= t){
                res = mid;
                R = mid - 1;
            } else
                L = mid + 1;
        }
        return res;
    }




    // 暴力解法
    public static int threeSumClosest2(int[] nums, int target) {
        if (nums == null || nums.length < 3)
            return Integer.MIN_VALUE;
        if (nums.length == 3)
            return nums[0] + nums[1] + nums[2];
        int res = Integer.MIN_VALUE;
        int N = nums.length;
        for (int i = 0; i < N - 2; i++) {
            for (int j = i + 1; j < N - 1; j++) {
                for (int k = j + 1; k < N; k++) {
                    int cur = nums[i] + nums[j] + nums[k];
                    if (res == Integer.MIN_VALUE || Math.abs(cur - target) < Math.abs(res - target))
                        res = cur;
                }
            }
        }
        return res;
    }




    // for test
    public static void test(int testTime, int maxLen, int maxVal, int range){
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.RandomArr(maxLen, maxVal);
            int[] ori = new int[arr.length];
            System.arraycopy(arr, 0, ori, 0, arr.length);
            int t = arrays.generateRandomNum(range);
            int res1 = threeSumClosest(arr, t);
            int res2 = threeSumClosest2(arr, t);
            if (res1 != res2){
                System.out.println("Failed");
                arrays.printArray(ori);
                System.out.println("目标值是：" + t);
                System.out.println("正确解法：" + res2);
                System.out.println("优化解法：" + res1);
                return;
            }
        }
        System.out.println("AC");
    }


    public static void main(String[] args) {
//        test(10000, 6, 30, 40);
        int[] arr = {-12, 25, 1, 14};
        int t = -22;
        System.out.println(threeSumClosest(arr, t));
    }
}
