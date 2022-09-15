package Recurse;

public class Backpack {
    // 给定两个长度都为N的数组weights和values,weights[i]和values[i]分别代表i号物品的重量和价值。
    // 给定一个正数bag,表示一个载重bag的袋子,你装的物品不能超过这个重量。返回你能装下最多的价值是多少?

    public static int getMaxValue(int[] w, int[] v, int bag){
        return process(w, v, 0, bag);
    }

    // 只考虑index及其之后的货物，rest表示背包剩余的载重量
    private static int process(int[] w, int[] v, int index, int rest) {
        // base case 1: 背包已经满了。返回-1表示没有方案
        if (rest < 0)
            return -1;
        if (index == w.length)  // base case 2: 背包还有容量，但是货物不够了，此时还没到达背包容量，所以返回0
            return 0;
        int value1 = process(w, v, index + 1, rest); // 没选index处的货物
        int value2 = -1;
        int p2Next = process(w, v, index + 1, rest - w[index]);
        if (p2Next != -1)
            value2 = v[index] + p2Next;
        return Math.max(value1, value2);

    }

    // weight为重量数组，value为对应的价值数组  bag为剩余背包容量
    // 改写递归到动态规划时，根据递归的模版改写
    public static int dp(int[] weight, int[] value, int bag){
        int N = weight.length;
        int[][] cache = new int[N + 1][bag + 1];
        // if (rest < 0)  return -1;  这个不需要考虑因为cache中rest只能从0～bag
        // if (index == w.length) return 0; 这个也不用管，因为数组默认初始值就是0

        // int value1 = process(w, v, index + 1, rest);
        // int value2 = -1;
        // int p2Next = process(w, v, index + 1, rest - w[index]);
        // if (p2Next != -1)
        //     value2 = v[index] + p2Next;
        // return Math.max(value1, value2);
        for (int index = N - 1; index >= 0; index++) {
            for (int rest = 0; rest <= bag; rest++) {
                int v1 = cache[index + 1][rest];
                int v2 = -1;
                if (rest - weight[index] >= 0)
                    v2 = value[index] + cache[index + 1][rest - weight[index]];
                cache[index][rest] = Math.max(v1, v2);
            }
        }
        return cache[0][bag];
    }
}
