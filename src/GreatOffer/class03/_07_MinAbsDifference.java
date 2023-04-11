package class03;

import utils.arrays;

import java.util.Arrays;

// leetCode1755
// 给你一个整数数组 nums 和一个目标值 goal。你需要从 nums 中选出一个子序列，使子序列元素累加和最接近goal。
// 也就是说，如果子序列元素和为 sum ，你需要最小化绝对差 abs(sum - goal) 。返回 abs(sum - goal)可能的最小值。

// 注意这道题给的限制：
//  1 <= nums.length <= 40
// -10^7 <= nums[i] <= 10^7
// -10^9 <= goal <= 10^9

// 这道题用传统的背包来做是可以的，不过那个二维缓存表的规模就是：40 * 10^9 --> 远远超过了 10^8 ，所以必定超时。
// 体系学习班里有根据数据规模猜解法的问题，像这样的情况，数组元素很大，目标值很大，唯独数组长度很小，所以就需要想到
// 用分治。先处理一半20个，思路还是每个元素都有要或者不要两种选择，所以左半部分就是：2^20，右半部分也是2^20;
// 有可能结果就在左边，也有可能结果就在右边；也有可能需要两边一起。将右半部分的结果都存入有序表，然后对左边2^20个结果，
// 都去有序表中查，有序表的增删改查都是logN，所以这部分时间就是：2^20 * log2^20 == 20 * 2^20
// 总的规模就是：2^20 + 2^20 + 20 * 2^20 == 11 * 2^21 << 10^8   分治直接让时间复杂度降低很多

public class _07_MinAbsDifference {

    // 数组左半部分的所有可能累加和都放在这里
    public static int[] left = new int[1 << 20];

    // 数组右半部分的所有可能累加和都放在这里
    public static int[] right = new int[1 << 20];

    public static int minAbsDifference(int[] nums, int goal) {
        if (nums == null || nums.length == 0)
            return goal;
        // 收集数组左半部分，所有可能的累加和
        // 并且返回一共收集了几个数，就是le
        int le = process(nums, 0, nums.length >> 1, 0, 0, left);            // O(logN)
        // 收集数组右半部分，所有可能的累加和
        // 并且返回一共收集了几个数，就是re
        int re = process(nums, nums.length >> 1, nums.length, 0, 0, right);      // O(logN)
        // 把左半部分收集到的累加和排序  Arrays.sort(arr, 0, 3)  是前开后闭的，只能排好[0, 2]
        Arrays.sort(left, 0, le);          // O(NlogN)
        // 把右半部分收集到的累加和排序
        Arrays.sort(right, 0, re--);       // O(NlogN)
        // 为什么要排序？
        // 因为排序之后定位数字可以不回退
        // 比如你想累加和尽量接近10
        // 左半部分累加和假设为 : 0, 2, 3, 7, 9
        // 右半部分累加和假设为 : 0, 6, 7, 8, 9
        // 左部分累加和是0时，右半部分选哪个累加和最接近10，是9
        // 左部分累加和是2时，右半部分选哪个累加和最接近10，是8
        // 左部分累加和是3时，右半部分选哪个累加和最接近10，是7
        // 左部分累加和是7时，右半部分选哪个累加和最接近10，是0
        // 左部分累加和是9时，右半部分选哪个累加和最接近10，是0
        // 上面你可以看到，
        // 当你从左往右选择左部分累加和时，右部分累加和的选取，可以从右往左
        // 这就非常的方便
        // 下面的代码就是这个意思
        int res = Math.abs(goal);
        for (int i = 0; i < le; i++) {
            int rest = goal - left[i];
            while (re > 0 && Math.abs(rest - right[re - 1]) <= Math.abs(rest - right[re])) {
                re--;
            }
            res = Math.min(res, Math.abs(rest - right[re]));
        }
        return res;
    }


    // nums[0..index-1]已经选了一些数字，组成了累加和sum
    // 当前来到nums[index....end)这个范围，所有可能的累加和
    // 填写到arr里去
    // fill参数的意思是: 如果出现新的累加和，填写到arr的什么位置
    // 返回所有生成的累加和，现在填到了arr的什么位置
    private static int process(int[] nums, int index, int end, int sum, int fill, int[] arr) {
        if (index == end) {
            // 把当前的累加和sum
            // 填写到arr[fill]的位置
            // 然后fill++，表示如果后续再填的话
            // 该放在什么位置了
            arr[fill++] = sum;
        } else {
            // 可能性1 : 不要当前的数字
            // 走一个分支，形成多少累加和，都填写到arr里去
            // 同时返回这个分支把arr填到了什么位置
            fill = process(nums, index + 1, end, sum, fill, arr);
            // 可能性2 : 要当前的数字
            // 走一个分支，形成多少累加和，都填写到arr里去
            // 接着可能性1所填到的位置，继续填写到arr里去
            // 这就是为什么要拿到上一个分支填到哪了
            // 因为如果没有这个信息，可能性2的分支不知道往哪填生成的累加和
            fill = process(nums, index + 1, end, sum + nums[index], fill, arr);
        }
        return fill;
    }


    public static void main(String[] args) {
        int[] arr = {3, 6, 4, 2, 5};
        Arrays.sort(arr, 0, 3);
        arrays.printArray(arr);
    }
}
