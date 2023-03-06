package Tips;
import utils.arrays;

// 这个文件主要讲述的技巧就是：根据数据规模猜解法！！
// 1) C/C++， 1秒处理的指令条数为10的8次方
// 2) Java等语言，1~4秒处理的指令条数为10的8次方

public class Guess {
    // int[] d， d[i] : i号怪兽的能力   int[] p， p[i] : i号怪兽需要的钱
    // 开始时你的能力是0，你的目标是从0号怪兽开始，通过所有的怪兽。如果你当前的能力，小于i号怪兽
    // 的能力，你必须付出p[i]的钱，贿赂这个怪兽，然后怪兽就会加入你，他的能力直接累加到你的能力上;
    // 如果你当前的能力大于等于d[i]，你可以选择直接通过，你的能力并不会下降，你也可以选择贿赂这个怪
    // 兽，然后怪兽就会加入你，他的能力直接累加到你的能力上。返回通过所有的怪兽，需要花的最小钱数。

    // 第一种尝试
    public static int adventureV1(int[] d, int[] p){
        if (d == null || p == null || d.length != p.length) // 无效参数
            return -1;
        return process1(d, p, 0, 0);
    }

    // index表示当前要从哪个怪兽开始做决定，ability表示当前的能力，expense表示已经花出去的钱
    private static int process1(int[] d, int[] p, int index, int ability) {
        if (index == d.length) // 已经没有怪兽了，所以不需要再花钱了，返回0
            return 0;
        if (ability < d[index]){
            return p[index] + process1(d, p, index + 1, ability + d[index]);
        } else {
            int p1 = process1(d, p, index + 1, ability);   // 不贿赂
            int p2 = p[index] + process1(d, p, index + 1, ability + d[index]);  // 贿赂
            return Math.min(p1, p2);
        }
    }

    // 由第一种尝试改成动态规划
    public static int dpV1(int[] d, int[] p){
        if (d == null || p == null || d.length != p.length) // 无效参数
            return -1;
        int limit = 0;
        for (int elem : d)
            limit += elem;
        int N = d.length;
        int[][] cache = new int[N + 1][limit + 1];
        for (int index = N - 1; index >= 0; index--) {
            for (int ability = limit; ability >= 0; ability--) {
                if (ability < d[index])
                    cache[index][ability] = p[index] + (ability + d[index] > limit ? 0 :
                            cache[index + 1][ability + d[index]]);
                else
                    cache[index][ability] = Math.min(cache[index + 1][ability],
                            (ability + d[index] > limit ? 0 :
                                    p[index] + cache[index + 1][ability + d[index]]));
            }
        }
        return cache[0][0];
    }
    // =====================================================================================================


    // 现在尝试另一种递归的思路
    public static int adventureV2(int[] d, int[] p){
        if (d == null || p == null || d.length != p.length) // 无效参数
            return -1;
        int limit = 0;
        for (int elem : p)
            limit += elem;
        int N = d.length;
        for (int expense = 0; expense < limit; expense++) {
            // 钱数不断增加，碰到的第一个返回有效解的必然是花费最低的情况
            if (process2(d, p, N - 1, expense) != -1)
                return expense;
        }
        return limit;
    }

    // 从0....index号怪兽，花的钱，必须严格==expense
    // 如果通过不了，返回-1
    // 如果可以通过，返回能通过情况下的最大能力值
    private static int process2(int[] d, int[] p, int index, int expense) {
        if (index == -1) // 一个怪兽也没遇到呢
            return expense == 0 ? 0 : -1;
        // 1.不贿赂当前怪兽
        int ability1 = process2(d, p, index - 1, expense);
        int p1 = -1;
        if (ability1 != -1 && ability1 >= d[index]) // 满足条件才能不贿赂
            p1 = ability1;
        // 要贿赂当前怪兽
        int ability2 = process2(d, p, index - 1, expense - p[index]);
        int p2 = ability2 != -1 ? ability2 + d[index] : -1;
        return Math.max(p1, p2);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            int[] d = arrays.fixedLenArray(10, 100);
            int[] p = arrays.fixedLenArray(10, 100);
            int res1 = adventureV2(d, p);
            int res2 = dpV1(d, p);
            if (res1 != res2){
                System.out.println("Failed");
                System.out.println("暴力递归：" + res1);
                System.out.println("动态规划：" + res2);
                arrays.printArray(d);
                arrays.printArray(p);
                return;
            }
        }
        System.out.println("AC");
    }
}
