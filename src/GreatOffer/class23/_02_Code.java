package GreatOffer.class23;


// 定义什么是可整合数组:
// 一个数组排完序之后，除了最左侧的数外，有arr[i] = arr[i-1]+1  则称这个数组为可整合数组
// 比如{5,1,2,4,3}、{6,2,3,1,5,4}都是可整合数组
// 返回arr中最长可整合子数组的长度

import java.util.HashSet;

public class _02_Code {

    // 这个题目的最优方法时间复杂度为：O(N^2)
    // N^2的主体就是套两层循环，外层循环确定L边界，内层循环确定R边界，这两层循环就是在枚举所有子数组
    // 首先我们需要对可整合的定义换一种等价的解释：
    //  1.L..R这个范围上的值不能重复
    //  2.如果L..R这段的最大值max和最小值min满足 max - min == 个数 - 1 == R - L
    // 如果满足以上两点，那么L..R这段必然是可整合数组

    public static int solution(int[] arr) {
        if (arr == null || arr.length == 0)
            return 0;
        if (arr.length <= 2)
            return arr.length == 1 ? 1 : (Math.abs(arr[0] - arr[1]) == 1 ? 2 : 1);
        int N = arr.length;
        HashSet<Integer> set = new HashSet<>();
        // 任何一个单独的元素都是可整合数组，所以最终答案必然>=1，所以我们让R每次都从L+1开始，从L开始无意义
        int res = 1;
        for (int L = 0; L < N; L++) {
            // 每次换了一个新的头后，就需要清空集合
            set.clear();
            int max = arr[L];
            int min = arr[L];
            set.add(arr[L]);
            for (int R = L + 1; R < N; R++) {
                // 如果已经包含了arr[R]直接跳出循环，让L换一个新的
                if (set.contains(arr[R]))
                    break;
                set.add(arr[R]);
                max = Math.max(max, arr[R]);
                min = Math.min(min, arr[R]);
                // max - min == 个数 - 1 == (R - L + 1) - 1
                if (max - min == R - L)
                    res = Math.max(res, R - L + 1);
            }
        }
        return res;
    }
}
