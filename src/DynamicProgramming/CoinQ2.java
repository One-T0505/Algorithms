package DynamicProgramming;

// 给一个数组arr，每个元素表示一种面值，不可能为负数，并且面值不重复，每种面值的货币使用次数不限制。
// 输入一个总金额aim，仅用arr提供的面值可以有多少种方法组出金额N？

public class CoinQ2 {
    // 暴力递归法
    public static int coin(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        return process(arr, 0, aim);
    }

    // 可以自由使用arr[ index...]所有的面值，每一种面值都可以使用任意张，
    // 组成rest有多少种方法
    private static int process(int[] arr, int index, int rest) {
        if (rest < 0)   // 这个分支其实可以不用写，因为下面调用递归的时候保证了：nums * arr[index] <= rest
            return 0;
        if (index == arr.length)
            return rest == 0 ? 1 : 0;
        int res = 0;
        // 每一种面值的货币都尝试所有可能使用的张数
        for (int nums = 0; nums * arr[index] <= rest; nums++) {
            res += process(arr, index + 1, rest - nums * arr[index]);
        }
        return res;
    }
    // ====================================================================================================


    // 改成动态规划
    public static int dp(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        int N = arr.length;
        int[][] cache = new int[N + 1][aim + 1];
        cache[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int res = 0;
                for (int nums = 0; nums * arr[index] <= rest; nums++) {
                    res += rest - nums * arr[index] < 0 ? 0 : cache[index + 1][rest - nums * arr[index]];
                }
                cache[index][rest] = res;
            }
        }
        return cache[0][aim];
    }
    // 这里就有点意思了，之前将递归改成动态规划后，计算每个格子的值，不管是什么依赖关系，时间复杂度都是O(1)。而这个题目的
    // 动态规划版里，计算每个格子的值还需要继续枚举，时间复杂度变成了O(N).当出现这种计算一个单元的时间复杂度不是O(1)时，
    // 就要考虑继续优化，从依赖关系中寻找重复计算。
    // eg：假如cache中随便一个格子cache[3][16]，arr= {1, 2, 3, 5, 10}.他要依赖：
    //     cache[4][16]、cache[4][11]、cache[4][6]、cache[4][1]
    //    而 cache[3][11] 要依赖：cache[4][11]、cache[4][6]、cache[4][1]
    //    于是：cache[3][16] = cache[4][16] + cache[3][11]
    // 所以，这样就找到了重复计算，这就是优化的方向.
    // 总结后的优化公式为：cache[index][rest] = cache[index + 1][rest] + cache[index][rest - arr[index]]
    // 边界情况仍然要单独考虑
    public static int dpV2(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        int N = arr.length;
        int[][] cache = new int[N + 1][aim + 1];
        cache[N][0] = 1;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                cache[index][rest] = cache[index + 1][rest];
                if (rest - arr[index] >= 0)
                    cache[index][rest] += cache[index][rest - arr[index]];
            }
        }
        return cache[0][aim];
    }
    // ====================================================================================================

    public static void main(String[] args) {
        int[] arr = {1, 2, 5, 10};
        int aim = 3000;
        long s1 = System.currentTimeMillis();
        int res1 = coin(arr, aim);
        long e1 = System.currentTimeMillis();
        System.out.println(res1 + "  " + (e1 - s1) + "ms");
        long s2 = System.currentTimeMillis();
        int res2 = dp(arr, aim);
        long e2 = System.currentTimeMillis();
        System.out.println(res2 + "  " + (e2 - s2) + "ms");
        long s3 = System.currentTimeMillis();
        int res3 = dpV2(arr, aim);
        long e3 = System.currentTimeMillis();
        System.out.println(res3 + "  " + (e3 - s3) + "ms");
    }
}
