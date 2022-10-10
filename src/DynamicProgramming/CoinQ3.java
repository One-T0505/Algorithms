package DynamicProgramming;

// arr是货币数组，其中的值都是正数。再给定一个正数aim。每个值都认为是一张货币，
// 认为值相同的货币没有任何不同,返回组成aim的方法数。
// 例如: arr= {1,2,1,1,2,1,2}，aim=4  方法: 1+1+1+1、1+1+2、 2+2
//      一共就3种方法，所以返回3

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoinQ3 {
    static class Info {
        public int[] kinds;  // 该数组记录的是面值种类
        public int[] nums;   // 该数组记录的是每种面值的货币有多少张

        public Info(int[] kinds, int[] nums) {
            this.kinds = kinds;
            this.nums = nums;
        }
    }

    // 暴力递归
    public static int coin(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        Info info = preProcess(arr);
        return process(info.kinds, info.nums, 0, aim);
    }

    // 首先要将arr数组去重并统计每种面值的货币有几张
    private static Info preProcess(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>(); // 记录每种面值的货币有多少张
        for (int cur : arr) {
            if (!map.containsKey(cur))
                map.put(cur, 1);
            else
                map.put(cur, map.get(cur) + 1);
        }
        int N = map.size();
        int[] kinds = new int[N];
        int[] nums = new int[N];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            kinds[index] = entry.getKey();
            nums[index++] = entry.getValue();
        }
        return new Info(kinds, nums);
    }

    private static int process(int[] kinds, int[] nums, int index, int rest) {
        if (rest < 0)
            return 0;
        if (index == kinds.length)
            return rest == 0 ? 1 : 0;
        int res = 0;
        for (int i = 0; (i <= nums[index] && i * kinds[index] <= rest); i++)
            res += process(kinds, nums, index + 1, rest - i * kinds[index]);
        return res;
    }
    // ================================================================================================


    // 初步的动态规划
    public static int dpV1(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        Info info = preProcess(arr);
        int N = info.kinds.length;
        int[][] cache = new int[N + 1][aim + 1];
        cache[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int res = 0;
                for (int i = 0; (i <= info.nums[index] && i * info.kinds[index] <= rest); i++)
                    res += cache[index + 1][rest - i * info.kinds[index]];
                cache[index][rest] = res;
            }
        }
        return cache[0][aim];
    }
    // 可以发现初步的动态规划仍然有枚举行为，所以需要寻找进一步可优化的方法。这里的优化方法和Q2还不一样，因为在Q2里，
    // 每一种面值的张数是无限的，但是这里有张数限制。举例：
    // cache[3][13] 依赖那些元素呢？ info.kinds[3] = 3  info.numS[3] = 2
    // 那么 cache[3][13] 只能依赖于：cache[4][13]  cache[4][10]  cache[4][7]  因为有张数限制
    // 如果是在Q2中，那么 cache[3][13] 可依赖于：cache[4][13] cache[4][10] cache[4][7] cache[4][4] cache[4][1]
    // 在Q2中，总结出的优化公式为：
    //     cache[index][rest] = cache[index + 1][rest] + cache[index][rest - arr[index]]
    // 这个公式在这里行不通，举例：拿上面的cache[3][13]举例，按照Q2的公式就是：
    //     cache[3][13] = cache[4][13] + cache[3][10]
    // 但是在Q3中，cache[3][10] = cache[4][10] + cache[4][7] + cache[4][4]
    // 这样算出的 cache[3][13] 就多加出了一个 cache[4][4]  所以要减去
    // 于是，我们总结出了一般的优化公式：
    //    cache[index][rest] = cache[index + 1][rest] +
    //                         cache[index][rest - arr[index]] -
    //                         cache[index][rest - (nums[index] + 1) * kinds[index]]
    //
    // 还有一个小问题要注意：如果某种货币的数量非常大比如100万张，而aim只有5，那必然是在张数没用完前先让rest<0了，
    // 如果rest<0了就得立刻停止不管张数有没有找完

    public static int dpV2(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        Info info = preProcess(arr);
        int N = info.kinds.length;
        int[][] cache = new int[N + 1][aim + 1];
        cache[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            int curValue = info.kinds[index];
            int curNum = info.nums[index];
            for (int rest = 0; rest <= aim; rest++) {
                cache[index][rest] = cache[index + 1][rest];
                if (rest - curValue >= 0)
                    cache[index][rest] += cache[index][rest - curValue];
                if (rest - (curNum + 1) * curValue >= 0)
                    cache[index][rest] -= cache[index + 1][rest - (curNum + 1) * curValue];
            }
        }
        return cache[0][aim];
    }


    public static void main(String[] args) {
        int[] arr = {1, 2, 1, 1, 2, 1, 2};
        int aim = 4;
        System.out.println(coin(arr, aim));
        System.out.println(dpV1(arr, aim));
        System.out.println(dpV2(arr, aim));
    }
}
