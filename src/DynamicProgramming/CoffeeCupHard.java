package DynamicProgramming;

// 给定一个数组arr，arr[i]代表第i号咖啡机泡一杯咖啡的时间，给定一个正数N，表示N个人等着咖啡机泡咖啡，每台咖啡机只能轮流泡咖啡。
// 只有一台洗杯机，一次只能洗一个杯子，时间耗费a，洗完才能洗下一杯。每个咖啡杯也可以自己挥发干净，时间耗费b，咖啡杯可以并行挥发。
// 假设所有人拿到咖啡之后立刻喝干净,返回从开始等到所有咖啡杯变干净的最短时间。
// 四个参数: int[] arr、int N, int a、 int b

import java.util.PriorityQueue;

// 这个问题其实挺困难的，我们先把问题拆分一下。有一个子问题是：安排一种调度策略让所有人都喝完咖啡的时间最短，
// 然后再去处理如何清洗这些杯子。
// 这里需要使用小根堆来安排哪些人应该在哪个机器等。定义一个machine类，有两个成员变量：1.何时该机器可用；2.做一杯咖啡的时间
// 两者之和就是我拿到咖啡的时间并喝完的时间。小根堆就以这个两者之和为排序规则排序，每次来一个人就从小根堆弹出一个，
// 修改何时可用之后再入堆，等所有人都处理完后，堆中就有结果了。
public class CoffeeCupHard {

    public static int washCoffeeCup(int[] arr, int a, int b, int N){
        PriorityQueue<Machine> heap = new PriorityQueue<>(((o1, o2) ->
                ((o1.access + o1.cost) - (o2.access + o2.cost))));
        for (int j : arr)
            heap.add(new Machine(0, j));
        int[] finish = new int[N];  // 该数组对应于每个人喝完咖啡的时间点
        for (int i = 0; i < N; i++) {
            Machine cur = heap.poll();
            cur.access += cur.cost;  // 加上cost就表示下一次何时可以使用这个机器
            finish[i] = cur.access;
            heap.add(cur);
        }
        return process1(finish, a, b, 0, 0);
    }

    // 该方法表示：finish是一个数组，每个元素表示那个人在何时喝完咖啡准备洗杯子；
    // a 是洗杯机器洗一个杯子的时间   b 是杯子自然挥发变干净的时间  access 表示洗杯子的机器何时可用
    // 该方法返回从index处开始及其后面所有杯子都洗干净的最早结束时间
    private static int process1(int[] finish, int a, int b, int index, int access) {
        if (index == finish.length)
            return 0;
        // index号杯子用机器洗
        // access+a 表示index处的杯子何时能干净
        // process1(finish, a, b, index + 1, access + a) 表示 index+1...所有杯子洗干净的最早结束时间
        // 第一个max表示：我喝完的时间和洗杯机能用的时间去较大值才是真正可以开始洗的时间，如果6时刻喝完了，机器8时刻才可用
        // 那就得等到8时刻才能洗
        // 第二个max表示要让两者都干净才行
        int clean = Math.max(finish[index], access) + a;
        int p1 = Math.max(clean, process1(finish, a, b, index + 1, clean));
        // index处的杯子选择自然挥发，但是不占用机器，所以后面传入的参数依然是access
        int p2 = Math.max(finish[index] + b, process1(finish, a, b, index + 1, access));
        // 返回两种选择下数值最小的那个
        return Math.min(p1, p2);
    }
    // ===================================================================================================


    // 将暴力递归改成动态规划。 这个问题是我们遇到的业务限制模型的第一个题目！！！
    // 可以发现可变参数只有index和access，index的范围很好确定，但是access的范围不好确定，像这样的情况就是也无限制模型！！
    // 碰到这种类型的题，就需要我们自己想一些限制来确定可变参数的范围。
    // access表示机器可用的时间，那它的最大值就是所有杯子都要用机器洗的结果
    public static int dp(int[] finish, int a, int b){
        int accessLimit = 0;
        for (int j : finish)
            accessLimit = Math.max(accessLimit, j) + a;
        int N = finish.length;
        int[][] cache = new int[N  + 1][accessLimit + 1];
        for (int index = N - 1; index >= 0; index--) {
            for (int access = 0; access <= accessLimit; access++) {
                int clean = Math.max(finish[index], access) + a;
                int p1 = Math.max(clean, cache[index + 1][clean]);
                int p2 = Math.max(finish[index] + b, cache[index + 1][access]);
                cache[index][access] = Math.min(p1, p2);
            }
        }
        return cache[0][0];
    }

    static class Machine {
        public int access; // 何时可用
        public int cost; // 何时可用

        public Machine(int access, int cost) {
            this.access = access;
            this.cost = cost;
        }
    }
}
