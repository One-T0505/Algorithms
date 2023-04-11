package Basic.Array;

import java.util.Arrays;
import java.util.HashSet;

public class _0061_Code {

    // 初版
    public static boolean isStraightV1(int[] nums) {
        bubbleSort(nums);
        int num = 0; // 记录0的个数
        for (int j : nums) {
            if (j == 0)
                num++;
        }
        for (int i = num; i < nums.length - 1; i++) {
            if (nums[i + 1] - nums[i] >= 1) {
                num -= (nums[i + 1] - nums[i] - 1);
                if (num < 0)
                    return false;
            } else if (nums[i] == nums[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static void bubbleSort(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;
                }
            }
        }
    }

    // 进化版
    // 思路：数组中，除了0以外，如果其他的数有重复则结束；除了0以外，最大值 - 最小值 >= 5，也直接结束
    public static boolean isStraightV2(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        int max = 0, min = 14;
        for (int elem : nums) {
            if (elem == 0) continue;
            if (set.contains(elem))
                return false;
            set.add(elem);
            max = Math.max(max, elem);
            min = Math.min(min, elem);
        }
        return max - min < 5;
    }

    // 最终版：不需要用一个hashset来记录是否有重复元素。先将数组排序好，数组最后一个元素和第一个不为0的元素的差值就是
    // 最大值 - 最小值
    public static boolean isStraightV3(int[] nums) {
        Arrays.sort(nums);
        int joker = 0; // 记录0的数量
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == 0) joker++;
            else if (nums[i] == nums[i + 1])
                return false;
        }
        return nums[nums.length - 1] - nums[joker] < 5;
    }

    // 生成一个长度为size的数组，数组元素的值从0～maxVal随机选择
    public static int[] generateRandomArray(int size, int maxVal) {
        int[] arr = new int[size];
        // 因为数组默认元素值都为0，所以当确定好zeroNum后，直接从zeroNum索引开始往后即可
        for (int i = 0; i < size; i++)
            arr[i] = (int) (Math.random() * (maxVal + 1)); // 随机生成一个1~maxVal的数，不能有重复
        return arr;
    }

    // 数组的打印
    public static void printArray(int[] arr) {
        for (int elem : arr)
            System.out.print(elem + "\t");
        System.out.println();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int[] array = generateRandomArray(5, 13);
            int[] copy = new int[array.length];
            System.arraycopy(array, 0, copy, 0, array.length);
            if (isStraightV3(array) != isStraightV2(copy))
                printArray(array);
        }
    }
}
