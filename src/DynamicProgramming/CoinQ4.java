package DynamicProgramming;

// arr是面值数组，其中的值都是正数且没有重复。再给定一个正数aim。每个值都认为是一种面值，且认为张数是无限的。
// 返回组成aim的最少货币数。注意：返回的不是方法数！！！
// eg：arr={2, 5, 10} aim=100， 应该返回10，因为用10张10元的是使用货币数量最少的

// 判断：本题属于从左到右的尝试模型

public class CoinQ4 {

    // 暴力递归尝试
    public static int coin(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        return process(arr, 0, aim);
    }

    // 方法定义：index表示从哪一个数组元素开始做决定，0..index-1的货币已经决定好了怎么用，所以不用管
    //         rest表示还需要拼出多少钱。方法返回拼出rest用的最少的货币数量
    private static int process(int[] arr, int index, int rest) {
        if (rest < 0)   // 这个分支可以删除，因为下面调用递归时，是保证了传入的rest>=0
            return Integer.MAX_VALUE;  // 这个也可以用-1标记搞不定这件事，但是下面代码写起来比较麻烦
        if (index == arr.length)
            return rest == 0 ? 0 : Integer.MAX_VALUE;
        else { // index != arr.length && rest >= 0
            int res = Integer.MAX_VALUE;
            for (int i = 0; i * arr[index] <= rest; i++){
                int p = process(arr, index + 1, rest - i * arr[index]);
                if (p != Integer.MAX_VALUE)
                    res = Math.min(res, p + i);
            }
            return res;
        }
    }
    // ===================================================================================================


    // 动态规划
    public static int dpV1(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        int N = arr.length;
        int[][] cache = new int[N + 1][aim + 1];
        cache[N][0] = 0;    // 这句可以省略，但是为了表现改写的过程还是加上了
        for (int i = 1; i <= aim; i++)
            cache[N][i] = Integer.MAX_VALUE;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                int res = Integer.MAX_VALUE;
                for (int i = 0; i * arr[index] <= rest; i++){
                    int p = cache[index + 1][rest - i * arr[index]];
                    if (p != Integer.MAX_VALUE)
                        res = Math.min(res, p + i);
                }
                cache[index][rest] = res;
            }
        }
        return cache[0][aim];
    }
    // ====================================================================================================


    // 继续优化，消除枚举行为。这里的优化有点难懂，需要画图举例。假设arr={1, 3, 2, 4}, aim=5  用m表示Integer.MAX_VALUE
    // 随便找一个格子，比如要填写c[2][5]
    //
    //    0  1  2  3  4  5
    //  0
    //  1
    //  2          ★     ?
    //  3    c     b     a
    //  4
    //
    // 从动态规划V1中知道，如果a、b、c都不是系统最大值，那么?处的值就是Math.min(a+0, b+1, c+2)的结果
    // 很自然联想到★处的值，同样也是在b、c都不是系统最大值的前提下，Math.min(b+0, c+1)的结果
    // 所以我们找到了通过★推算？的省掉枚举的方法。即：
    //      ? = Math.min(a+0, ★+1)
    //
    public static int dpV2(int[] arr, int aim){
        if (arr == null || arr.length == 0 || aim <= 0)
            return 0;
        int N = arr.length;
        int[][] cache = new int[N + 1][aim + 1];
        cache[N][0] = 0;    // 这句可以省略，但是为了表现改写的过程还是加上了
        for (int i = 1; i <= aim; i++)
            cache[N][i] = Integer.MAX_VALUE;
        for (int index = N - 1; index >= 0; index--) {
            for (int rest = 0; rest <= aim; rest++) {
                cache[index][rest] = cache[index + 1][rest];  // 省略+0
                if (rest - arr[index] >= 0 && cache[index][rest - arr[index]] != Integer.MAX_VALUE)
                    cache[index][rest] = Math.min(cache[index][rest - arr[index]] + 1, cache[index][rest]);
            }
        }
        return cache[0][aim];
    }


    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 4};
        int aim = 21;
        System.out.println(coin(arr, aim));
        System.out.println(dpV1(arr, aim));
        System.out.println(dpV2(arr, aim));
    }
}
