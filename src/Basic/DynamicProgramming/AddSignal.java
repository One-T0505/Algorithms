package Basic.DynamicProgramming;

// 给定一个数组arr，你可以在每个数字之前决定+或者-，但是必须所有数字都参与。
// 再给定一个数target，请问最后算出target的方法数是多少?

import java.util.HashMap;

public class AddSignal {


    // 暴力递归   典型的从左到右尝试模型
    public static int calculate(int[] arr, int t){
        if (arr == null || arr.length == 0)
            return 0;
        int sum = 0;
        for (int e : arr)
            sum += e;
        if (t < (-sum) || t > sum)
            return 0;
        return f(arr, 0, t);
    }

    private static int f(int[] arr, int i, int rest) {
        if (i == arr.length)
            return rest == 0 ? 1 : 0;
        return f(arr, i + 1, rest - arr[i]) + f(arr, i + 1, rest + arr[i]);
    }
    // ===========================================================================================





    // 不难发现这肯定是有重复计算的，于是可以用记忆化搜索优化
    public static int calculate2(int[] arr, int t){
        if (arr == null || arr.length == 0)
            return 0;
        int sum = 0;
        for (int e : arr)
            sum += e;
        if (t < (-sum) || t > sum)
            return 0;
        // 有两个可变参数，其中rest的范围我们无法确定，所以不能用数组来表示，改用哈希表
        HashMap<Integer, HashMap<Integer, Integer>> dp = new HashMap<>();
        return g(arr, 0, t, dp);
    }

    private static int g(int[] arr, int i, int rest, HashMap<Integer, HashMap<Integer, Integer>> dp) {
        if (dp.containsKey(i) && dp.get(i).containsKey(rest))
            return dp.get(i).get(rest);
        int res = 0;
        if (i == arr.length)
            res = rest == 0 ? 1 : 0;
        else {
            res = g(arr, i + 1, rest - arr[i], dp) + g(arr, i + 1, rest + arr[i], dp);
        }
        if (!dp.containsKey(i))
            dp.put(i, new HashMap<>());
        dp.get(i).put(rest, res);
        return res;
    }
    // =============================================================================================





    // 动态规划
    public static int calculate3(int[] arr, int t){
        if (arr == null || arr.length == 0)
            return 0;
        int N = arr.length;
        int sum = 0;
        for (int e : arr)
            sum += Math.abs(e);
        if (t < (-sum) || t > sum)
            return 0;
        // 行数是i可以从0～N；列数表示的是rest，其范围可能在-sum～sum之间，所以要申请(2*sum + 1) 个格子
        // 因为 -10～10 有21个值
        int[][] dp = new int[N + 1][(sum << 1) | 1];
        // dp[N]这一行都应该设为0，因为没有元素可用，除了rest==0的时候可以是1  rest==0的这一列，可不是dp[N][0]
        // 因为我们是用 0~2sum 来表示 -sum~sum  所以坐标要换算。rest==0，对应的是第sum列
        dp[N][sum] = 1;
        for (int i = N - 1; i >= 0; i--){
            for (int j = 0; j < dp[0].length; j++){
                int p1 = (j - arr[i] >= 0 && j - arr[i] <= (sum << 1)) ? dp[i + 1][j - arr[i]] : 0;
                int p2 = (j + arr[i] >= 0 && j + arr[i] <= (sum << 1)) ? dp[i + 1][j + arr[i]] : 0;
                dp[i][j] = p1 + p2;
            }
        }
        // 本应是返回[0,t]位置的，不过还要下标换算
        return dp[0][t + sum];
    }
    // ============================================================================================





    // 上面是初级的动态规划。这里还有4个可以优化的点。
    // 1.既然是每个元素前面都可以加正负号，那么元素本身的正负性也就无关了，那就索性先把整个数组变成非负的；这样做的好处在于，
    //   当我们再判断变量范围去申请缓存大小时，全部为正可以比上面的方法申请的缓存小一半。因为sum之前是-upper～upper，
    //   现在是0～upper。
    // 2.继续剪枝。我们求得upper以后，将其中一个元素x变为负数后，那么upper就变成了upper-2x，所以不管改变多少个数的符号，
    //   一定是和upper差的是一个偶数，并且奇偶性不变。所以，如果target和upper的奇偶性不一致，那么无论怎么调整，都不可能
    //   拼出target。因为upper是所有取正，将一个数x变为负，upper就会减去一个偶数2x，但是奇偶性不变。
    // 3.将数组处理成非负数组后，就是说从中选一批数当正数，剩下的当负数拼出target。假设有一个集合P，里面是从非负数组中挑选
    //   的当作正数的元素；N元素就是剩下的数组元素，即将作为负数；我们要求的关系式满足：P - N == target -->
    //   P - N + (N + P) == target + (N + P) --> 2P == target + upper --> p == (target + upper) / 2.
    //   所以问题就成了：从整个非负数组中，每个数都可用，挑选一些组成(target + upper) / 2，这不就是经典的背包问题吗？？？
    // 4.缓存表可以用状态压缩，用单行数组完成。
    public static int calculate4(int[] arr, int t){
        if (arr == null || arr.length == 0)
            return 0;
        int N = arr.length;
        int sum = 0;
        // for循环做了两件事：处理成非负元素；统计上限
        for (int i = 0; i < N; i++) {
            arr[i] = arr[i] < 0 ? -arr[i] : arr[i];
            sum += arr[i];
        }
        //      超过上限                   奇偶性不一致
        if (t > sum || ((sum & 1) ^ (t & 1)) != 0)
            return 0;
        // 背包问题
        return backpack(arr,  (t + sum) >> 1);
    }

    private static int backpack(int[] arr, int target) {
        if (target < 0)
            return 0;
        int N = arr.length;
        int[] cache = new int[target + 1];
        cache[0] = 1;
        for (int i = N - 1; i >= 0; i--) {
            for (int rest = target; rest >= 0; rest--) {
                int p1 = cache[rest];
                int p2 = rest - arr[i] < 0 ? 0 : cache[rest - arr[i]];
                cache[rest] = p1 + p2;
            }
        }
        return cache[target];
    }


    public static void main(String[] args) {
        int[] arr = {2, 4, 1, -5, 2, -1, 3, -2, 6, 4};
        System.out.println(calculate(arr, 8));
        System.out.println(calculate2(arr, 8));
        System.out.println(calculate3(arr, 8));
        System.out.println(calculate4(arr, 8));
    }
}
