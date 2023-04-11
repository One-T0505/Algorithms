package GreatOffer.Interview;

import java.util.Arrays;

// 给定一个数组arr，比如{5，3，1,4}  和一个正数 k==4
// 全部数字对是: (5,3). (5,1)、 (5,4)、 (3,1). (3,4)、 (1,4)
// 数字对的差值绝对值: 2、4、1、2、1、3
// 差值绝对值排序后: 1、1、2、2、3、4
// 返回arr中所有数字对差值的绝对值，第k小的是多少  返回2

public class KthMinPairAbs {

    // 暴力解法就不说了，时间复杂度为O(N^2)  这里讲最优解，利用二分后达到 O(NlogN)
    // 首先先对原数组排序，那么该数组任意数字对的绝对值abs都有上下界限：0 <= abs <= arr[N - 1] - arr[0]
    // 确定了差值的上下界限L、R后，我们来讲下算法主流程：
    // 假如k==50   <=38  <=39  <=40  <=41  <=42     上面表示差值对  下面表示数量
    //              22    22    41    52
    // 差值<=38的数字对有22个，差值<=39的数字对也有22个，那说明差值==39的数字对数量为0。k==50，
    // 那就是说我们要返回的值是41，因为<=40的差值对有41个，而<=41的差值对就增加到>=50个了，所以答案就是41。
    // 总体来说就是我们要二分找到一个目标差值abs，使得其数量<k，但是又是最大的，并且abs+1的数量就刚好>=k，那么我们的答案
    // 就是abs+1。
    // 第二步就是如何求<=某一个差值的数字对数量，这里我们用双指针不回退技巧，因为原数组是排好序的，所以当前指针不动，后指针
    // 往右移动，其差值的绝对值必然持平或增加。

    public static int kthAbs(int[] arr, int k) {
        int N = arr.length;
        // N个数只有 N*(N-1)/2个数字对
        if (N < 2 || k < 1 || k > (((N * (N - 1)) >> 1)))
            return Integer.MAX_VALUE;
        Arrays.sort(arr);
        int L = 0;
        int R = arr[N - 1] - arr[0];
        int mid = 0;
        int rightest = -1;  // 这个就表示我们要找的那个最合适的差值，注意是差值不是<=这个差值的数字对数量
        while (L <= R) {
            mid = L + ((R - L) >> 1);
            // valid的作用：arr中差值绝对值<=mid的数量是否<k
            if (valid(arr, mid, k)) {
                rightest = mid;
                L = mid + 1;
            } else
                R = mid - 1;
        }
        // 这个rightest的初始值设置得非常巧妙 即便是最极端的例子，数组全都是同样的数：[3, 3, 3, 3, 3, 3]
        // 那么其差值只有0，进入while后会立马出来，rightest不变还是-1，不管k是多少，都应该返回0，rightest+1正好
        // 也适应这种极端个例。
        return rightest + 1;  // 万一这个差值并不存在呢？
    }


    // 假设arr中的所有数字对，差值绝对值<=limit的个数为x
    // 如果 x < k，达标，返回true
    // 如果 x >= k，不达标，返回false
    // 利用双指针求
    private static boolean valid(int[] arr, int limit, int k) {
        int x = 0;
        for (int l = 0, r = 1; l < arr.length; r = Math.max(r, ++l)) {
            while (r < arr.length && arr[r] - arr[l] <= limit)
                r++;
            x += r - l - 1;
        }
        return x < k;
    }

    // 3  5  5  7  10  12   4
    //    L         R

    public static void main(String[] args) {
        System.out.println(kthAbs(new int[]{5, 3, 1, 4}, 4));
    }
}
