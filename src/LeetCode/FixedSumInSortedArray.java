package LeetCode;

import java.util.Arrays;

/**
 * ymy
 * 2023/4/10 - 20 : 00
 **/


// leetCode167
// 给你一个下标从 1 开始的整数数组 numbers ，该数组已按非递减顺序排列，请你从数组中找出满足相加之和等于目标数
// target 的两个数。如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，则
// 1 <= index1 < index2 <= numbers.length。以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整
// 数的下标 index1 和 index2。
// 你可以假设每个输入只对应唯一的答案 ，而且你不可以重复使用相同的元素。
// 你所设计的解决方案必须只使用常量级的额外空间。

// 2 <= numbers.length <= 3 * 10^4
// -1000 <= numbers[i] <= 1000
// numbers 按 非递减顺序 排列
// -1000 <= target <= 1000
// 仅存在一个有效答案


public class FixedSumInSortedArray {

    public static int[] twoSum(int[] numbers, int target) {
        if (numbers == null || numbers.length < 2)
            return new int[] {};
        int N = numbers.length;
        // 枚举第一个元素，我们希望每次枚举的第一个元素是不一样的，如果是重复的，那就不要枚举了
        // 但是亲测过完发现题目给的数组都是无重复值的
        for (int i = 0; i < N; i++) {
            int one = numbers[i];
            int otherPos = find(numbers, i + 1, N - 1, target - one);
            if (numbers[otherPos] == target - one)
                return new int[] {i + 1, otherPos + 1};
        }
        return new int[] {};
    }



    // arr是有序数组，在[L..R]上找<=t的最右边
    private static int find(int[] arr, int L, int R, int t) {
        int pos = R;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (arr[mid] <= t){
                pos = mid;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        return pos;
    }
    // --------------------------------------------------------------------------------------------------




    // 使用双指针优化
    public static int[] twoSum2(int[] numbers, int target) {
        if (numbers == null || numbers.length < 2)
            return new int[] {};
        int L = 0;
        int R = numbers.length - 1;
        while (L < R){
            if (numbers[L] + numbers[R] == target)
                return new int[] {L + 1, R + 1};
            if ((numbers[L] + numbers[R] < target))
                L++;
            else
                R--;
        }
        return new int[] {};
    }


    public static void main(String[] args) {
        int[] arr = {2, 7, 11, 15};
        int t = 9;
        System.out.println(Arrays.toString(twoSum(arr, t)));
    }
}
