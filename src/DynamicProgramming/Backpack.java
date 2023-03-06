package DynamicProgramming;

import java.util.Arrays;
import utils.arrays;

public class Backpack {
    // 给定两个长度都为N的数组weights和values,weights[i]和values[i]分别代表i号物品的重量和价值,所有值均非负。
    // 给定一个正数bag,表示一个载重bag的袋子,你装的物品不能超过这个重量。返回你能装下最多的价值是多少?

    public static int backpackV1(int[] w, int[] v, int bag){
        if (w == null || v == null || w.length == 0 || v.length == 0 || w.length != v.length)
            return -1; // 表示无方案
        return process1(w, v, 0, bag);
    }

    // 只考虑index及其之后的货物，rest表示背包剩余的载重量
    // process函数返回值表示：从index处开始做选择能得到的最大价值
    private static int process1(int[] w, int[] v, int index, int rest) {
        // base case 1: 背包已经满了。返回-1表示没有方案
        if (rest < 0) // rest<=0还是rest<0要看题目怎么说，如果重量数组w中可能存在重量为0的货物，那此时只能写rest<0
            return -1; // 返回-1表示前面的选择不可行
        if (index == w.length)  // base case 2: 背包还有容量，但是货物不够了，所以从index做选择能得到的最大价值为0
            return 0;
        int p1 = process1(w, v, index + 1, rest); // 没选index处的货物
        int p2 = -1;
        int next = process1(w, v, index + 1, rest - w[index]); // 选index处的货物
        if (next != -1)
            p2 = v[index] + next;
        return Math.max(p1, p2);
    }
    // ===================================================================================================



    // 记忆化搜索版本
    public static int backpackV2(int[] w, int[] v, int bag){
        if (w == null || v == null || w.length == 0 || v.length == 0 || w.length != v.length)
            return -1; // 表示无方案
        int N = w.length;
        int[][] cache = new int[N + 1][bag + 1];
        // -1表示cache[i][j]这种方案还没计算过
        for (int index = 0; index <= N; index++)
            Arrays.fill(cache[index], -1);
        return process2(w, v, 0, bag, cache);
    }

    // 只考虑index及其之后的货物，rest表示背包剩余的载重量
    // process函数返回值表示：从index处开始做选择能得到的最大价值
    private static int process2(int[] w, int[] v, int index, int rest, int[][] cache) {
        // base case 1: 背包已经满了。返回-1表示没有方案
        if (rest < 0)
            return -1;
        if (cache[index][rest] != -1)
            return cache[index][rest];
        if (index == w.length){
            cache[index][rest] = 0;
            return 0;
        }  // base case 2: 背包还有容量，但是货物不够了，所以从index做选择能得到的最大价值为0
        int p1 = process2(w, v, index + 1, rest, cache); // 没选index处的货物
        int p2 = -1;
        int next = process2(w, v, index + 1, rest - w[index], cache); // 选index处的货物
        if (next != -1)
            p2 = v[index] + next;
        cache[index][rest] = Math.max(p1, p2);
        return cache[index][rest];
    }
    // ===================================================================================================



    // weight为重量数组，value为对应的价值数组  bag为剩余背包容量
    // 改写递归到动态规划时，根据递归的模版改写
    public static int dp(int[] w, int[] v, int bag){
        if (w == null || v == null || w.length == 0 || v.length == 0 || w.length != v.length)
            return -1; // 表示无方案
        int N = w.length;
        int[][] cache = new int[N + 1][bag + 1];
        // cache[N]这一整行都需要设为0，但是初始化本来就为0，所以不需要额外设置
        for (int index = N - 1; index >= 0; index--) {
            // 第0列也全部为0，因为rest==0，不管从哪个元素开始往后做选择，能得到的收益都是0
            for (int rest = 1; rest <= bag; rest++) {
                int p1 = cache[index + 1][rest];
                int p2 = -1;
                if (rest - w[index] >= 0)
                    p2 = v[index] + cache[index + 1][rest - w[index]];
                cache[index][rest] = Math.max(p1, p2);
            }
        }
        return cache[0][bag];
    }

    public static void main(String[] args) {
        int maxLen = 40;
        int maxBag = 100;
        for (int i = 0; i < 10000; i++) {
            int len = ((int) (Math.random() * maxLen)) + 1;
            int[] weights = arrays.fixedLenArray(len, 50);
            int[] values = arrays.fixedLenArray(len, 50);
            int bag = (int) (Math.random() * maxBag) + 1;

            int res1 = backpackV1(weights, values, bag);
            int res2 = backpackV2(weights, values, bag);
            int res3 = dp(weights, values, bag);
            if (res1 != res3 || res2 != res3){
                arrays.printArray(weights);
                arrays.printArray(values);
                System.out.println(bag);
                System.out.println(res1 + "\t暴力递归");
                System.out.println(res2 + "\t记忆化搜索");
                System.out.println(res3 + "\t动态规划");
                return;
            }
        }
        System.out.println("AC");
    }
}
