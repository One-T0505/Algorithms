package Basic.DynamicProgramming;

import java.util.Arrays;

public class Robot {
//    假设有排成一行的n个位置， 记为1~N，cells一定大于或等于2
//    开始时机器人在其中的m位置上(start 一定是1~n中的一个)
//    如果机器人来到1位置，那么下一步只能往右来到2位置; 如果机器人来到n位置， 那么下一步只能往左来到n-1位置;
//    如果机器人来到中间位置，那么下一步可以往左走或者往右走;
//    规定机器人必须走k步，最终能来到p位置(p也是1~n中的一个)的方法有多少种
//    给定四个参数n、start、steps、end，返回方法数。
    public static int robotV1(int N, int start, int steps, int end){
        // 无效参数的情况
        if (N < 2 || start < 1 || start > N || steps < 1 || end < 1 || end > N)
            return 0;
        return walk1(N, start, steps, end);
    }

    // N 还是表示一共多少个位置，end 还是表示目标位置
    // cur 表示当前位置，rest表示还能走几步
    private static int walk1(int N, int cur, int rest, int end) {
        // 如果没有剩余步数了，当前的cur位置就是最后的位置
        // 如果最后的位置停在P上，那么之前做的移动是有效的；如果最后的位置没在P上，那么之前做的移动是无效的
        if (rest == 0)
            return cur == end ? 1 : 0;
        if (cur == 1)
            return walk1(N, cur + 1, rest - 1, end);
        if (cur == N)
            return walk1(N, cur - 1, rest - 1, end);
        // 如果还有rest步要走，而当前的cur位置在中间位置上，那么当前这步可以走向左，也可以走右
        // 走向左之后，后续的过程就是，来到cur-1位置 上，还剩rest-1步要走
        // 走向右之后，后续的过程就是，来到cur+1位置. 上，还剩rest-1步要走
        // 走向左、走向右是截然不同的方法，所以总方法数要都算上
        return walk1(N, cur - 1, rest - 1, end) + walk1(N, cur + 1, rest - 1, end);
    }
    // ==================================================================================================

    // 上述的这种暴力递归方法是有重复计算的。可以看出递归中n、p两个参数是固定不变的，结果只取决于(start,steps)的组合，如果有
    // 一个cache存放各种组合的结果，当重复计算时只需要从cache中返回结果。
    // 像这样的，用缓存来减少递归过程中重复计算的方法就叫记忆化搜索
    public static int robotV2(int N, int start, int steps, int end){
        // 无效参数的情况
        if (N < 2 || start < 1 || start > N || steps < 1 || end < 1 || end > N)
            return 0;
        // 因为 start 范围：1~N  steps 范围：0~steps  所以申请一个 (N+1)*(steps+1)的缓存表
        int[][] cache = new int[N + 1][steps + 1];
        // 默认将cache所有元素都设为-1，表示从来没计算过，当递归访问某个元素时发现不是-1时说明已经计算过了，直接取值即可
        for (int[] ints : cache) Arrays.fill(ints, -1);

        return walk2(N, start, steps, end, cache);
    }

    // 此时，所有的递归都要带上cache这张表一起玩
    private static int walk2(int N, int cur, int rest, int end, int[][] cache) {
        if (cache[cur][rest] != -1)
            return cache[cur][rest];
        if (rest == 0){
            cache[cur][rest] = cur == end ? 1 : 0;
            return cache[cur][rest];
        }
        if (cur == 1){
            cache[cur][rest] = walk2(N, cur + 1, rest - 1, end, cache);
            return cache[cur][rest];
        }
        if (cur == N){
            cache[cur][rest] = walk2(N, cur - 1, rest - 1, end, cache);
            return cache[cur][rest];
        }
        // 在中间位置
        cache[cur][rest] = walk2(N, cur - 1, rest - 1, end, cache) +
                walk2(N, cur + 1, rest - 1, end, cache);
        return cache[cur][rest];
    }
    // ==================================================================================================

    // 当彻底不需要递归的时候，就演变为了动态规划
    public static int robotV3(int N, int start, int steps, int end){
        // 无效参数的情况
        if (N < 2 || start < 1 || start > N || steps < 1 || end < 1 || end > N)
            return 0;
        int[][] cache = new int[N + 1][steps + 1];
        // 数组默认初始值就为0，所以第0列，也就是rest=0时，只有目标行设为1
        cache[end][0] = 1;
        // 先填列再填行
        for (int col = 1; col <= steps; col++) {
            cache[1][col] = cache[2][col - 1];
            for (int row = 2; row < N; row++)
                cache[row][col] = cache[row - 1][col - 1] + cache[row + 1][col - 1];
            cache[N][col] = cache[N - 1][col - 1];
        }
        return cache[start][steps];
    }

    public static void main(String[] args) {
        System.out.println(robotV1(4, 2, 5, 4));
        System.out.println(robotV2(4, 2, 5, 4));
        System.out.println(robotV3(4, 2, 5, 4));
    }
}
