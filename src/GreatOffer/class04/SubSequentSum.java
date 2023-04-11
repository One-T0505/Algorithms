package class04;

import utils.arrays;

// 返回一个数组中，选择的数字不能相邻的情况下，最大子序列累加和.

public class SubSequentSum {

    // 暴力尝试
    public static int gapSubSequentSum(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        return process(arr, 0, true);
    }

    // 0~index-1已经做好决策不用管了，现在来到index位置，需要对index...的元素负责。flag表示当前元素是否可选。
    // 返回index...能找到的最大不相邻子序列累加和。
    private static int process(int[] arr, int index, boolean flag) {
        if (index == arr.length)
            return 0;
        int p1 = 0;
        if (flag)
            p1 = arr[index] + process(arr, index + 1, false);
        int p2 = process(arr, index + 1, true);
        return Math.max(p1, p2);
    }
    // ====================================================================================================


    // 根据暴力递归改出动态规划
    public static int dpV1(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        int N = arr.length;
        int[][] cache = new int[N + 1][2]; // 0列表示true，1列表示false
        for (int index = N - 1; index >= 0; index--) {
            cache[index][0] = Math.max(arr[index] + cache[index + 1][1], cache[index + 1][0]);
            cache[index][1] = cache[index + 1][0];
        }
        return cache[0][0];
    }


    // 动态规划   转移方程非常好想：0～i位置上找不相邻子序列的最大累加和，如果我一定要选arr[i]，那么就只能从0～i-2位置上
    //           找最大累加和；如果不选arr[i]，那就是从0～i-1位置上找最大累加和。这里有一种情况非常容易漏掉！！！！！！
    // 比如arr=[-2, 1, 10]  那么cahce=[-2, 1, ]，根据上面说的，cache[2]有两种情况：1>cahce[2] = cache[1];
    // 2>cache[2]=arr[2] + cache[0]。第一种情况cahce[2]=1，第二种情况cahce[2]=8，但其实我只选我自己10是最大的。
    // 所以我们漏掉的情况就是单独只选自己的情况。这第三种情况只适用于数组中可能包含负数的情况，如果数组是非负的，那前面两种
    // 情况就完全了。
    public static int dpV2(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length <= 2)
            return arr.length == 1 ? arr[0] : Math.max(arr[0], arr[1]);
        int N = arr.length;
        int[] cache = new int[N];
        cache[0] = arr[0];
        cache[1] = Math.max(arr[0], arr[1]);
        for (int i = 2; i < N; i++)
            cache[i] = Math.max(arr[i] + cache[i - 2], Math.max(cache[i - 1], arr[i]));
        return cache[N - 1];
    }
    // ====================================================================================================


    // 在V2的基础上使用空间压缩
    public static int dpV3(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length <= 2)
            return arr.length == 1 ? arr[0] : Math.max(arr[0], arr[1]);
        int N = arr.length;

        int former = arr[0];
        int latter = Math.max(arr[0], arr[1]);
        int cur = 0;
        for (int i = 2; i < N; i++) {
            cur = Math.max(arr[i] + former, Math.max(latter, arr[i]));
            former = latter;
            latter = cur;
        }
        return cur;
    }


    // for test
    public static void test() {
        for (int i = 0; i < 1000; i++) {
            int[] arr = arrays.randomNoNegativeArr(30, 100);
            int res1 = gapSubSequentSum(arr);
            int res2 = dpV1(arr);
            int res3 = dpV2(arr);
            int res4 = dpV3(arr);
            if (!(res1 == res2 && res2 == res3 && res3 == res4)) {
                System.out.println("Failed");
                System.out.println("暴力递归：" + res1);
                System.out.println("动态规划V1：" + res2);
                System.out.println("动态规划V2：" + res3);
                System.out.println("动态规划V3：" + res4);
                arrays.printArray(arr);
                return;
            }
        }
        System.out.println("AC");
    }

    // ====================================================================================================
    public static void main(String[] args) {
//        int[] arr = {2, 7, -6, 3, 9};
//        System.out.println(maxSum(arr));
        test();
    }


}
