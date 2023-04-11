package LeetCode;

/**
 * ymy
 * 2023/3/20 - 10 : 26
 **/

import utils.arrays;

import java.util.Arrays;

// leetCode153
// 已知一个长度为 n 的数组，预先按照升序排列，经由 1 到 n 次 旋转 后，得到输入数组。例如，原数组
// nums = [0,1,2,4,5,6,7] 在变化后可能得到：
//   若旋转 1 次，则可以得到 [7,0,1,2, 4, 5, 6]
//   若旋转 7 次，则可以得到 [0,1,2,4,5,6,7]
// 注意，数组 [a[0], a[1], a[2], ..., a[n-1]] 旋转一次的结果为数组 [a[n-1], a[0], a[1], a[2], ..., a[n-2]]。
// 给你一个元素值互不相同的数组 nums ，它原来是一个升序排列的数组，并按上述情形进行了多次旋转。请你找出并返回数组中的
// 最小元素。你必须设计一个时间复杂度为 O(log n) 的算法解决此问题。

public class RotatedArray {

    // 可以发现：如果旋转次数为0，或者是数组长度的整数倍，那么就相当于没有旋转。
    // 如果没有旋转，那么左边界的值必然小于右边界的值。
    public static int findMin(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MAX_VALUE;
        int N = nums.length;
        if (N == 1 || nums[0] < nums[N - 1]) // 说明相当于没旋转 或者 只有一个元素
            return nums[0];
        int L = 0, R = N - 1;
        while (L < R){
            int mid = L + ((R - L) >> 1);
            if (nums[mid] < nums[R]){
                // 这里要注意。因为经典二分查找时 right = mid - 1
                // 因为在经典二分查找时，我们可以很确定地说mid处的值已经不可能是答案了，所以在让L、R移动时排除了mid。
                // 但是这里，如果 nums[mid] < nums[R]  我们能这样做吗？不能，因为要找全局最小值，你怎么知道
                // 此时的mid处的值不是最小值呢？所以说，我们没办法排除，那就不能排除mid这个位置。但是下面的else
                // 就不一样了，因为不包含重复，所以else就等价于 nums[mid] > nums[R]
                // 既然都大于了，那么mid处的值肯定不是全局最小了，所以可以排除了。
                R = mid;
            } else {
                L = mid + 1;
            }
        }
        return nums[L];
    }



    // 升级版，leetCode154  取消上面无重复数字的限制。
    // 当数组中可能包含重复值时，我们就不能像上面那样做简单判断了。这里我们要用二分和顺序查找相结合的方式来做。
    // 我们考虑数组中的最后一个元素 x：在最小值右侧的元素，它们的值一定都小于等于 x；而在最小值左侧的元素，它们的
    // 值一定都大于等于 x。因此，我们可以根据这一条性质，通过二分查找的方法找出最小值。
    //  1.第一种情况是 nums[mid] < nums[R] 因此我们可以忽略二分查找区间的右半部分。
    //  2.第一种情况是 nums[mid] > nums[R] 因此我们可以忽略二分查找区间的左半部分。
    //  2.第一种情况是 nums[mid] = nums[R]  比如：[3, 4, 3, 3, 3]  或者  [3, 3, 3, 4, 3]
    //    这两个数组都是原数组经过 [3, 3, 3, 3, 4]  旋转得到的，所以说，碰到相等情况时，我们无法断定最小值在哪边，
    //    也就不能草率地舍弃某一侧。我们唯一可以知道的是，由于它们的值相同，所以无论 nums[R] 是不是最小值，都有一个
    //    它的「替代品」 nums[mid]，因此我们可以忽略二分查找区间的右端点。
    public static int findMin2(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MAX_VALUE;
        int N = nums.length;
        int L = 0, R = N - 1;
        while (L < R){
            int mid = L + ((R - L) >> 1);
            if (nums[mid] < nums[R]){
                R = mid;
            } else if (nums[mid] > nums[R]){
                L = mid + 1;
            } else // 相等的时候
                R--;
        }
        return nums[L];
    }



    public static int findMin3(int[] nums) {
        if (nums == null || nums.length == 0)
            return Integer.MAX_VALUE;
        int N = nums.length;
        for(int i = 1; i < N; i++){
            if(nums[i] < nums[i - 1])
                return nums[i];
        }
        return nums[0];
    }




    // performance test
    private static void performanceTest(int testTime, int len, int maxVal){
        int a = 0, b = 0;
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.RandomArr(len, maxVal);
            Arrays.sort(arr);
            int N = arr.length;
            int pivot = (int) (Math.random() * N);
            rotate(arr, 0, pivot, N - 1);
            long start = System.currentTimeMillis();
            int res = findMin2(arr);
            long end = System.currentTimeMillis();
            a += end - start;
        }
        System.out.println("二分法总共用时：" + a + " ms");
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.RandomArr(len, maxVal);
            Arrays.sort(arr);
            int N = arr.length;
            int pivot = (int) (Math.random() * N);
            rotate(arr, 0, pivot, N - 1);
            long start = System.currentTimeMillis();
            int res = findMin3(arr);
            long end = System.currentTimeMillis();
            b += end - start;
        }
        System.out.println("暴力法总共用时：" + b + " ms");
    }

    private static void rotate(int[] arr, int L, int mid, int R) {
        reverse(arr, L, mid);
        reverse(arr, mid + 1, R);
        reverse(arr, L, R);
    }

    private static void reverse(int[] arr, int L, int R) {
        while (L < R){
            int tmp = arr[L];
            arr[L++] = arr[R];
            arr[R--] = tmp;
        }
    }




    public static void main(String[] args) {
        performanceTest(10000, 10000, 500);
    }

}
