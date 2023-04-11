package Basic.SlidingWindow;

// 给定一个有序数组arr，代表坐落在X轴上的点，给定一个正数K，代表绳子的长度，返回绳子最多压中几个点？
// 即使绳子边缘处盖住点也算盖住

import utils.arrays;
import java.util.Arrays;

public class RodeCoverNodes {

    // 不管用什么方法，我们先确定一个贪心策略：就是说让绳子一段正好压在点上，这样能覆盖的点才能是最多的。
    // 如果让绳子的开头放在不存在的点上，这样能找到的点不是最多的。

    // 初级方法：遍历整个数组，每次以该数组元素arr[i]作为绳子开头的地方，然后在剩余元素中找出
    // 最后一个arr[x] <= arr[i]+k即可，这个找法可以用二分，所以时间复杂度为: O(NlogN)
    public static int coverNodeV1(int[] arr, int k){
        if (arr == null || arr.length == 0 || k <= 0)
            return 0;
        Arrays.sort(arr);  // 要先保证数组有序
        int res = 0;
        for (int i = 0; i < arr.length; i++) {
            int nearest = search(arr, i, arr[i] + k);
            res = Math.max(nearest - i + 1, res);
        }
        return res;
    }

    private static int search(int[] arr, int L, int des) {
        int index = L;
        int R = arr.length - 1;
        while (L <= R){
            int mid = L + ((R - L) >> 1);
            if (arr[mid] > des){
                R = mid - 1;
            } else if (arr[mid] <= des){
                index = mid;
                L = mid + 1;
            }
        }
        return index;
    }
    // =========================================================================================



    // 高级方法：利用滑动窗口。以0号元素作为开头，扩到能达到的最右边界，算出结果，然后左边弹出一个，再继续
    //         看能不能再推进。 时间复杂度：O(N)
    public static int coverNodeV2(int[] arr, int k){ // 题目说了给的数组就是有序的
        if (arr == null || arr.length == 0 || k <= 0)
            return 0;
        int res = 0;
        int L = 0, R = 0;
        while (L <= arr.length - 1){
            while (R < arr.length && arr[R] <= arr[L] + k){
                R++;
            }
            res = Math.max(res, R - L);
            L++;
        }
        return res;
    }
    // =========================================================================================


    // for test
    public static void test(int testTime, int maxLen, int maxVal){
        for (int i = 0; i < testTime; i++) {
            int[] arr = arrays.randomNoNegativeArr(maxLen, maxVal);
            int k = arrays.randomNoNegativeNum(maxVal);
            int res1 = coverNodeV1(arr, k);
            int res2 = coverNodeV2(arr, k);
            if (res1 != res2){
                System.out.println("Failed");
                System.out.println("初级方法： " + res1);
                System.out.println("高级方法： " + res2);
                arrays.printArray(arr);
                return;
            }
        }
        System.out.println("AC");
    }



    public static void main(String[] args) {
        test(10000, 20, 100);
    }
}
