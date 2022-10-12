package MonotonicStack;

import utils.arrays;

// 给定一个只包含正数的数组arr，arr中任何一个子数组sub, 一定都可以算出(sub累加和)* (sub中的最小值)是什么，
// 那么所有子数组中，这个值最大是多少?

// 思路：如果想应用上单调栈结构，那么找出一个数组的所有子数组的标准就和之前有点不一样了。这里我们找子数组应该以数组中某个元素是
// 作为该子数组的最小值来找。因为最后要求的那个最大的指标肯定也是原数组的一个子数组，其次它必然是以数组中的某个元素为该子数组的
// 最小值。
// 以arr[i]作为子数组的最小值，能找出哪些子数组呢？这样就用到了单调栈。单调栈可以找出某个元素左边离它最近但是比它小的元素位置left，
// 和右边离他最近且比他小的元素的位置right，那么[left+1, right-1]就是以arr[i]为最小值，并且最长的子数组了，那么其累加和
// 必然是以arr[i]作为最小值的所有子数组中最大的，那么该子数组的指标也是这些子数组中最大的。所以只需要对每个元素运用单调栈找出
// 这样一个区间算出指标，那么最终的答案一定在其中。

// 累加和这个问题就需要提前做好一个累加和数组

public class exercise01 {

    // 暴力法实现
    public static int v1(int[] arr){
        // 制作累加和数组
        int N = arr.length;
        int[] sum = new int[N];
        sum[0] = arr[0];
        for (int i = 1; i < N; i++)
            sum[i] = sum[i - 1] + arr[i];
        int res = Integer.MIN_VALUE;
        for (int left = 0; left < N; left++) {
            for (int right = left; right < N; right++) {
                int min = Integer.MAX_VALUE;
                for (int i = left; i <= right; i++)
                    min = Math.min(arr[i], min);
                int curSum = left == right ? arr[left] : sum[right] - sum[left];
                res = Math.max(res, min * curSum);
            }
        }
        return res;
    }


    // 单调栈实现
    public static int v2(int[] arr){
        // 返回的areas形状为N*2 ，每一行的0列表示左边界，1列表示右边界，如果是-1表示没有
        int[][] areas = MonotonicStack.monotonicStackRepeat(arr);
        // 制作累加和数组
        int N = arr.length;
        int[] sum = new int[N];
        sum[0] = arr[0];
        for (int i = 1; i < N; i++)
            sum[i] = sum[i - 1] + arr[i];

        int res = Integer.MIN_VALUE;
        for (int i = 0; i < N; i++) {
            int left = areas[i][0] + 1;
            int right = areas[i][1] == -1 ? N - 1 : areas[i][1] - 1;
            // 处理过后的left、right可能相等
            int curSum = right == left ? arr[left] : sum[right] - sum[left];
            res = Math.max(res, curSum * arr[i]);
        }
        return res;
    }


    public static void main(String[] args) {
        int[] arr = arrays.generateRandomArray(15, 50);
        System.out.println(v1(arr));
        System.out.println(v2(arr));
    }
}
