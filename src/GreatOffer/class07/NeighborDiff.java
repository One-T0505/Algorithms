package class07;

import utils.arrays;

import java.util.Arrays;

// 给定一个数组arr，返回如果排序之后，相邻两数的最大差值。要求:时间复杂度O(N)  不使用基于非比较的排序

public class NeighborDiff {

    // 暴力解法
    public static int maxNeighborSum(int[] arr) {
        if (arr == null || arr.length < 2)
            return -1;
        int N = arr.length;
        Arrays.sort(arr);
        int res = 0;
        for (int i = 1; i < N; i++)
            res = Math.max(res, arr[i] - arr[i - 1]);
        return res;
    }
    // ===================================================================================================


    // 时间复杂度的要求已经彻底卡死了排序。这里要用到桶排序的思想。假如数组有N个元素，那么就设置N+1个桶，先遍历一遍
    // 数组找出最大值和最小值，然后将这个范围平均分成N+1份，让每个桶记录一个子范围。每个桶时刻维护着自己桶内当前记录
    // 的最大值和最小值。根据鸽巢原理，必然会有一个空桶，因为即便最差的情况，每个元素独享一个桶，也至少会有一个空桶。
    // 空桶的出现杀死了桶内拥有最大相邻差值的可能，因为同一个桶内最大差值不可能超过一个桶记录的范围，相邻最大差值必然
    // 是来自两个不同的桶。只需要找前一个桶内的最大值和下一个非空桶的最小值即可。因为这样才可能是排序后相邻的元素。
    // 有一个容易忽视的地方：不是说空桶左侧第一个非空桶的最大值和右侧第一个非空桶的最小值就是最大相邻差值。假设：
    // 20～29的桶收到的最大值为20，30～39的桶收到的最小值为39，40～49为空桶，50～59收到的最小值为50，空桶左右两侧
    // 第一个非空桶的差值为：50-39=11。而两个相邻非空桶的最大差值为19。所以非空桶的出现不是为了说明结果是什么，而是为
    // 了说明结果肯定不是来自同一个桶内的。

    // 相邻差值，就是排序后后一个减前一个，所以必然大于等于0
    public static int maxNeighborSumV2(int[] arr) {
        if (arr == null || arr.length < 2)
            return -1;
        int N = arr.length;
        // 桶   buckets[i][0] 第i个桶的最小值；  buckets[i][1] 第i个桶的最大值
        int[][] buckets = new int[N + 1][2];
        boolean[] hasElem = new boolean[N + 1];  // 0表示空， 1表示非空
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int elem : arr) {
            min = Math.min(min, elem);
            max = Math.max(max, elem);
        }
        if (min == max)
            return 0;
        for (int elem : arr) {
            int pos = bucket(elem, N, min, max);
            buckets[pos][0] = hasElem[pos] ? Math.min(buckets[pos][0], elem) : elem;
            buckets[pos][1] = hasElem[pos] ? Math.max(buckets[pos][1], elem) : elem;
            hasElem[pos] = true;
        }
        int cur = 0, res = 0;
        // 先找到第一个非空桶
        while (!hasElem[cur])
            cur++;
        int lastMax = buckets[cur++][1];   // 上一个非空桶的最大值
        while (cur <= N) {
            if (hasElem[cur]) {
                res = Math.max(buckets[cur][0] - lastMax, res);
                lastMax = buckets[cur][1];
            }
            cur++;
        }
        return res;
    }

    private static int bucket(long num, long len, long min, long max) {
        return (int) ((num - min) * len / (max - min));
    }


    // for test
    public static void test(int testTime, int maxLen, int maxVal) {
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.randomNoNegativeArr(maxLen, maxVal);
            int res1 = maxNeighborSum(arr);
            int res2 = maxNeighborSumV2(arr);
            if (res1 != res2) {
                System.out.println("Failed");
                System.out.println("暴力解法：" + res1);
                System.out.println("最终解法：" + res2);
                arrays.printArray(arr);
                return;
            }
        }
        System.out.println("AC");
    }


    public static void main(String[] args) {
        test(100000, 40, 100);
    }
}
